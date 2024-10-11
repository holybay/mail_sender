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
import java.util.UUID;

public class RecipientAddressStorage implements IRecipientAddressStorage {

    private static final String INSERT_ADDRESS_QUERY = "INSERT INTO app.recipient_address (id, email) VALUES(?,?)";
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

            try (PreparedStatement insrtRecStmt = connection.prepareStatement(INSERT_ADDRESS_QUERY)) {
                insrtRecStmt.setObject(1, address.getId());
                insrtRecStmt.setString(2, address.getEmailAddress());
                int insertedCount = insrtRecStmt.executeUpdate();
                if (insertedCount != 1) {
                    DBUtil.transactionRollback(connection);
                    throw new IllegalStateException("Didn't receive the ID for the provided recipient address!");
                }
                insrtRecStmt.clearParameters();
            }

            connection.commit();
            return readById(address.getId());
        } catch (SQLException e) {
            DBUtil.transactionRollback(connection);
            throw new RuntimeException("Failed to create a recipient address!" + e);
        } catch (RuntimeException e) {
            DBUtil.transactionRollback(connection);
            throw new RuntimeException("Failed to create a recipient address!" + address, e);
        } finally {
            DBUtil.connectionClose(connection);
        }
    }

    @Override
    public List<RecipientAddress> create(Collection<RecipientAddress> addressesToCreate) {
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            connection.setAutoCommit(false);
            DBUtil.transactionBegin(connection);

            List<UUID> idList = addressesToCreate.stream()
                                                 .map(RecipientAddress::getId)
                                                 .toList();

            try (PreparedStatement insrtRecStmt = connection.prepareStatement(INSERT_ADDRESS_QUERY)) {
                for (RecipientAddress address : addressesToCreate) {
                    insrtRecStmt.setObject(1, address.getId());
                    insrtRecStmt.setString(2, address.getEmailAddress());
                    insrtRecStmt.addBatch();
                }
                int[] rowsAffectedByBatch = insrtRecStmt.executeBatch();

                int rowsInserted = 0;
                for (int count : rowsAffectedByBatch) {
                    rowsInserted += count;
                }

                if (idList.size() != rowsInserted) {
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
        } catch (RuntimeException e) {
            DBUtil.transactionRollback(connection);
            throw new RuntimeException("Failed to create a recipient addresses!" + addressesToCreate, e);
        } finally {
            DBUtil.connectionClose(connection);
        }
    }

    @Override
    public RecipientAddress readById(UUID id) {
        try (Connection connection = connectionManager.getConnection();) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_READ_BY_ID)) {
                preparedStatement.setObject(1, id);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    preparedStatement.clearParameters();
                    if (rs.next()) {
                        return mapper(rs);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read a recipient address with id: " + id, e);
        }
        return null;
    }

    @Override
    public List<RecipientAddress> readAllByIds(Collection<UUID> idList) {
        try (Connection connection = connectionManager.getConnection();) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    DBUtil.setDynamicSelectSqlParams(SELECT_ALL_IN_LIST_QUERY, idList.size()))) {
                int paramCounter = 1;
                for (UUID id : idList) {
                    preparedStatement.setObject(paramCounter, id);
                    paramCounter++;
                }
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    List<RecipientAddress> addresses = new ArrayList<>();
                    while (rs.next()) {
                        addresses.add(mapper(rs));
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
                        return mapper(rs);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find a recipient address: " + emailAddress, e);
        }
        return null;
    }

    private RecipientAddress mapper(ResultSet rs) throws SQLException {
        RecipientAddress address = new RecipientAddress();
        address.setId(rs.getObject("id", UUID.class));
        address.setEmailAddress(rs.getString("email"));
        return address;
    }
}
