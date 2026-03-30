package service;

import dao.UserDAO;
import model.User;

public class AdminService {
    private UserDAO userDAO = new UserDAO();

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
}
