package util;

import java.sql.Connection;
import java.sql.SQLException;

public class TestDBConnection {
    public static void main(String[] args) {
        String configuredUrl = System.getProperty("DB_URL");
        if (configuredUrl == null || configuredUrl.isEmpty()) {
            configuredUrl = System.getenv("DB_URL");
        }
        System.out.println("Sử dụng DB_URL: " + (configuredUrl != null ? configuredUrl : "<giá trị mặc định trong JDBCConnection>") );

        try (Connection conn = JDBCConnection.getConnection()) {
            System.out.println("Kết nối thành công: " + (conn != null && !conn.isClosed()));
        } catch (SQLException e) {
            System.err.println("Không thể kết nối tới cơ sở dữ liệu: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(2);
        }
    }
}
