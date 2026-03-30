package presentation;

import model.User;
import java.util.Scanner;

public class SupportStaffMenu {
    private User currentUser;
    private Scanner scanner = new Scanner(System.in);

    public SupportStaffMenu(User user) {
        this.currentUser = user;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n=== MENU NHÂN VIÊN HỖ TRỢ ===");
            System.out.println("Xin chào, " + currentUser.getFullName());
            // TODO: Implement Booking Preparation later
            System.out.println("0. Đăng xuất");
            System.out.print("Chọn: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "0":
                    return;
                default:
                    System.out.println("Tính năng đang phát triển hoặc lựa chọn không hợp lệ!");
            }
        }
    }
}
