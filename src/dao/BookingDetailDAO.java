package dao;

import model.BookingDetail;
import util.JDBCConnection;
import java.sql.*;
import java.util.List;

public class BookingDetailDAO {
    public boolean addBookingDetails(int bookingId, List<BookingDetail> details) {
        if (details == null || details.isEmpty()) return true;
        String sql = "INSERT INTO booking_details (booking_id, type, item_id, quantity) VALUES (?, ?, ?, ?)";
        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (BookingDetail bd : details) {
                stmt.setInt(1, bookingId);
                stmt.setString(2, bd.getType());
                stmt.setInt(3, bd.getItemId());
                stmt.setInt(4, bd.getQuantity());
                stmt.addBatch();
            }
            int[] results = stmt.executeBatch();
            return results.length == details.size();
        } catch (SQLException e) {
            System.err.println("Lỗi lưu chi tiết booking: " + e.getMessage());
        }
        return false;
    }
}
