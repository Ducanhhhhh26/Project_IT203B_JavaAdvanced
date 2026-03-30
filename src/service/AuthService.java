package service;

import dao.UserDAO;
import model.User;

public class AuthService {
    private UserDAO userDAO = new UserDAO();

    public User login(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            System.err.println("Username và password không được để trống.");
            return null;
        }
        return userDAO.authenticate(username, password);
    }

    public boolean registerEmployee(String username, String password, String fullName, String email, String phone, String department) {
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
        user.setDepartment(department);
        return userDAO.registerEmployee(user);
    }
}
