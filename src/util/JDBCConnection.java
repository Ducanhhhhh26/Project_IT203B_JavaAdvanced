package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnection {
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DB_NAME = "meeting_room_mgmt";
    private static final String USER = "root";
    private static final String PASS = "123456";

    public static Connection getConnection() throws SQLException {
        String url = String.format("jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=UTF-8&allowPublicKeyRetrieval=true&useSSL=false", HOST, PORT, DB_NAME);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, USER, PASS);
        } catch (ClassNotFoundException e) {
            System.err.println("Database Driver không tìm thấy: " + e.getMessage());
            return null;
        }
    }
}
