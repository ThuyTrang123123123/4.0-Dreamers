package util;

/**
 * Xử lý lỗi toàn cục — hiển thị thông báo, log hoặc gửi report.
 */
public class ErrorHandler {

    public static void handle(Exception e) {
        Logger.error("Exception: " + e.getMessage());
        e.printStackTrace();
    }

    public static void handle(String msg) {
        Logger.error(msg);
    }
}
