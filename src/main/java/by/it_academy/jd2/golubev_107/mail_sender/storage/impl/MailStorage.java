package by.it_academy.jd2.golubev_107.mail_sender.storage.impl;

import by.it_academy.jd2.golubev_107.mail_sender.platform.DBUtil;
import by.it_academy.jd2.golubev_107.mail_sender.storage.IMailStorage;
import by.it_academy.jd2.golubev_107.mail_sender.storage.connection.IConnectionManager;
import by.it_academy.jd2.golubev_107.mail_sender.storage.dto.EmailStorageOutDto;
import by.it_academy.jd2.golubev_107.mail_sender.storage.dto.RecipientOutDto;
import by.it_academy.jd2.golubev_107.mail_sender.storage.entity.Email;
import by.it_academy.jd2.golubev_107.mail_sender.storage.entity.Recipient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MailStorage implements IMailStorage {

    private static final String INSERT_EMAIL_QUERY = "INSERT INTO app.email (title, body_text) VALUES(?, ?) RETURNING id;";
    private static final String SELECT_EMAIL_QUERY = "SELECT id, title, body_text FROM app.email WHERE id = ?;";
    private static final String INSERT_CROSS_EMAIL_RECIPIENTS_QUERY = """
            INSERT INTO app.cross_email_address_type (email_id, address_id, type_id)
            VALUES (?, ?, ?);
            """;
    private static final String SELECT_RECIPIENT_TYPE_QUERY = "SELECT id FROM app.recipient_type WHERE type = ?;";
    private static final String SELECT_ALL_RECIPIENT_TYPE_QUERY = "SELECT id, type FROM app.recipient_type;";
    private static final String SELECT_RECIPIENTS_BY_EMAIL_ID_QUERY = """
            SELECT id, address_id, type_id
            FROM app.cross_email_address_type
            WHERE email_id = ?;
            """;
    private final IConnectionManager connectionManager;

    public MailStorage(IConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Long create(Email email) {
        Connection connection = null;
        Long emailId;
        try {
            connection = connectionManager.getConnection();
            connection.setAutoCommit(false);
            DBUtil.transactionBegin(connection);

            emailId = insertEmail(email, connection);
            insertRecipientsByType(emailId, Recipient.RecipientType.TO.name(), email.getRecipientsTo(), connection);
            insertRecipientsByType(emailId, Recipient.RecipientType.CC.name(), email.getRecipientsCC(), connection);
            insertRecipientsByType(emailId, Recipient.RecipientType.BCC.name(), email.getRecipientsBCC(), connection);

            connection.commit();
        } catch (SQLException e) {
            DBUtil.transactionRollback(connection);
            throw new RuntimeException("Failed to create an email" + email);
        } catch (RuntimeException e) {
            DBUtil.transactionRollback(connection);
            throw new RuntimeException("Failed to create an email" + email, e);
        } finally {
            DBUtil.connectionClose(connection);
        }
        return emailId;
    }

    @Override
    public EmailStorageOutDto readById(Long id) {
        try (Connection connection = connectionManager.getConnection();) {
            EmailStorageOutDto emailOutDto = selectEmailPart(id, connection);
            if (emailOutDto != null) {
                Map<Long, Recipient.RecipientType> allRecTypes = selectAllRecipientTypes(connection);
                selectRecipientsPart(emailOutDto, allRecTypes, connection);
                return emailOutDto;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read an email by id: " + id);
        }
        return null;
    }

    private EmailStorageOutDto selectEmailPart(Long receivedId, Connection connection) throws SQLException {
        try (PreparedStatement selectEmailStmt = connection.prepareStatement(SELECT_EMAIL_QUERY)) {
            selectEmailStmt.setLong(1, receivedId);
            try (ResultSet rs = selectEmailStmt.executeQuery()) {
                selectEmailStmt.clearParameters();
                if (rs.next()) {
                    return EmailStorageOutDto.builder()
                                             .setId(rs.getLong("id"))
                                             .setTitle(rs.getString("title"))
                                             .setText(rs.getString("body_text"))
                                             .build();
                }
            }
        }
        return null;
    }

    private Map<Long, Recipient.RecipientType> selectAllRecipientTypes(Connection connection) throws SQLException {
        try (PreparedStatement selectAllRecTypeStmt = connection.prepareStatement(SELECT_ALL_RECIPIENT_TYPE_QUERY)) {
            Map<Long, Recipient.RecipientType> allRecTypes = new HashMap<>();
            try (ResultSet rs = selectAllRecTypeStmt.executeQuery()) {
                while (rs.next()) {
                    allRecTypes.put(rs.getLong("id"),
                            Recipient.RecipientType.valueOf(rs.getString("type")));
                }
            }
            return allRecTypes;
        }
    }

    private void selectRecipientsPart(EmailStorageOutDto emailOutDto, Map<Long, Recipient.RecipientType> allRecTypes,
                                      Connection connection) throws SQLException {
        try (PreparedStatement selectRecByEmailIdStmt = connection.prepareStatement(
                SELECT_RECIPIENTS_BY_EMAIL_ID_QUERY)) {
            selectRecByEmailIdStmt.setLong(1, emailOutDto.getId());
            try (ResultSet rs = selectRecByEmailIdStmt.executeQuery()) {
                selectRecByEmailIdStmt.clearParameters();

                List<RecipientOutDto> allRecipientList = new ArrayList<>();
                while (rs.next()) {
                    RecipientOutDto recOut = new RecipientOutDto();
                    recOut.setId(rs.getLong("id"));
                    recOut.setAddress_id(rs.getLong("address_id"));
                    recOut.setType(allRecTypes.get(rs.getLong("type_id")));
                    allRecipientList.add(recOut);
                }

                List<RecipientOutDto> recipientsTo = new ArrayList<>();
                List<RecipientOutDto> recipientsCC = new ArrayList<>();
                List<RecipientOutDto> recipientsBCC = new ArrayList<>();
                allRecipientList.forEach(e -> {
                    if (Recipient.RecipientType.TO.equals(e.getType())) {
                        recipientsTo.add(e);
                    } else if (Recipient.RecipientType.CC.equals(e.getType())) {
                        recipientsCC.add(e);
                    } else {
                        recipientsBCC.add(e);
                    }
                });
                emailOutDto.setRecipientsTo(recipientsTo);
                emailOutDto.setRecipientsCC(recipientsCC);
                emailOutDto.setRecipientsBCC(recipientsBCC);
            }
        }
    }

    private Long insertEmail(Email email, Connection connection) throws SQLException {
        Long emailId = null;
        try (PreparedStatement insrtEmail = connection.prepareStatement(INSERT_EMAIL_QUERY)) {
            insrtEmail.setString(1, email.getTitle());
            insrtEmail.setString(2, email.getText());
            try (ResultSet rs = insrtEmail.executeQuery()) {
                if (rs.next()) {
                    emailId = rs.getLong("id");
                }
            }
            if (emailId == null) {
                DBUtil.transactionRollback(connection);
                throw new IllegalStateException("Didn't receive the ID for the provided email!");
            }
            insrtEmail.clearParameters();
        }
        return emailId;
    }

    private Long getRecipientTypeId(String type, Connection connection) throws SQLException {
        Long recTypeId = null;
        try (PreparedStatement selectRecTypeStmt = connection.prepareStatement(SELECT_RECIPIENT_TYPE_QUERY);) {
            selectRecTypeStmt.setString(1, type);
            try (ResultSet rs = selectRecTypeStmt.executeQuery()) {
                if (rs.next()) {
                    recTypeId = rs.getLong("id");
                }
            }
            if (recTypeId == null) {
                DBUtil.transactionRollback(connection);
                throw new IllegalStateException("Didn't receive the ID for the provided recipient type: " + type);
            }
            selectRecTypeStmt.clearParameters();
        }
        return recTypeId;
    }

    private void insertRecipientsByType(Long emailId, String type, List<Recipient> recipientList,
                                        Connection connection) throws SQLException {
        if (recipientList.isEmpty()) {
            return;
        }
        Long recTypeId = getRecipientTypeId(type, connection);
        try (PreparedStatement insrtCrossEmAddrTypeStmt = connection.prepareStatement(
                INSERT_CROSS_EMAIL_RECIPIENTS_QUERY)) {
            for (Recipient recipient : recipientList) {
                insrtCrossEmAddrTypeStmt.setLong(1, emailId);
                insrtCrossEmAddrTypeStmt.setLong(2, recipient.getAddress().getId());
                insrtCrossEmAddrTypeStmt.setLong(3, recTypeId);
                insrtCrossEmAddrTypeStmt.addBatch();
            }
            insrtCrossEmAddrTypeStmt.executeBatch();

            insrtCrossEmAddrTypeStmt.clearParameters();
        }
    }
}
