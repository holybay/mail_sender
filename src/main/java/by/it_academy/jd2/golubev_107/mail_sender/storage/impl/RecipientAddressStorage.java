package by.it_academy.jd2.golubev_107.mail_sender.storage.impl;

import by.it_academy.jd2.golubev_107.mail_sender.platform.DBUtil;
import by.it_academy.jd2.golubev_107.mail_sender.storage.IRecipientAddressStorage;
import by.it_academy.jd2.golubev_107.mail_sender.storage.connection.IConnectionManager;
import by.it_academy.jd2.golubev_107.mail_sender.storage.entity.RecipientAddress;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecipientAddressStorage implements IRecipientAddressStorage {

    private static final String INSERT_ADDRESS_QUERY = "INSERT INTO app.recipient_address (email) VALUES(?) RETURNING id;";
    private static final String SELECT_READ_BY_ID = "SELECT id,email FROM app.recipient_address WHERE id = ?;";
    private static final String SELECT_ALL_IN_LIST_QUERY = "SELECT id, email FROM app.recipient_address WHERE id IN (?);";
    private static final String SELECT_READ_BY_EMAIL = "SELECT id,email FROM app.recipient_address WHERE email = ?;";
    private final IConnectionManager connectionManager;

    public RecipientAddressStorage(IConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public RecipientAddress create(RecipientAddress address) {
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            connection.setAutoCommit(false);
            DBUtil.transactionBegin(connection);

            Long recId = null;
            try (PreparedStatement insrtRecStmt = connection.prepareStatement(INSERT_ADDRESS_QUERY)) {
                insrtRecStmt.setString(1, address.getEmailAddress());
                try (ResultSet rs = insrtRecStmt.executeQuery();) {
                    if (rs.next()) {
                        recId = rs.getLong("id");
                    }
                }
                if (recId == null) {
                    DBUtil.transactionRollback(connection);
                    throw new IllegalStateException("Didn't receive the ID for the provided recipient address!");
                }
                insrtRecStmt.clearParameters();
            }

            connection.commit();
            return readById(recId);
        } catch (SQLException e) {
            DBUtil.transactionRollback(connection);
            throw new RuntimeException("Failed to create a recipient address!" + e);
        } finally {
            DBUtil.connectionClose(connection);
        }
    }

    @Override
    public List<RecipientAddress> create(Collection<RecipientAddress> addresses) {
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            connection.setAutoCommit(false);
            DBUtil.transactionBegin(connection);

            List<Long> idList = new ArrayList<>();
            try (PreparedStatement insrtRecStmt = connection.prepareStatement(
                    DBUtil.setMultipleRowsToInsert(INSERT_ADDRESS_QUERY, addresses.size()))) {
                int paramCounter = 1;
                for (RecipientAddress address : addresses) {
                    insrtRecStmt.setString(paramCounter, address.getEmailAddress());
                    paramCounter++;
                }
                try (ResultSet rs = insrtRecStmt.executeQuery()) {
                    while (rs.next()) {
                        idList.add(rs.getLong("id"));
                    }
                }
                if (addresses.size() != idList.size()) {
                    DBUtil.transactionRollback(connection);
                    throw new IllegalStateException("Didn't receive all the IDs for the provided recipient type!");
                }
                insrtRecStmt.clearParameters();
            }

            connection.commit();
            return readAllByIds(idList);
        } catch (SQLException e) {
            DBUtil.transactionRollback(connection);
            throw new RuntimeException("Failed to create a recipient!" + e);
        } finally {
            DBUtil.connectionClose(connection);
        }
    }

    @Override
    public RecipientAddress readById(Long id) {
        try (Connection connection = connectionManager.getConnection();) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_READ_BY_ID)) {
                preparedStatement.setLong(1, id);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    preparedStatement.clearParameters();
                    if (rs.next()) {
                        RecipientAddress address = new RecipientAddress();
                        address.setId(rs.getLong("id"));
                        address.setEmailAddress(rs.getString("email"));
                        return address;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read a recipient address with id: " + id, e);
        }
        return null;
    }

    @Override
    public List<RecipientAddress> readAllByIds(Collection<Long> idList) {
        try (Connection connection = connectionManager.getConnection();) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    DBUtil.setDynamicSelectSqlParams(SELECT_ALL_IN_LIST_QUERY, idList.size()))) {
                int paramCounter = 1;
                for (Long id : idList) {
                    preparedStatement.setLong(paramCounter, id);
                    paramCounter++;
                }
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    List<RecipientAddress> addresses = new ArrayList<>();
                    while (rs.next()) {
                        RecipientAddress address = new RecipientAddress();
                        address.setId(rs.getLong("id"));
                        address.setEmailAddress(rs.getString("email"));
                        addresses.add(address);
                    }
                    preparedStatement.clearParameters();
                    return addresses;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute a query!", e);
        }
    }

    @Override
    public RecipientAddress readByEmail(String emailAddress) {
        try (Connection connection = connectionManager.getConnection();) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_READ_BY_EMAIL)) {
                preparedStatement.setString(1, emailAddress);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    preparedStatement.clearParameters();
                    if (rs.next()) {
                        RecipientAddress address = new RecipientAddress();
                        address.setId(rs.getLong("id"));
                        address.setEmailAddress(rs.getString("email"));
                        return address;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find a recipient address: " + emailAddress, e);
        }
        return null;
    }
}
