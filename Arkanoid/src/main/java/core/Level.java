package core;

import entities.bricks.Brick;
import entities.bricks.ExplodingBrick;
import entities.bricks.HardBrick;
import entities.bricks.NormalBrick;

import java.util.ArrayList;
import java.util.List;

/**
 * Level - Quản lý level và tạo gạch
 * Mỗi level có cùng độ khó, chỉ khác về số thứ tự
 */
public class Level {
    private final List<Brick> bricks;        // Danh sách gạch trong level
    private int currentLevel = 1;             // Level hiện tại (bắt đầu từ 1)

    /**
     * Constructor - Tạo level với số hàng và cột gạch
     * @param rows Số hàng gạch
     * @param cols Số cột gạch
     */
    public Level(int rows, int cols) {
        bricks = new ArrayList<>();
        generateLevel(rows, cols);
    }

    /**
     * Tạo lưới gạch cho level
     * @param rows Số hàng
     * @param cols Số cột
     */
    private void generateLevel(int rows, int cols) {
        bricks.clear(); // Xóa gạch cũ (nếu có)

        int startX = 60;
        int startY = 50;
        int brickWidth = 60;
        int brickHeight = 20;
        int gap = 10;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double x = startX + col * (brickWidth + gap);
                double y = startY + row * (brickHeight + gap);
                bricks.add(new Brick(x, y, brickWidth, brickHeight));
            }
        }
    }

    /**
     * Tạo lại level mới (giữ nguyên kích thước)
     * Gọi khi chuyển sang level tiếp theo
     */
    public void regenerate() {
        // Lấy kích thước từ level cũ (giả sử có ít nhất 1 gạch)
        int rows = Config.BRICK_ROWS;
        int cols = Config.BRICK_COLS;

        currentLevel++;
        generateLevel(rows, cols);
    }

    /**
     * Reset về level 1
     */
    public void reset() {
        currentLevel = 1;
        generateLevel(Config.BRICK_ROWS, Config.BRICK_COLS);
    }

    /**
     * Kiểm tra xem level đã hoàn thành chưa
     * @return true nếu tất cả gạch đã bị phá
     */
    public boolean isComplete() {
        return bricks.stream().allMatch(Brick::isDestroyed);
    }

    // ===== Getters =====

    /**
     * Lấy danh sách gạch
     */
    public List<Brick> getBricks() {
        return bricks;
    }

    /**
     * Lấy level hiện tại
     */
    public int getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Set level hiện tại (dùng khi load game)
     */
    public void setCurrentLevel(int level) {
        this.currentLevel = level;
    }
}