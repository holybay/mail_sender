package by.it_academy.jd2.golubev_107.mail_sender.storage.impl;

import by.it_academy.jd2.golubev_107.mail_sender.platform.DBUtil;
import by.it_academy.jd2.golubev_107.mail_sender.storage.IMailStorage;
import by.it_academy.jd2.golubev_107.mail_sender.storage.connection.IConnectionManager;
import by.it_academy.jd2.golubev_107.mail_sender.storage.dto.EmailStorageOutDto;
import by.it_academy.jd2.golubev_107.mail_sender.storage.dto.RecipientOutDto;
import by.it_academy.jd2.golubev_107.mail_sender.storage.entity.Email;
import by.it_academy.jd2.golubev_107.mail_sender.storage.entity.EmailStatus;
import by.it_academy.jd2.golubev_107.mail_sender.storage.entity.Recipient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MailStorage implements IMailStorage {

    private static final String INSERT_EMAIL_QUERY =
            "INSERT INTO app.email (id, title, body_text, status_id, created_at, updated_at) VALUES(?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_EMAIL_QUERY = """
            UPDATE app.email
            SET title = ?, body_text = ?, status_id = ?, updated_at = ? WHERE id = ?;""";
    private static final String SELECT_EMAIL_QUERY =
            "SELECT id, title, body_text, status_id, created_at, updated_at FROM app.email WHERE id = ?;";
    private static final String SELECT_ALL_EMAIL_QUERY =
            "SELECT id, title, body_text, status_id, created_at, updated_at FROM app.email;";
    private static final String INSERT_RECIPIENTS_QUERY = """
            INSERT INTO app.email_recipients (id, email_id, address_id, type_id)
            VALUES (?, ?, ?, ?);""";
    private static final String UPDATE_RECIPIENTS_QUERY = """
            UPDATE app.email_recipients
            SET email_id = ?, address_id = ?, type_id = ? WHERE id = ?;""";
    private static final String SELECT_RECIPIENT_TYPE_QUERY = "SELECT id FROM app.recipient_type WHERE type = ?;";
    private static final String SELECT_EMAIL_STATUS_BY_NAME_QUERY = "SELECT id, name FROM app.email_status WHERE name = ?;";
    private static final String SELECT_EMAIL_STATUS_BY_ID_QUERY = "SELECT id, name FROM app.email_status WHERE id = ?;";
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

            EmailStatus emailStatus = getEmailStatus(email.getEmailStatus().getStatus().name(), connection,
                    SELECT_EMAIL_STATUS_BY_NAME_QUERY);

            insertEmail(email, emailStatus.getId(), connection);
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
        try (Connection connection = connectionManager.getConnection()) {
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

    @Override
    public void updateStatus(UUID id, EmailStatus.EStatus newStatus) {
        EmailStorageOutDto toUpdate = readById(id);
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            connection.setAutoCommit(false);
            DBUtil.transactionBegin(connection);

            EmailStatus emailStatus = getEmailStatus(newStatus.name(), connection,
                    SELECT_EMAIL_STATUS_BY_NAME_QUERY);

            updateEmail(toUpdate, emailStatus.getId(), connection);
            UUID emailId = toUpdate.getId();
            updateRecipientsByType(emailId, Recipient.RecipientType.TO.name(), toUpdate.getRecipientsTo(), connection);
            updateRecipientsByType(emailId, Recipient.RecipientType.CC.name(), toUpdate.getRecipientsCC(), connection);
            updateRecipientsByType(emailId, Recipient.RecipientType.BCC.name(), toUpdate.getRecipientsBCC(), connection);

            connection.commit();
        } catch (SQLException | RuntimeException e) {
            DBUtil.transactionRollback(connection);
            throw new RuntimeException("Failed to create an email with id: " + id, e);
        } finally {
            DBUtil.connectionClose(connection);
        }
    }

    private EmailStorageOutDto selectEmailPart(UUID receivedId, Connection connection) throws SQLException {
        try (PreparedStatement selectEmailStmt = connection.prepareStatement(SELECT_EMAIL_QUERY)) {
            selectEmailStmt.setObject(1, receivedId);
            try (ResultSet rs = selectEmailStmt.executeQuery()) {
                selectEmailStmt.clearParameters();
                if (rs.next()) {
                    Long statusId = rs.getLong("status_id");
                    EmailStatus status = getEmailStatus(statusId, connection,
                            SELECT_EMAIL_STATUS_BY_ID_QUERY);
                    return mapToEmailOutDto(rs, status);
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
                    Long statusId = rs.getLong("status_id");
                    EmailStatus status = getEmailStatus(statusId, connection,
                            SELECT_EMAIL_STATUS_BY_ID_QUERY);
                    allEmails.add(mapToEmailOutDto(rs, status));
                }
                return allEmails;
            }
        }
    }

    private <T> EmailStatus getEmailStatus(T paramToFindBy, Connection connection, String sqlQuery) throws SQLException {
        try (PreparedStatement selectEStatusStmt = connection.prepareStatement(sqlQuery)) {
            selectEStatusStmt.setObject(1, paramToFindBy);
            try (ResultSet rs = selectEStatusStmt.executeQuery()) {
                selectEStatusStmt.clearParameters();
                if (rs.next()) {
                    EmailStatus status = new EmailStatus();
                    status.setId(rs.getLong("id"));
                    status.setStatus(EmailStatus.EStatus.valueOf(rs.getString("name")));
                    return status;
                }
                DBUtil.transactionRollback(connection);
                throw new IllegalStateException("Can't get the email status by param: " + paramToFindBy);
            }
        }
    }

    private EmailStorageOutDto mapToEmailOutDto(ResultSet rs, EmailStatus status) throws SQLException {
        return EmailStorageOutDto.builder()
                                 .setId(rs.getObject("id", UUID.class))
                                 .setTitle(rs.getString("title"))
                                 .setText(rs.getString("body_text"))
                                 .setEmailStatus(status)
                                 .setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                                 .setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime())
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

    private void insertEmail(Email email, Long statusId, Connection connection) throws SQLException {
        try (PreparedStatement insrtEmail = connection.prepareStatement(INSERT_EMAIL_QUERY)) {
            insrtEmail.setObject(1, email.getId());
            insrtEmail.setString(2, email.getTitle());
            insrtEmail.setString(3, email.getText());
            insrtEmail.setLong(4, statusId);
            insrtEmail.setTimestamp(5, Timestamp.valueOf(email.getCreatedAt()));
            insrtEmail.setTimestamp(6, Timestamp.valueOf(email.getUpdatedAt()));
            int rowsInserted = insrtEmail.executeUpdate();
            insrtEmail.clearParameters();

            if (rowsInserted != 1) {
                DBUtil.transactionRollback(connection);
                throw new IllegalStateException("Inserted more than one row!");
            }
        }
    }

    private void updateEmail(EmailStorageOutDto emailStorageOutDto, Long statusId, Connection connection) throws SQLException {
        try (PreparedStatement updateEmail = connection.prepareStatement(UPDATE_EMAIL_QUERY)) {
            updateEmail.setString(1, emailStorageOutDto.getTitle());
            updateEmail.setString(2, emailStorageOutDto.getText());
            updateEmail.setLong(3, statusId);
            updateEmail.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            updateEmail.setObject(5, emailStorageOutDto.getId());
            int rowsUpdated = updateEmail.executeUpdate();
            updateEmail.clearParameters();

            if (rowsUpdated != 1) {
                DBUtil.transactionRollback(connection);
                throw new IllegalStateException("Updated more than one row!");
            }
        }
    }

    private Long getRecipientTypeId(String type, Connection connection) throws SQLException {
        Long recTypeId = null;
        try (PreparedStatement selectRecTypeStmt = connection.prepareStatement(SELECT_RECIPIENT_TYPE_QUERY)) {
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

    private void updateRecipientsByType(UUID emailId, String type, List<RecipientOutDto> recipientList,
                                        Connection connection) throws SQLException {
        if (recipientList.isEmpty()) {
            return;
        }
        Long recTypeId = getRecipientTypeId(type, connection);
        try (PreparedStatement updateRecipientsStmt = connection.prepareStatement(
                UPDATE_RECIPIENTS_QUERY)) {
            for (RecipientOutDto recipientDto : recipientList) {
                updateRecipientsStmt.setObject(1, emailId);
                updateRecipientsStmt.setObject(2, recipientDto.getAddressId());
                updateRecipientsStmt.setLong(3, recTypeId);
                updateRecipientsStmt.setObject(4, recipientDto.getId());
                updateRecipientsStmt.addBatch();
            }
            updateRecipientsStmt.executeBatch();

            updateRecipientsStmt.clearParameters();
        }
    }
}
