package dao;

import model.Booking;
import util.JDBCConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {
    // Check if the room is available in the requested time frame
    // (return true if NO overlapping APPROVED or PENDING booking exists)
    public boolean checkRoomAvailable(int roomId, Timestamp start, Timestamp end) {
        String sql = "SELECT COUNT(*) FROM bookings WHERE room_id = ? "
                   + "AND status IN ('PENDING', 'APPROVED') "
                   + "AND ((start_time < ? AND end_time > ?) "
                   + "OR (start_time >= ? AND start_time < ?))";
        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomId);
            stmt.setTimestamp(2, end);
            stmt.setTimestamp(3, start);
            stmt.setTimestamp(4, start);
            stmt.setTimestamp(5, end);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi kiểm tra tính khả dụng phòng: " + e.getMessage());
        }
        return false;
    }

    public int createBooking(Booking booking) {
        String sql = "INSERT INTO bookings (user_id, room_id, start_time, end_time, status, prep_status, total_cost) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, booking.getUserId());
            stmt.setInt(2, booking.getRoomId());
            stmt.setTimestamp(3, booking.getStartTime());
            stmt.setTimestamp(4, booking.getEndTime());
            stmt.setString(5, booking.getStatus());
            stmt.setString(6, booking.getPrepStatus());
            stmt.setDouble(7, booking.getTotalCost());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tạo booking: " + e.getMessage());
        }
        return -1;
    }
}
