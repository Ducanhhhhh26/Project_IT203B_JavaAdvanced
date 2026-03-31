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

    public List<Booking> getBookingsByStatus(String status) {
        String sql = "SELECT * FROM bookings WHERE status = ?";
        List<Booking> list = new ArrayList<>();
        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while(rs.next()){
                    list.add(mapRowToBooking(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Booking> getBookingsBySupportStaffId(int staffId) {
        String sql = "SELECT * FROM bookings WHERE support_staff_id = ?";
        List<Booking> list = new ArrayList<>();
        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, staffId);
            try (ResultSet rs = stmt.executeQuery()) {
                while(rs.next()){
                    list.add(mapRowToBooking(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Booking> getBookingsByUserId(int userId) {
        String sql = "SELECT * FROM bookings WHERE user_id = ?";
        List<Booking> list = new ArrayList<>();
        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while(rs.next()){
                    list.add(mapRowToBooking(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateBookingStatus(int id, String status) {
        String sql = "UPDATE bookings SET status = ? WHERE id = ?";
        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateBookingSupportStaff(int id, int staffId) {
        String sql = "UPDATE bookings SET support_staff_id = ? WHERE id = ?";
        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, staffId);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateBookingPrepStatus(int id, String prepStatus) {
        String sql = "UPDATE bookings SET prep_status = ? WHERE id = ?";
        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, prepStatus);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateFeedback(int id, int rating, String feedback) {
        String sql = "UPDATE bookings SET rating = ?, feedback = ? WHERE id = ?";
        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, rating);
            stmt.setString(2, feedback);
            stmt.setInt(3, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void printRevenueByMonth() {
        String sql = "SELECT YEAR(start_time) as year, MONTH(start_time) as month, SUM(total_cost) as revenue " +
                     "FROM bookings WHERE status = 'APPROVED' " +
                     "GROUP BY YEAR(start_time), MONTH(start_time) ORDER BY year DESC, month DESC";
        try (Connection conn = JDBCConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("=== DOANH THU THEO THÁNG ===");
            System.out.printf("%-10s | %-10s | %-15s\n", "Năm", "Tháng", "Doanh thu (VND)");
            System.out.println("----------------------------------------");
            while (rs.next()) {
                System.out.printf("%-10d | %-10d | %-15.2f\n", rs.getInt("year"), rs.getInt("month"), rs.getDouble("revenue"));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi thống kê doanh thu: " + e.getMessage());
        }
    }

    public void printMostBookedRooms() {
        String sql = "SELECT r.name, COUNT(b.id) as booking_count " +
                     "FROM bookings b JOIN rooms r ON b.room_id = r.id " +
                     "WHERE b.status = 'APPROVED' " +
                     "GROUP BY r.id, r.name ORDER BY booking_count DESC LIMIT 5";
        try (Connection conn = JDBCConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("=== TOP 5 PHÒNG ĐƯỢC ĐẶT NHIỀU NHẤT ===");
            System.out.printf("%-20s | %-15s\n", "Tên phòng", "Số lần đặt");
            System.out.println("----------------------------------------");
            while (rs.next()) {
                System.out.printf("%-20s | %-15d\n", rs.getString("name"), rs.getInt("booking_count"));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi thống kê phòng chờ: " + e.getMessage());
        }
    }

    private Booking mapRowToBooking(ResultSet rs) throws SQLException {
        Booking b = new Booking();
        b.setId(rs.getInt("id"));
        b.setUserId(rs.getInt("user_id"));
        b.setRoomId(rs.getInt("room_id"));

        int staffId = rs.getInt("support_staff_id");
        if (rs.wasNull()) {
            b.setSupportStaffId(null);
        } else {
            b.setSupportStaffId(staffId);
        }

        b.setStartTime(rs.getTimestamp("start_time"));
        b.setEndTime(rs.getTimestamp("end_time"));
        b.setStatus(rs.getString("status"));
        b.setPrepStatus(rs.getString("prep_status"));
        b.setTotalCost(rs.getDouble("total_cost"));

        // Handling rating and feedback if they exist in schema
        try {
            b.setRating(rs.getInt("rating"));
            b.setFeedback(rs.getString("feedback"));
        } catch (SQLException e) {
            // Columns might not exist yet if migration wasn't run
        }

        b.setCreatedAt(rs.getTimestamp("created_at"));
        return b;
    }
}
