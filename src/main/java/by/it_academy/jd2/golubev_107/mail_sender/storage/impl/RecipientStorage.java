package by.it_academy.jd2.golubev_107.mail_sender.storage.impl;

import by.it_academy.jd2.golubev_107.mail_sender.platform.DBUtil;
import by.it_academy.jd2.golubev_107.mail_sender.storage.IRecipientStorage;
import by.it_academy.jd2.golubev_107.mail_sender.storage.connection.IConnectionManager;
import by.it_academy.jd2.golubev_107.mail_sender.storage.entity.Recipient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RecipientStorage implements IRecipientStorage {

    private static final String INSERT_RECIPIENT_QUERY = "INSERT INTO app.recipient (type_id, email) VALUES(?, ?) RETURNING id;";
    private static final String SELECT_REC_TYPE_ID_QUERY = "SELECT rt.id FROM app.recipient_type rt WHERE rt.type = ?";
    private static final String SELECT_READ_BY_ID = """
            SELECT r.id, r.email, rt.type
            FROM app.recipient r
            JOIN app.recipient_type rt ON r.type_id = rt.id
            WHERE r.id = ?;""";
    private final IConnectionManager connectionManager;

    public RecipientStorage(IConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Recipient create(Recipient recipient) {
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            connection.setAutoCommit(false);
            DBUtil.transactionBegin(connection);

            Long recTypeId = null;
            try (PreparedStatement selRecTypeIdStmt = connection.prepareStatement(SELECT_REC_TYPE_ID_QUERY)) {
                selRecTypeIdStmt.setString(1, recipient.getType().name());
                try (ResultSet rs = selRecTypeIdStmt.executeQuery();) {
                    if (rs.next()) {
                        recTypeId = rs.getLong("id");
                    }
                }
                if (recTypeId == null) {
                    DBUtil.transactionRollback(connection);
                    throw new IllegalStateException("Didn't receive the ID for the provided recipient type!");
                }
                selRecTypeIdStmt.clearParameters();
            }

            Long recipientId = null;
            try (PreparedStatement insrtRecptStmt = connection.prepareStatement(INSERT_RECIPIENT_QUERY)) {
                insrtRecptStmt.setLong(1, recTypeId);
                insrtRecptStmt.setString(2, recipient.getEmailAddress());
                try (ResultSet rs = insrtRecptStmt.executeQuery();) {
                    if (rs.next()) {
                        recipientId = rs.getLong("id");
                    }
                }
                if (recipientId == null) {
                    DBUtil.transactionRollback(connection);
                    throw new IllegalStateException("Didn't receive the ID for the saved recipient!" + recipient);
                }
                insrtRecptStmt.clearParameters();
            }
            connection.commit();
            return readById(recipientId);
        } catch (SQLException e) {
            DBUtil.transactionRollback(connection);
            throw new RuntimeException("Failed to create a recipient!" + e);
        } finally {
            DBUtil.connectionClose(connection);
        }
    }

    @Override
    public Recipient readById(Long id) {
        try (Connection connection = connectionManager.getConnection();) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_READ_BY_ID)) {
                preparedStatement.setLong(1, id);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        return Recipient.builder()
                                        .setId(rs.getLong("id"))
                                        .setEmailAddress(rs.getString("email"))
                                        .setType(Recipient.RecipientType.valueOf(rs.getString("type")))
                                        .build();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read a recipient with id: " + id);
        }
        return null;
    }
}
