package core;

import entities.bricks.*;

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
            case 1 -> generateLevel1();
            case 2 -> generateLevel2();
            case 3 -> generateLevel3();
            case 4 -> generateLevel4();
            case 5 -> generateLevel5();
            case 6 -> generateLevel6();
            case 7 -> generateLevel7();
            case 8 -> generateLevel8();
            case 9 -> generateLevel9();
            case 10 -> generateLevel10();
            case 11 -> generateLevel11();
            case 12 -> generateLevel12();
            default -> generateLevel1();
        }

        System.out.println("Level " + level + " - Số gạch: " + bricks.size());
    }

    // ===== Level 1: 1 hàng đơn giản (5 gạch)
    private void generateLevel1() {
        int startX = 250, startY = 100;
        int brickWidth = 60, brickHeight = 20, gap = 10;

        for (int col = 0; col < 1; col++) {
            double x = startX + col * (brickWidth + gap);
            bricks.add(new Brick(x, startY, brickWidth, brickHeight));
        }
    }

    // ===== Level 2: 2 hàng (12 gạch)
    private void generateLevel2() {
        int startX = 180, startY = 80;
        int brickWidth = 60, brickHeight = 20, gap = 10;

        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 6; col++) {
                double x = startX + col * (brickWidth + gap);
                double y = startY + row * (brickHeight + gap);
                bricks.add(new ExplodingBrick(x, y, brickWidth, brickHeight));
            }
        }
    }

    // ===== Level 3: 3 hàng (21 gạch)
    private void generateLevel3() {
        int startX = 110, startY = 80;
        int brickWidth = 60, brickHeight = 20, gap = 10;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 7; col++) {
                double x = startX + col * (brickWidth + gap);
                double y = startY + row * (brickHeight + gap);
                bricks.add(new NormalBrick(x, y, brickWidth, brickHeight));
            }
        }
    }

    // ===== Level 4: Hình kim cương (~28 gạch)
    private void generateLevel4() {
        int startX = 60, startY = 50;
        int brickWidth = 60, brickHeight = 20, gap = 10;

        int[] colsPerRow = {4, 6, 8, 6, 4};

        for (int row = 0; row < 5; row++) {
            int cols = colsPerRow[row];
            int offset = (10 - cols) / 2;

            for (int col = 0; col < cols; col++) {
                double x = startX + (offset + col) * (brickWidth + gap);
                double y = startY + row * (brickHeight + gap);
                bricks.add(new Brick(x, y, brickWidth, brickHeight));
            }
        }
    }

    // ===== Level 5: Hình chữ T (~16 gạch)
    private void generateLevel5() {
        int startX = 60, startY = 50;
        int brickWidth = 60, brickHeight = 20, gap = 10;

        for (int col = 2; col < 9; col++) {
            double x = startX + col * (brickWidth + gap);
            bricks.add(new Brick(x, startY, brickWidth, brickHeight));
        }

        for (int row = 1; row < 4; row++) {
            double x = startX + 5 * (brickWidth + gap);
            double y = startY + row * (brickHeight + gap);
            bricks.add(new Brick(x, y, brickWidth, brickHeight));
        }
    }

    // ===== Level 6: Hình chữ X (10 gạch)
    private void generateLevel6() {
        int startX = 60, startY = 50;
        int brickWidth = 60, brickHeight = 20, gap = 10;

        for (int row = 0; row < 5; row++) {
            double x1 = startX + row * 2 * (brickWidth + gap);
            double y = startY + row * (brickHeight + gap);
            bricks.add(new Brick(x1, y, brickWidth, brickHeight));

            if (row != 2) {
                double x2 = startX + (8 - row * 2) * (brickWidth + gap);
                bricks.add(new Brick(x2, y, brickWidth, brickHeight));
            }
        }
    }

    // ===== Level 7: Hình vuông rỗng - khung (~24 gạch)
    private void generateLevel7() {
        int startX = 60, startY = 50;
        int brickWidth = 60, brickHeight = 20, gap = 10;

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 8; col++) {
                if (row == 0 || row == 4 || col == 0 || col == 7) {
                    double x = startX + col * (brickWidth + gap);
                    double y = startY + row * (brickHeight + gap);
                    bricks.add(new Brick(x, y, brickWidth, brickHeight));
                }
            }
        }
    }

    // ===== Level 8: Tam giác ngược (~25 gạch)
    private void generateLevel8() {
        int startX = 60, startY = 50;
        int brickWidth = 60, brickHeight = 20, gap = 10;

        for (int row = 0; row < 5; row++) {
            int cols = 9 - row;

            for (int col = 0; col < cols; col++) {
                double x = startX + col * (brickWidth + gap);
                double y = startY + row * (brickHeight + gap);
                bricks.add(new Brick(x, y, brickWidth, brickHeight));
            }
        }
    }

    // ===== Level 9: Lưới cờ vua
    private void generateLevel9() {
        int startX = 60, startY = 50;
        int brickWidth = 60, brickHeight = 20, gap = 10;

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 9; col++) {
                if ((row + col) % 2 == 0) {
                    double x = startX + col * (brickWidth + gap);
                    double y = startY + row * (brickHeight + gap);
                    bricks.add(new Brick(x, y, brickWidth, brickHeight));
                }
            }
        }
    }

    // ===== Level 10:
    private void generateLevel10() {
        int startX = 60, startY = 50;
        int brickWidth = 60, brickHeight = 20, gap = 10;

        for (int row = 0; row < 5; row++) {
            double x1 = startX + 2 * (brickWidth + gap);
            double y = startY + row * (brickHeight + gap);
            bricks.add(new Brick(x1, y, brickWidth, brickHeight));

            double x2 = startX + 7 * (brickWidth + gap);
            bricks.add(new Brick(x2, y, brickWidth, brickHeight));

            if (row == 2) {
                for (int col = 3; col < 7; col++) {
                    double x = startX + col * (brickWidth + gap);
                    bricks.add(new Brick(x, y, brickWidth, brickHeight));
                }
            }
        }
    }

    // ===== Level 11: Hình sóng
    private void generateLevel11() {
        int startX = 60, startY = 50;
        int brickWidth = 60, brickHeight = 20, gap = 10;

        for (int col = 0; col < 10; col++) {
            int row = (int)(2 + 1.5 * Math.sin(col * Math.PI / 3));

            double x = startX + col * (brickWidth + gap);
            double y = startY + row * (brickHeight + gap);
            bricks.add(new Brick(x, y, brickWidth, brickHeight));
        }
    }

    // ===== Level 12: BOSS LEVEL - Full màn hình (60 gạch)
    private void generateLevel12() {
        int startX = 60, startY = 50;
        int brickWidth = 60, brickHeight = 20, gap = 10;

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 10; col++) {
                double x = startX + col * (brickWidth + gap);
                double y = startY + row * (brickHeight + gap);
                bricks.add(new Brick(x, y, brickWidth, brickHeight));
            }
        }
    }

    public void regenerate() {
        if (currentLevel < MAX_LEVEL) {
            currentLevel++;
            generateLevel(currentLevel);
            System.out.println("Chuyển sang Level " + currentLevel);
        } else {
            System.out.println("Chúc mừng! Bạn đã hoàn thành tất cả 12 level!");
        }
    }

    public void reset() {
        currentLevel = 1;
        generateLevel(1);
    }

    public boolean isComplete() {
        return bricks.stream().allMatch(Brick::isDestroyed);
    }

    public boolean isGameComplete() {
        return currentLevel == MAX_LEVEL && isComplete();
    }

    public List<Brick> getBricks() { return bricks; }
    public int getCurrentLevel() { return currentLevel; }
    public int getMaxLevel() { return MAX_LEVEL; }
    public void setCurrentLevel(int level) {
        this.currentLevel = Math.min(level, MAX_LEVEL);
        generateLevel(this.currentLevel);
    }
}
