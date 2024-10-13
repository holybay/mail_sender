package by.it_academy.jd2.golubev_107.mail_sender.storage.connection.impl;

import by.it_academy.jd2.golubev_107.mail_sender.storage.connection.IConnectionManager;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManagerImpl implements IConnectionManager, AutoCloseable {

    private static final String JDBC_DRIVER = "app.db.driver";
    private static final String DB_URL = "app.db.url";
    private static final String DB_USER = "app.db.user";
    private static final String DB_PASSWORD = "app.db.password";
    private final ComboPooledDataSource cpds;

    public ConnectionManagerImpl(Properties config) {
        cpds = new ComboPooledDataSource();
        try {
            cpds.setDriverClass(config.getProperty(JDBC_DRIVER));
            cpds.setJdbcUrl(config.getProperty(DB_URL));
            cpds.setUser(config.getProperty(DB_USER));
            cpds.setPassword(config.getProperty(DB_PASSWORD));
        } catch (PropertyVetoException e) {
            throw new RuntimeException("The provided connection pool config isn't correct!");
        }

    }

    @Override
    public Connection getConnection() throws SQLException {
        return cpds.getConnection();
    }

    @Override
    public void close() throws Exception {
        if (cpds != null) {
            cpds.close();
        }
    }
}
