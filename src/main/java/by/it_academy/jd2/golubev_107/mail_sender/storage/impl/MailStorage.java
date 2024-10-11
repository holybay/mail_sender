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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MailStorage implements IMailStorage {

    private static final String INSERT_EMAIL_QUERY = "INSERT INTO app.email (id, title, body_text) VALUES(?, ?, ?)";
    private static final String SELECT_EMAIL_QUERY = "SELECT id, title, body_text FROM app.email WHERE id = ?;";
    private static final String SELECT_ALL_EMAIL_QUERY = "SELECT id, title, body_text FROM app.email;";
    private static final String INSERT_RECIPIENTS_QUERY = """
            INSERT INTO app.email_recipients (id, email_id, address_id, type_id)
            VALUES (?, ?, ?, ?);""";
    private static final String SELECT_RECIPIENT_TYPE_QUERY = "SELECT id FROM app.recipient_type WHERE type = ?;";
    private static final String SELECT_ALL_RECIPIENT_TYPE_QUERY = "SELECT id, type FROM app.recipient_type;";
    private static final String SELECT_RECIPIENTS_BY_EMAIL_ID_QUERY = """
            SELECT id, email_id, address_id, type_id
            FROM app.email_recipients
            WHERE email_id = ?;""";
    private static final String SELECT_ALL_RECIPIENTS_QUERY = """
            SELECT id, email_id, address_id, type_id
            FROM app.email_recipients
            ORDER BY email_id;""";
    private final IConnectionManager connectionManager;

    public MailStorage(IConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public void create(Email email) {
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            connection.setAutoCommit(false);
            DBUtil.transactionBegin(connection);

            insertEmail(email, connection);
            UUID emailId = email.getId();
            insertRecipientsByType(emailId, Recipient.RecipientType.TO.name(), email.getRecipientsTo(), connection);
            insertRecipientsByType(emailId, Recipient.RecipientType.CC.name(), email.getRecipientsCC(), connection);
            insertRecipientsByType(emailId, Recipient.RecipientType.BCC.name(), email.getRecipientsBCC(), connection);

            connection.commit();
        } catch (SQLException | RuntimeException e) {
            DBUtil.transactionRollback(connection);
            throw new RuntimeException("Failed to create an email: " + email, e);
        } finally {
            DBUtil.connectionClose(connection);
        }
    }

    @Override
    public EmailStorageOutDto readById(UUID id) {
        try (Connection connection = connectionManager.getConnection()) {
            EmailStorageOutDto emailOutDto = selectEmailPart(id, connection);
            if (emailOutDto != null) {
                Map<Long, Recipient.RecipientType> allRecTypes = selectAllRecipientTypes(connection);
                Map<UUID, List<RecipientOutDto>> allRecptsByEmailId = selectRecipientsPart(emailOutDto,
                        allRecTypes, connection);
                setRecipientsToEmail(emailOutDto, allRecptsByEmailId.get(emailOutDto.getId()));
                return emailOutDto;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read an email by id: " + id, e);
        }
        return null;
    }

    @Override
    public List<EmailStorageOutDto> readAll() {
        try (Connection connection = connectionManager.getConnection();) {
            List<EmailStorageOutDto> allEmails = selectAllEmails(connection);
            if (!allEmails.isEmpty()) {
                Map<Long, Recipient.RecipientType> allRecTypes = selectAllRecipientTypes(connection);
                Map<UUID, List<RecipientOutDto>> allRecptsByEmailId = selectAllRecipients(allRecTypes, connection);
                allEmails.forEach(e -> setRecipientsToEmail(e, allRecptsByEmailId.get(e.getId())));
                return allEmails;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read all emails", e);
        }
        return Collections.emptyList();
    }

    private EmailStorageOutDto selectEmailPart(UUID receivedId, Connection connection) throws SQLException {
        try (PreparedStatement selectEmailStmt = connection.prepareStatement(SELECT_EMAIL_QUERY)) {
            selectEmailStmt.setObject(1, receivedId);
            try (ResultSet rs = selectEmailStmt.executeQuery()) {
                selectEmailStmt.clearParameters();
                if (rs.next()) {
                    return mapToEmailOutDto(rs);
                }
            }
        }
        return null;
    }

    private List<EmailStorageOutDto> selectAllEmails(Connection connection) throws SQLException {
        try (PreparedStatement selectEmailStmt = connection.prepareStatement(SELECT_ALL_EMAIL_QUERY)) {
            try (ResultSet rs = selectEmailStmt.executeQuery()) {
                selectEmailStmt.clearParameters();
                List<EmailStorageOutDto> allEmails = new ArrayList<>();
                while (rs.next()) {
                    allEmails.add(mapToEmailOutDto(rs));
                }
                return allEmails;
            }
        }
    }

    private EmailStorageOutDto mapToEmailOutDto(ResultSet rs) throws SQLException {
        return EmailStorageOutDto.builder()
                                 .setId(rs.getObject("id", UUID.class))
                                 .setTitle(rs.getString("title"))
                                 .setText(rs.getString("body_text"))
                                 .build();
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

    private Map<UUID, List<RecipientOutDto>> selectRecipientsPart(EmailStorageOutDto emailOutDto,
                                                                  Map<Long, Recipient.RecipientType> allRecTypes,
                                                                  Connection connection) throws SQLException {
        try (PreparedStatement selectRecByEmailIdStmt = connection.prepareStatement(
                SELECT_RECIPIENTS_BY_EMAIL_ID_QUERY)) {
            selectRecByEmailIdStmt.setObject(1, emailOutDto.getId());
            try (ResultSet rs = selectRecByEmailIdStmt.executeQuery()) {
                selectRecByEmailIdStmt.clearParameters();
                return getEmailRecptsFromRS(rs, allRecTypes);
            }
        }
    }

    private Map<UUID, List<RecipientOutDto>> selectAllRecipients(Map<Long, Recipient.RecipientType> allRecTypes,
                                                                 Connection connection) throws SQLException {
        try (PreparedStatement selectAllEmailsRecptsStmt = connection.prepareStatement(
                SELECT_ALL_RECIPIENTS_QUERY)) {
            try (ResultSet rs = selectAllEmailsRecptsStmt.executeQuery()) {
                selectAllEmailsRecptsStmt.clearParameters();
                return getEmailRecptsFromRS(rs, allRecTypes);
            }
        }
    }

    private Map<UUID, List<RecipientOutDto>> getEmailRecptsFromRS(ResultSet rs,
                                                                  Map<Long, Recipient.RecipientType> allRecTypes)
            throws SQLException {

        Map<UUID, List<RecipientOutDto>> allRecptsByEmail = new HashMap<>();
        UUID iterableId = null;
        while (rs.next()) {
            UUID emailIdToCheck = rs.getObject("email_id", UUID.class);
            if (iterableId == null) {
                iterableId = emailIdToCheck;
                allRecptsByEmail.put(iterableId, new ArrayList<>());
            }
            if (!iterableId.equals(emailIdToCheck)) {
                iterableId = emailIdToCheck;
                allRecptsByEmail.put(iterableId, new ArrayList<>());
            }
            RecipientOutDto recOut = new RecipientOutDto();
            recOut.setId(rs.getObject("id", UUID.class));
            recOut.setAddressId(rs.getObject("address_id", UUID.class));
            recOut.setType(allRecTypes.get(rs.getLong("type_id")));
            allRecptsByEmail.get(iterableId).add(recOut);
        }
        return allRecptsByEmail;
    }

    private void setRecipientsToEmail(EmailStorageOutDto emailOutDto, List<RecipientOutDto> emailRecptList) {
        List<RecipientOutDto> recipientsTo = new ArrayList<>();
        List<RecipientOutDto> recipientsCC = new ArrayList<>();
        List<RecipientOutDto> recipientsBCC = new ArrayList<>();
        emailRecptList.forEach(e -> {
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

    private void insertEmail(Email email, Connection connection) throws SQLException {
        try (PreparedStatement insrtEmail = connection.prepareStatement(INSERT_EMAIL_QUERY)) {
            insrtEmail.setObject(1, email.getId());
            insrtEmail.setString(2, email.getTitle());
            insrtEmail.setString(3, email.getText());
            int rowsInserted = insrtEmail.executeUpdate();

            if (rowsInserted != 1) {
                DBUtil.transactionRollback(connection);
                throw new IllegalStateException("Inserted more than one row!");
            }
            insrtEmail.clearParameters();
        }
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

    private void insertRecipientsByType(UUID emailId, String type, List<Recipient> recipientList,
                                        Connection connection) throws SQLException {
        if (recipientList.isEmpty()) {
            return;
        }
        Long recTypeId = getRecipientTypeId(type, connection);
        try (PreparedStatement insrtRecipientsStmt = connection.prepareStatement(
                INSERT_RECIPIENTS_QUERY)) {
            for (Recipient recipient : recipientList) {
                insrtRecipientsStmt.setObject(1, recipient.getId());
                insrtRecipientsStmt.setObject(2, emailId);
                insrtRecipientsStmt.setObject(3, recipient.getAddress().getId());
                insrtRecipientsStmt.setLong(4, recTypeId);
                insrtRecipientsStmt.addBatch();
            }
            insrtRecipientsStmt.executeBatch();

            insrtRecipientsStmt.clearParameters();
        }
    }
}
