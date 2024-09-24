package by.it_academy.jd2.golubev_107.mail_sender.platform;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;

public class DBUtil {

    public static final int MIN_PARAMS_COUNT = 1;

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

    public static String setDynamicSelectSqlParams(String sql, final int paramsCount) {
        if (paramsCount < MIN_PARAMS_COUNT) {
            throw new IllegalArgumentException("SQL params amount can't be negative or equal 0");
        }
        if (paramsCount > MIN_PARAMS_COUNT) {
            final StringBuilder sb = new StringBuilder(
                    String.join(", ", Collections.nCopies(paramsCount, "?")));
            sql = sql.replace("(?)", "(" + sb + ")");
        }
        return sql;
    }

}
