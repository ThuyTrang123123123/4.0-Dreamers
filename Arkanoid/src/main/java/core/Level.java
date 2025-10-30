package core;

import entities.bricks.Brick;
import java.util.ArrayList;
import java.util.List;

/**
 * Level - Quản lý 12 level với độ khó tăng dần
 * Thiết kế theo bảng: Level 1-12 với các hình dạng khác nhau
 */
public class Level {
    private final List<Brick> bricks;
    private int currentLevel = 1;
    private static final int MAX_LEVEL = 12;

    public Level(int rows, int cols) {
        bricks = new ArrayList<>();
        generateLevel(currentLevel);
    }

    /**
     * Tạo gạch theo từng level cụ thể
     */
    private void generateLevel(int level) {
        bricks.clear();

        switch (level) {
            case 1 -> generateLevel1();   // 1 hàng đơn giản - 5 gạch
            case 2 -> generateLevel2();   // 2 hàng - 12 gạch
            case 3 -> generateLevel3();   // 3 hàng - 21 gạch
            case 4 -> generateLevel4();   // Hình kim cương - ~28 gạch
            case 5 -> generateLevel5();   // Hình chữ T - ~16 gạch
            case 6 -> generateLevel6();   // Hình chữ X - 10 gạch
            case 7 -> generateLevel7();   // Hình vuông rỗng (khung) - ~24 gạch
            case 8 -> generateLevel8();   // Tam giác ngược - ~25 gạch
            case 9 -> generateLevel9();   // Lưới cờ vua (checkerboard) - ~23 gạch
            case 10 -> generateLevel10(); // Hình chữ H - ~15 gạch
            case 11 -> generateLevel11(); // Hình sóng - ~15 gạch
            case 12 -> generateLevel12(); // BOSS LEVEL - Full màn hình - 60 gạch
            default -> generateLevel1();
        }

        System.out.println("🎮 Level " + level + " - Số gạch: " + bricks.size());
    }

    // ===== Level 1: 1 hàng đơn giản (5 gạch) ⭐ =====
    private void generateLevel1() {
        int startX = 250, startY = 100;
        int brickWidth = 60, brickHeight = 20, gap = 10;

        for (int col = 0; col < 1; col++) {
            double x = startX + col * (brickWidth + gap);
            bricks.add(new Brick(x, startY, brickWidth, brickHeight));
        }
    }

