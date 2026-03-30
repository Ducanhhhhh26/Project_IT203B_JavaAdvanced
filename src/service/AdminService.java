package service;

import dao.UserDAO;
import dao.BookingDAO;
import model.User;
import model.Booking;
import model.Role;

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
        return bookingDAO.updateBookingStatus(bookingId, "APPROVED");
    }

    public boolean rejectBooking(int bookingId) {
        return bookingDAO.updateBookingStatus(bookingId, "REJECTED");
    }

    public List<User> getSupportStaffs() {
        return userDAO.getUsersByRole(Role.SUPPORT_STAFF);
    }

    public boolean assignSupportStaff(int bookingId, int staffId) {
        return bookingDAO.updateBookingSupportStaff(bookingId, staffId);
    }
}
