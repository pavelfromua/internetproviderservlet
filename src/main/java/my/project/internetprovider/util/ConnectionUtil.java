package my.project.internetprovider.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUtil {
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Can't find MySQL Driver", e);
        }
    }

    public static Connection getConnection() {
        Properties connectionProperties = new Properties();
        connectionProperties.put("user", "root");
        connectionProperties.put("password", "root");

        String url = "jdbc:mysql://localhost:3306/iptest?serverTimezone=UTC";
        try {
            Connection connection = DriverManager.getConnection(url, connectionProperties);
            connection.setAutoCommit(false);

            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("Can't establish the connection to DB", e);
        }
    }

    public static Connection getConnection(String url, String user, String password) {
        Properties connectionProperties = new Properties();
        connectionProperties.put("user", user);
        connectionProperties.put("password", password);

        try {
            Connection connection = DriverManager.getConnection(url, connectionProperties);
            connection.setAutoCommit(false);

            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("Can't establish the connection to DB", e);
        }
    }
}
