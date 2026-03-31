package presentation;

import model.User;
import service.AuthService;
import util.Logger;

import java.util.Scanner;
import java.io.Console;
import java.util.regex.Pattern;

public class MainMenu {
    private AuthService authService = new AuthService();
    private Scanner scanner = new Scanner(System.in);

    public void start() {
        while (true) {
            System.out.println("\n=== HỆ THỐNG QUẢN LÝ PHÒNG HỌP ===");
            System.out.println("1. Đăng nhập");
            System.out.println("2. Đăng ký (Dành cho Nhân viên)");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleLogin();
                    break;
                case "2":
                    handleRegister();
                    break;
                case "0":
                    System.out.println("Tạm biệt!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void handleLogin() {
        System.out.println("\n=== ĐĂNG NHẬP ===");
        System.out.print("Username: "); String user = scanner.nextLine();
        String pass = readPassword("Password: ");

        User loggedInUser = authService.login(user, pass);
        if (loggedInUser != null) {
            System.out.println("Đăng nhập thành công! Vai trò: " + loggedInUser.getRole());
            switch (loggedInUser.getRole()) {
                case ADMIN:
                    new AdminMenu().showMenu();
                    break;
                case EMPLOYEE:
                    new EmployeeMenu(loggedInUser).showMenu();
                    break;
                case SUPPORT_STAFF:
                    new SupportStaffMenu(loggedInUser).showMenu();
                    break;
                default:
                    System.out.println("Vai trò không hợp lệ!");
            }
        } else {
            System.out.println("Đăng nhập thất bại. Sai thông tin hoặc tài khoản không tồn tại!");
        }
    }

    private void handleRegister() {
        System.out.println("\n=== ĐĂNG KÝ NHÂN VIÊN ===");

        String user = "";
        while (user.isEmpty()) {
            System.out.print("Username (bắt buộc): ");
            user = scanner.nextLine().trim();
        }

        String pass = "";
        while (pass.isEmpty()) {
            pass = readPassword("Password (bắt buộc): ").trim();
        }

        String fn = "";
        while (fn.isEmpty()) {
            System.out.print("Họ tên (bắt buộc): ");
            fn = scanner.nextLine().trim();
        }

        String email = "";
        while (true) {
            System.out.print("Email (bắt buộc, định dạng hợp lệ): ");
            email = scanner.nextLine().trim();
            if (isValidEmail(email)) break;
            System.out.println("Lỗi: Email không hợp lệ (vd: abc@domain.com).");
        }

        String phone = "";
        while (true) {
            System.out.print("SĐT (bắt buộc, 10-11 số): ");
            phone = scanner.nextLine().trim();
            if (isValidPhone(phone)) break;
            System.out.println("Lỗi: Số điện thoại không hợp lệ.");
        }

        System.out.print("Phòng ban: "); String dept = scanner.nextLine().trim();

        if (authService.registerEmployee(user, pass, fn, email, phone, dept)) {
            System.out.println("Đăng ký thành công! Bạn có thể đăng nhập ngay bây giờ.");
        } else {
            System.out.println("Đăng ký thất bại. Vui lòng thử lại.");
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.compile(emailRegex).matcher(email).matches() && !email.isEmpty();
    }

    private boolean isValidPhone(String phone) {
        String phoneRegex = "^[0-9]{10,11}$";
        return Pattern.compile(phoneRegex).matcher(phone).matches();
    }

    private String readPassword(String prompt) {
        Console console = System.console();
        if (console != null) {
            char[] passwordArray = console.readPassword(prompt);
            return new String(passwordArray);
        } else {
            // Fallback dành cho các IDE (như IntelliJ, Eclipse) không hỗ trợ System.console()
            System.out.print(prompt);
            return scanner.nextLine();
        }
    }

    public static void main(String[] args) {
        // Tạo dữ liệu mẫu cho người dùng
//        SeedUsers.init();

        System.out.println("Tạo dữ liệu mẫu thành công.");
        Logger.info("Hệ thống khởi động và kiểm tra dữ liệu mẫu.");

        MainMenu menu = new MainMenu();
        menu.start();
    }
}
