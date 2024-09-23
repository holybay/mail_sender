package by.it_academy.jd2.golubev_107.mail_sender.platform;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {

    public static void transactionBegin(Connection connection) throws SQLException {
        Statement trBegin = connection.createStatement();
        trBegin.execute("BEGIN;");
    }

    public static void transactionRollback(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
                System.out.println("Error - transaction rollback!");
            } catch (SQLException e) {
                throw new RuntimeException("Failed to close a connection" + e);
            }
        }
    }

    public static void connectionClose(Connection connection) {
        if (connection != null) {
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to close a connection" + e);
            }
        }
    }

}
