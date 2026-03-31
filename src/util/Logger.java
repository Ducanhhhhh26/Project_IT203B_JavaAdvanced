package util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final String LOG_FILE = "system.log";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void info(String message) {
        log("INFO", message);
    }

    public static void warning(String message) {
        log("WARNING", message);
    }

    public static void error(String message) {
        log("ERROR", message);
    }

    private static void log(String level, String message) {
        String time = LocalDateTime.now().format(formatter);
        String formattedMessage = String.format("[%s] [%s] %s%n", time, level, message);

        // Print to console
        if (level.equals("ERROR") || level.equals("WARNING")) {
            System.err.print(formattedMessage);
        } else {
            System.out.print(formattedMessage);
        }

        // Write to file
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.print(formattedMessage);
        } catch (IOException e) {
            System.err.println("Không thể ghi log vào tệp: " + e.getMessage());
        }
    }
}
