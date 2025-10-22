package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Ghi log ra console kèm timestamp, dùng cho debug */
public class Logger {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void info(String msg) {
        System.out.println("[INFO " + time() + "] " + msg);
    }

    public static void warn(String msg) {
        System.out.println("[WARN " + time() + "] " + msg);
    }

    public static void error(String msg) {
        System.err.println("[ERROR " + time() + "] " + msg);
    }

    private static String time() {
        return LocalDateTime.now().format(FORMATTER);
    }
}
