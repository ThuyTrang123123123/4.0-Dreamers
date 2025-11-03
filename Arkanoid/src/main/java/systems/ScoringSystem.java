package systems;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.application.Platform; // Cần thiết để xử lý đa luồng

/**
 * Hệ thống quản lý điểm số, mạng sống và thống kê game
 * Sử dụng JavaFX Property để hỗ trợ data binding với UI
 */
public class ScoringSystem {
    private final IntegerProperty score;
    private final IntegerProperty lives;
    private final IntegerProperty bricksDestroyed;

    public ScoringSystem() {
        this.score = new SimpleIntegerProperty(0);
        this.lives = new SimpleIntegerProperty(1);
        this.bricksDestroyed = new SimpleIntegerProperty(0);
    }

    // ===== Property cho data binding (Giữ nguyên) =====
    public IntegerProperty scoreProperty() { return score; }
    public IntegerProperty livesProperty() { return lives; }
    public IntegerProperty bricksDestroyedProperty() { return bricksDestroyed; }

    // ===== Getters - Lấy giá trị trực tiếp (Giữ nguyên) =====
    public int getScore() { return score.get(); }
    public int getLives() { return lives.get(); }
    public int getBricksDestroyed() { return bricksDestroyed.get(); }

    // ===== Game actions - Các hành động trong game (Đã bọc Platform.runLater) =====

    /**
     * Cộng điểm vào tổng điểm
     * @param points Số điểm muốn cộng
     */
    public void addScore(int points) {
        // BAO BỌC: Cập nhật Property trên Luồng UI
        Platform.runLater(() -> {
            this.score.set(this.score.get() + points);
        });
    }

    /**
     * Mất 1 mạng (khi bóng rơi xuống đáy)
     */
    public void loseLife() {
        // BAO BỌC: Cập nhật Property trên Luồng UI
        Platform.runLater(() -> {
            int current = this.lives.get();
            if (current > 0) {
                this.lives.set(current - 1);
            }
        });
    }

    /**
     * Thêm 1 mạng
     */
    public void addLife() {
        // BAO BỌC: Cập nhật Property trên Luồng UI
        Platform.runLater(() -> {
            this.lives.set(this.lives.get() + 1);
        });
    }

    /**
     * Tăng số lượng gạch đã phá lên 1 và kiểm tra thưởng mạng
     */
    public void incrementBricksDestroyed() {
        // BAO BỌC TOÀN BỘ LOGIC CÓ THAY ĐỔI PROPERTY
        Platform.runLater(() -> {
            int currentBricks = this.bricksDestroyed.get();
            this.bricksDestroyed.set(currentBricks + 1);

            // Kiểm tra thưởng mạng: mỗi 3 gạch được 1 mạng
            if ((currentBricks + 1) % 3 == 0) {
                // Tái sử dụng logic addLife, nhưng cần set trực tiếp để tránh runLater lồng nhau
                this.lives.set(this.lives.get() + 1);
            }
        });
    }

    /**
     * Kiểm tra game over (Giữ nguyên - chỉ là getter, không thay đổi Property)
     * @return true nếu hết mạng (lives <= 0)
     */
    public boolean isGameOver() {
        return lives.get() <= 0;
    }

    /**
     * Reset toàn bộ thống kê về giá trị ban đầu
     */
    public void reset() {
        // BAO BỌC: Cập nhật Property trên Luồng UI
        Platform.runLater(() -> {
            score.set(0);
            lives.set(1);
            bricksDestroyed.set(0);
        });
    }
}