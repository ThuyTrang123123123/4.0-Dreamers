package systems;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Hệ thống quản lý điểm số, mạng sống và thống kê game
 * Sử dụng JavaFX Property để hỗ trợ data binding với UI
 */
public class ScoringSystem {
    private final IntegerProperty score;           // Điểm số hiện tại
    private final IntegerProperty lives;           // Số mạng còn lại
    private final IntegerProperty bricksDestroyed; // Số gạch đã phá

    /**
     * Khởi tạo hệ thống với giá trị mặc định:
     * - Điểm: 0
     * - Mạng: 0 (thưởng mạng khi phá 3 gạch)
     * - Gạch phá: 0
     */
    public ScoringSystem() {
        this.score = new SimpleIntegerProperty(0);
        this.lives = new SimpleIntegerProperty(1);
        this.bricksDestroyed = new SimpleIntegerProperty(0);
    }

    // ===== Property cho data binding =====
    // Các method này cho phép UI tự động cập nhật khi giá trị thay đổi

    /** Trả về Property của điểm số (dùng cho binding) */
    public IntegerProperty scoreProperty() { return score; }

    /** Trả về Property của mạng sống (dùng cho binding) */
    public IntegerProperty livesProperty() { return lives; }

    /** Trả về Property của số gạch đã phá (dùng cho binding) */
    public IntegerProperty bricksDestroyedProperty() { return bricksDestroyed; }

    // ===== Getters - Lấy giá trị trực tiếp =====

    /** Lấy điểm số hiện tại */
    public int getScore() { return score.get(); }

    /** Lấy số mạng còn lại */
    public int getLives() { return lives.get(); }

    /** Lấy số gạch đã phá */
    public int getBricksDestroyed() { return bricksDestroyed.get(); }

    // ===== Game actions - Các hành động trong game =====

    /**
     * Cộng điểm vào tổng điểm
     * @param points Số điểm muốn cộng (thường là 10 khi phá 1 gạch)
     */
    public void addScore(int points) {
        this.score.set(this.score.get() + points);
    }

    /**
     * Mất 1 mạng (khi bóng rơi xuống đáy)
     * Chỉ giảm nếu còn mạng (không cho số âm)
     */
    public void loseLife() {
        int current = this.lives.get();
        if (current > 0) {
            this.lives.set(current - 1);
        }
    }

    /**
     * Thêm 1 mạng (có thể dùng cho power-up sau này)
     */
    public void addLife() {
        this.lives.set(this.lives.get() + 1);
    }

    /**
     * Tăng số lượng gạch đã phá lên 1
     * Gọi mỗi khi phá được 1 viên gạch
     *
     * ⭐ Cơ chế thưởng mạng: Cứ mỗi 3 viên gạch phá → +1 mạng
     */
    public void incrementBricksDestroyed() {
        int currentBricks = this.bricksDestroyed.get();
        this.bricksDestroyed.set(currentBricks + 1);

        // Kiểm tra thưởng mạng: mỗi 3 gạch được 1 mạng
        if ((currentBricks + 1) % 3 == 0) {
            addLife();
        }
    }

    /**
     * Kiểm tra game over
     * @return true nếu hết mạng (lives <= 0)
     */
    public boolean isGameOver() {
        return lives.get() <= 0;
    }

    /**
     * Reset toàn bộ thống kê về giá trị ban đầu
     * Dùng khi restart game
     */
    public void reset() {
        score.set(0);
        lives.set(1);  // Bắt đầu với 0 mạng
        bricksDestroyed.set(0);
    }
}