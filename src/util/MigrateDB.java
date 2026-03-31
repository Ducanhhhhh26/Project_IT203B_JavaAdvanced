package util;

import java.sql.Connection;
import java.sql.Statement;

public class MigrateDB {
    public static void main(String[] args) {
        try (Connection conn = JDBCConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            try {
                stmt.executeUpdate("ALTER TABLE rooms ADD COLUMN price_per_hour DOUBLE DEFAULT 100000");
                System.out.println("Added price_per_hour to rooms");
            } catch (Exception e) {}

            try {
                stmt.executeUpdate("ALTER TABLE equipments ADD COLUMN rental_price DOUBLE DEFAULT 50000");
                System.out.println("Added rental_price to equipments");
            } catch (Exception e) {}

            try {
                stmt.executeUpdate("ALTER TABLE bookings ADD COLUMN total_cost DOUBLE DEFAULT 0");
                System.out.println("Added total_cost to bookings");
            } catch (Exception e) {}

            try {
                stmt.executeUpdate("ALTER TABLE bookings ADD COLUMN rating INT DEFAULT 0");
                System.out.println("Added rating to bookings");
            } catch (Exception e) {}

            try {
                stmt.executeUpdate("ALTER TABLE bookings ADD COLUMN feedback TEXT");
                System.out.println("Added feedback to bookings");
            } catch (Exception e) {}

            System.out.println("Migration complete!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