    // ===== Level 2: 2 hàng (12 gạch) ⭐ =====
    private void generateLevel2() {
        int startX = 180, startY = 80;
        int brickWidth = 60, brickHeight = 20, gap = 10;

        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 6; col++) {
                double x = startX + col * (brickWidth + gap);
                double y = startY + row * (brickHeight + gap);
                bricks.add(new Brick(x, y, brickWidth, brickHeight));
            }
        }
    }

    // ===== Level 3: 3 hàng (21 gạch) ⭐⭐ =====
    private void generateLevel3() {
        int startX = 110, startY = 80;
        int brickWidth = 60, brickHeight = 20, gap = 10;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 7; col++) {
                double x = startX + col * (brickWidth + gap);
                double y = startY + row * (brickHeight + gap);
                bricks.add(new Brick(x, y, brickWidth, brickHeight));
            }
        }
    }

    // ===== Level 4: Hình kim cương 💎 (~28 gạch) ⭐⭐ =====
    private void generateLevel4() {
        int startX = 60, startY = 50;
        int brickWidth = 60, brickHeight = 20, gap = 10;

        // Hình kim cương: hàng giữa dài nhất, 2 đầu ngắn dần
        int[] colsPerRow = {4, 6, 8, 6, 4};  // Tổng ~28 gạch

        for (int row = 0; row < 5; row++) {
            int cols = colsPerRow[row];
            int offset = (10 - cols) / 2;  // Căn giữa

            for (int col = 0; col < cols; col++) {
                double x = startX + (offset + col) * (brickWidth + gap);
                double y = startY + row * (brickHeight + gap);
                bricks.add(new Brick(x, y, brickWidth, brickHeight));
            }
        }
    }

    // ===== Level 5: Hình chữ T (~16 gạch) ⭐⭐ =====
    private void generateLevel5() {
        int startX = 60, startY = 50;
        int brickWidth = 60, brickHeight = 20, gap = 10;

        // Hàng ngang trên (7 gạch)
        for (int col = 2; col < 9; col++) {
            double x = startX + col * (brickWidth + gap);
            bricks.add(new Brick(x, startY, brickWidth, brickHeight));
        }

        // Cột dọc giữa (3 gạch)
        for (int row = 1; row < 4; row++) {
            double x = startX + 5 * (brickWidth + gap);
            double y = startY + row * (brickHeight + gap);
            bricks.add(new Brick(x, y, brickWidth, brickHeight));
        }
    }

    // ===== Level 6: Hình chữ X (10 gạch) ⭐⭐⭐ =====
    private void generateLevel6() {
        int startX = 60, startY = 50;
        int brickWidth = 60, brickHeight = 20, gap = 10;

        for (int row = 0; row < 5; row++) {
            // Đường chéo chính
            double x1 = startX + row * 2 * (brickWidth + gap);
            double y = startY + row * (brickHeight + gap);
            bricks.add(new Brick(x1, y, brickWidth, brickHeight));

            // Đường chéo phụ (trừ gạch giữa để không trùng)
            if (row != 2) {
                double x2 = startX + (8 - row * 2) * (brickWidth + gap);
                bricks.add(new Brick(x2, y, brickWidth, brickHeight));
            }
        }
    }

    // ===== Level 7: Hình vuông rỗng - khung (~24 gạch) ⭐⭐⭐ =====
    private void generateLevel7() {
        int startX = 60, startY = 50;
        int brickWidth = 60, brickHeight = 20, gap = 10;

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 8; col++) {
                // Chỉ vẽ viền: hàng đầu, hàng cuối, cột đầu, cột cuối
                if (row == 0 || row == 4 || col == 0 || col == 7) {
                    double x = startX + col * (brickWidth + gap);
                    double y = startY + row * (brickHeight + gap);
                    bricks.add(new Brick(x, y, brickWidth, brickHeight));
                }
            }
        }
    }

    // ===== Level 8: Tam giác ngược (~25 gạch) ⭐⭐⭐ =====
    private void generateLevel8() {
        int startX = 60, startY = 50;
        int brickWidth = 60, brickHeight = 20, gap = 10;

        for (int row = 0; row < 5; row++) {
            int cols = 9 - row;  // Giảm dần: 9, 8, 7, 6, 5

            for (int col = 0; col < cols; col++) {
                double x = startX + col * (brickWidth + gap);
                double y = startY + row * (brickHeight + gap);
                bricks.add(new Brick(x, y, brickWidth, brickHeight));
            }
        }
    }

    // ===== Level 9: Lưới cờ vua - checkerboard (~23 gạch) ⭐⭐⭐⭐ =====
    private void generateLevel9() {
        int startX = 60, startY = 50;
        int brickWidth = 60, brickHeight = 20, gap = 10;

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 9; col++) {
                // Xen kẽ như bàn cờ
                if ((row + col) % 2 == 0) {
                    double x = startX + col * (brickWidth + gap);
                    double y = startY + row * (brickHeight + gap);
                    bricks.add(new Brick(x, y, brickWidth, brickHeight));
                }
            }
        }
    }

    // ===== Level 10: Hình chữ H (~15 gạch) ⭐⭐⭐⭐ =====
    private void generateLevel10() {
        int startX = 60, startY = 50;
        int brickWidth = 60, brickHeight = 20, gap = 10;

        for (int row = 0; row < 5; row++) {
            // Cột trái
            double x1 = startX + 2 * (brickWidth + gap);
            double y = startY + row * (brickHeight + gap);
            bricks.add(new Brick(x1, y, brickWidth, brickHeight));

            // Cột phải
            double x2 = startX + 7 * (brickWidth + gap);
            bricks.add(new Brick(x2, y, brickWidth, brickHeight));

            // Thanh ngang giữa (chỉ ở hàng 2)
            if (row == 2) {
                for (int col = 3; col < 7; col++) {
                    double x = startX + col * (brickWidth + gap);
                    bricks.add(new Brick(x, y, brickWidth, brickHeight));
                }
            }
        }
    }

    // ===== Level 11: Hình sóng 〰️ (~15 gạch) ⭐⭐⭐⭐ =====
    private void generateLevel11() {
        int startX = 60, startY = 50;
        int brickWidth = 60, brickHeight = 20, gap = 10;

        // Tạo hình sóng sin đơn giản
        for (int col = 0; col < 10; col++) {
            // Tính row theo hình sin
            int row = (int)(2 + 1.5 * Math.sin(col * Math.PI / 3));

            double x = startX + col * (brickWidth + gap);
            double y = startY + row * (brickHeight + gap);
            bricks.add(new Brick(x, y, brickWidth, brickHeight));
        }
    }

    // ===== Level 12: BOSS LEVEL - Full màn hình (60 gạch) ⭐⭐⭐⭐⭐ =====
    private void generateLevel12() {
        int startX = 60, startY = 50;
        int brickWidth = 60, brickHeight = 20, gap = 10;

        // 6 hàng x 10 cột = 60 gạch
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 10; col++) {
                double x = startX + col * (brickWidth + gap);
                double y = startY + row * (brickHeight + gap);
                bricks.add(new Brick(x, y, brickWidth, brickHeight));
            }
        }
    }

    /**
     * Chuyển sang level tiếp theo
     */
    public void regenerate() {
        if (currentLevel < MAX_LEVEL) {
            currentLevel++;
            generateLevel(currentLevel);
            System.out.println("📈 Chuyển sang Level " + currentLevel);
        } else {
            System.out.println("🎉 Chúc mừng! Bạn đã hoàn thành tất cả 12 level!");
        }
    }

    /**
     * Reset về level 1
     */
    public void reset() {
        currentLevel = 1;
        generateLevel(1);
    }

    /**
     * Kiểm tra xem đã hoàn thành level chưa
     */
    public boolean isComplete() {
        return bricks.stream().allMatch(Brick::isDestroyed);
    }

    /**
     * Kiểm tra xem đã hoàn thành TẤT CẢ 12 level chưa
     */
    public boolean isGameComplete() {
        return currentLevel == MAX_LEVEL && isComplete();
    }

    // ===== Getters =====
    public List<Brick> getBricks() { return bricks; }
    public int getCurrentLevel() { return currentLevel; }
    public int getMaxLevel() { return MAX_LEVEL; }
    public void setCurrentLevel(int level) {
        this.currentLevel = Math.min(level, MAX_LEVEL);
        generateLevel(this.currentLevel);
    }
}