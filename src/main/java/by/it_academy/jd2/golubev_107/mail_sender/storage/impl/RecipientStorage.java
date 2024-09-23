package by.it_academy.jd2.golubev_107.mail_sender.storage.impl;

import by.it_academy.jd2.golubev_107.mail_sender.storage.IRecipientStorage;
import by.it_academy.jd2.golubev_107.mail_sender.storage.connection.IConnectionManager;
import by.it_academy.jd2.golubev_107.mail_sender.storage.entity.Recipient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RecipientStorage implements IRecipientStorage {

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
