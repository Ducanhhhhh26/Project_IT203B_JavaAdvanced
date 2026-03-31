package service;

import dao.UserDAO;
import dao.BookingDAO;
import model.User;
import model.Booking;
import model.Role;
import util.Logger;

import java.util.List;

public class AdminService {
    private UserDAO userDAO = new UserDAO();
    private BookingDAO bookingDAO = new BookingDAO();

    public boolean createSupportStaff(String username, String password, String fullName, String email, String phone) {
        if (userDAO.checkUsernameExists(username)) {
            System.err.println("Username đã tồn tại!");
            return false;
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword_hash(password);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setDepartment("Support");
        return userDAO.createSupportStaff(user);
    }

    public List<Booking> getPendingBookings() {
        return bookingDAO.getBookingsByStatus("PENDING");
    }

    public List<Booking> getApprovedBookings() {
        return bookingDAO.getBookingsByStatus("APPROVED");
    }

    public boolean approveBooking(int bookingId) {
        boolean success = bookingDAO.updateBookingStatus(bookingId, "APPROVED");
        if (success) {
            Logger.info("Admin đã phê duyệt yêu cầu đặt phòng (ID: " + bookingId + ")");
        }
        return success;
    }

    public boolean rejectBooking(int bookingId) {
        boolean success = bookingDAO.updateBookingStatus(bookingId, "REJECTED");
        if (success) {
            Logger.info("Admin đã từ chối yêu cầu đặt phòng (ID: " + bookingId + ")");
        }
        return success;
    }

    public List<User> getSupportStaffs() {
        return userDAO.getUsersByRole(Role.SUPPORT_STAFF);
    }

    public boolean assignSupportStaff(int bookingId, int staffId) {
        boolean success = bookingDAO.updateBookingSupportStaff(bookingId, staffId);
        if (success) {
            Logger.info("Admin đã phân công Support Staff (ID: " + staffId + ") cho Booking (ID: " + bookingId + ")");
        }
        return success;
    }

    public void printStatistics() {
        bookingDAO.printRevenueByMonth();
        System.out.println();
        bookingDAO.printMostBookedRooms();
    }

    public void printRecentFeedbacks() {
        System.out.println("\n=== PHẢN HỒI GẦN ĐÂY ===");
        List<Booking> list = getApprovedBookings();
        boolean hasFeedback = false;
        for (Booking b : list) {
            if (b.getRating() > 0) {
                System.out.printf("Cần ID: %d | Phòng: %d | Đánh giá: %d Sao | Nhận xét: %s\n",
                        b.getId(), b.getRoomId(), b.getRating(), b.getFeedback());
                hasFeedback = true;
            }
        }
        if (!hasFeedback) {
            System.out.println("Chưa có phản hồi nào.");
        }
    }
}
