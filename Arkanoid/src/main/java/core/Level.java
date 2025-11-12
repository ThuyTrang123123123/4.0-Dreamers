package core;

import entities.bricks.*;
import entities.bricks.factory.BrickFactory;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Level - Quản lý 12 level với độ khó tăng dần
 * Sử dụng BrickFactory để tạo bricks (Factory Pattern)
 */
public class Level {
    private final List<Brick> bricks;
    private int currentLevel = 1;
    private static final int MAX_LEVEL = 12;
    private final BrickFactory brickFactory = new BrickFactory();
    private Image backgroundImage;

    public Level(int rows, int cols) {
        bricks = new ArrayList<>();
        generateLevel(currentLevel);
    }

    private String pathForLevel() {
        return "/images/CLevel " + currentLevel + ".png";
    }

    public Image getBackgroundImage() {
        if (backgroundImage == null) {
            try {
                backgroundImage = new Image(
                        Objects.requireNonNull(
                                getClass().getResource(pathForLevel())
                        ).toExternalForm()
                );
            } catch (Exception e) {
                System.err.println("Không tìm thấy ảnh nền level " + currentLevel + " : " + e.getMessage());
            }
        }
        return backgroundImage;
    }

    private void generateLevel(int level) {
        bricks.clear();
        backgroundImage = null;

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

        System.out.println("Level " + level + " - Tổng gạch: " + bricks.size());
    }


    private void generateLevel1() {
        String[][] pattern = {
                {"", "N", "", "", "", "", "", "N", ""},
                {"", "H", "", "", "", "", "", "H", ""},
                {"H", "", "H", "", "", "", "H", "", "H"},
                {"N", "", "N", "", "", "", "N", "", "N"},
                {"", "", "", "N", "", "N", "", "", ""},
                {"", "", "", "H", "", "H", "", "", ""},
                {"", "", "", "", "H", "", "", "", ""},
                {"", "", "", "", "N", "", "", "", ""}
        };
        addBricksFromPattern(pattern);
    }

    private void generateLevel2() {
        String[][] pattern = {
                {"", "H", "", "", "", "", "H", ""},
                {"", "", "H", "H", "H", "H", "", ""},
                {"", "H", "N", "N", "N", "N", "H", ""},
                {"H", "N", "", "", "", "", "N", "H"},
                {"N", "", "", "", "", "", "", "N"},
                {"N", "", "", "", "", "", "", "N"},
                {"H", "N", "", "", "", "", "N", "H"},
                {"", "H", "N", "N", "N", "N", "H", ""},
                {"", "", "H", "H", "H", "H", "", ""}
        };
        addBricksFromPattern(pattern);
    }

    private void generateLevel3() {
        String[][] pattern = {
                {"N", "", "", "", "", "", "N"},
                {"", "N", "", "", "", "N", ""},
                {"", "H", "N", "", "N", "H", ""},
                {"", "H", "", "N", "", "H", ""},
                {"", "H", "", "", "", "H", ""},
                {"", "H", "", "", "", "H", ""},
                {"", "H", "", "N", "", "H", ""},
                {"", "H", "N", "", "N", "H", ""},
                {"", "N", "", "", "", "N", ""},
                {"N", "", "", "", "", "", "N"}
        };
        addBricksFromPattern(pattern);
    }

    private void generateLevel4() {
        String[][] pattern = {
                {"", "N", "H", "H", "", ""},
                {"", "N", "H", "H", "H", ""},
                {"N", "", "N", "", "H", ""},
                {"N", "", "N", "", "", ""},
                {"", "N", "", "", "", ""},
                {"", "N", "", "", "", ""},
                {"", "", "", "", "N", ""},
                {"", "", "", "", "N", ""},
                {"", "", "", "N", "", "N"},
                {"", "H", "", "N", "", "N"},
                {"", "H", "H", "H", "N", ""},
                {"", "", "H", "H", "N", ""}
        };
        addBricksFromPattern(pattern);
    }

    private void generateLevel5() {
        String[][] pattern = {
                {"", "", "", "", "", "", "U", "", ""},
                {"", "", "", "", "", "H", "E", "H", ""},
                {"", "", "", "", "", "U", "", "U", ""},
                {"", "", "", "", "H", "", "", "", "U"},
                {"", "", "", "N", "H", "", "", "", "H"},
                {"", "", "N", "N", "N", "", "", "H", ""},
                {"", "N", "N", "", "N", "N", "", "H", ""},
                {"", "N", "", "", "", "N", "", "H", ""},
                {"", "N", "N", "", "N", "N", "", "H", ""},
                {"", "", "N", "N", "N", "", "", "E", "H"},
                {"", "", "", "N", "", "", "", "", "N"}
        };
        addBricksFromPattern(pattern);
    }

    private void generateLevel6() {
        String[][] pattern = {
                {"", "N", "", "N", "", "", "", "", ""},
                {"N", "N", "H", "N", "N", "", "H", "", ""},
                {"N", "", "H", "", "N", "H", "H", "H", ""},
                {"N", "", "H", "", "N", "H", "", "H", ""},
                {"N", "", "E", "", "N", "", "", "", "H"},
                {"N", "", "H", "", "N", "", "", "", "H"},
                {"N", "", "H", "", "N", "", "", "H", ""},
                {"N", "", "H", "", "N", "N", "", "H", ""},
                {"N", "", "H", "", "", "N", "N", "", ""},
                {"", "", "", "", "", "", "N", "N", ""},
                {"", "", "", "", "", "", "", "N", ""}
        };
        addBricksFromPattern(pattern);
    }

    private void generateLevel7() {
        String[][] pattern = {
                {"", "", "", "H", "", "", ""},
                {"", "", "H", "N", "H", "", ""},
                {"", "H", "N", "", "N", "H", ""},
                {"H", "N", "", "", "", "N", "H"},
                {"H", "", "", "", "", "", "H"},
                {"H", "", "", "", "", "", "H"},
                {"H", "", "", "", "", "", "H"},
                {"H", "N", "", "", "", "N", "H"},
                {"", "N", "N", "", "N", "N", ""},
                {"", "", "E", "", "E", "", ""},
                {"", "", "E", "", "E", "", ""},
                {"", "N", "N", "", "N", "N", ""},
                {"", "N", "N", "", "N", "N", ""},
                {"", "", "", "", "", "", ""},
                {"", "H", "H", "H", "H", "H", ""},
                {"", "N", "N", "N", "N", "N", ""}
        };
        addBricksFromPattern(pattern);
    }

    private void generateLevel8() {
        String[][] pattern = {
                {"", "N", "", "N", "", "", "", ""},
                {"N", "N", "H", "N", "N", "", "", ""},
                {"N", "", "N", "", "N", "", "", ""},
                {"N", "", "H", "", "N", "", "", ""},
                {"N", "", "N", "", "N", "", "", ""},
                {"E", "", "H", "", "E", "", "", ""},
                {"N", "", "N", "", "N", "", "H", ""},
                {"N", "", "H", "", "N", "U", "N", "U"},
                {"N", "", "N", "", "N", "", "N", ""},
                {"N", "", "H", "", "N", "N", "E", ""}
        };
        addBricksFromPattern(pattern);
    }

    private void generateLevel9() {
        String[][] pattern = {
                {"", "", "H", "H", "H"},
                {"", "", "N", "N", "N"},
                {"", "", "", "E", "N"},
                {"", "", "", "E", "N"},
                {"U", "", "N", "", "N"},
                {"", "H", "N", "", "N"},
                {"", "N", "H", "", ""},
                {"", "N", "", "U", ""},
                {"N", "", "", "", ""},
                {"N", "", "", "", ""}
        };
        addBricksFromPattern(pattern);
    }

    private void generateLevel10() {
        String[][] pattern = {
                {"H", "", "N", "", "", "", ""},
                {"", "N", "E", "N", "", "", ""},
                {"", "H", "", "H", "", "", ""},
                {"", "N", "", "N", "", "", ""},
                {"", "U", "", "U", "", "", ""},
                {"", "N", "", "N", "", "", ""},
                {"", "N", "", "N", "", "H", ""},
                {"", "N", "", "N", "", "E", ""},
                {"", "N", "", "N", "E", "", "E"},
                {"", "U", "", "N", "H", "", "H"},
                {"", "", "", "N", "", "H", ""},
                {"", "", "", "U", "", "U", ""},
                {"", "", "H", "", "", "", ""},
        };
        addBricksFromPattern(pattern);
    }

    private void generateLevel11() {
        String[][] pattern = {
                {"", "", "N", "", "N", "", "N", "", ""},
                {"", "N", "E", "", "E", "", "E", "N", ""},
                {"", "H", "", "H", "", "H", "", "H", ""},
                {"N", "", "", "U", "", "U", "", "", "N"},
                {"", "", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", "", ""},
                {"", "", "H", "", "H", "", "H","",""},
                {"", "H", "N", "", "N", "", "N", "H", ""},
                {"", "U", "", "H", "", "H", "", "U", ""},
                {"N", "", "", "U", "", "U", "", "", "N"},
        };
        addBricksFromPattern(pattern);
    }

    private void generateLevel12() {
        String[][] pattern = {
                {"H", "", "", "", "", "", "H"},
                {"N", "H","", "", "", "H", "N"},
                {"", "N", "H", "", "H", "N", ""},
                {"", "", "N", "", "N", "", ""},
                {"", "", "H", "", "H", "", ""},
                {"", "U", "E", "U", "E", "U", ""},
                {"", "", "H", "", "H", "", ""},
                {"", "", "N", "", "N", "", ""},
                {"", "N", "H", "", "H", "N", ""},
                {"N", "H","", "", "", "H", "N"},
                {"H", "", "", "", "", "", "H"},
        };
        addBricksFromPattern(pattern);
    }


    // Helper: Add bricks using BrickFactory
    private void addBricksFromPattern(String[][] pattern) {
        int brickWidth = 60, brickHeight = 20, gap = 3;
        int startX = 185, startY = 80;
        for (int row = 0; row < pattern.length; row++) {
            for (int col = 0; col < pattern[row].length; col++) {
                String typeStr = pattern[row][col];
                if (!typeStr.isEmpty()) {
                    char type = typeStr.charAt(0);
                    double x = startX + col * (brickWidth + gap);
                    double y = startY + row * (brickHeight + gap);
                    Brick brick = brickFactory.create(type, x, y, brickWidth, brickHeight);
                    if (brick != null) {
                        bricks.add(brick);
                    }
                }
            }
        }
        long hardCount = bricks.stream().filter(b -> b instanceof HardBrick).count();
        long explodingCount = bricks.stream().filter(b -> b instanceof ExplodingBrick).count();
        System.out.println("Level " + currentLevel + " - Total: " + bricks.size() + ", Hard: " + hardCount + ", Exploding: " + explodingCount + ", Normal: " + (bricks.size() - hardCount - explodingCount));
    }

    // Các phương thức còn lại giữ nguyên
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

    /**
     public boolean isComplete() {
     return bricks.stream().allMatch(Brick::isDestroyed);
     }**/
    public boolean isComplete() {
        return bricks.stream()
                .filter(brick -> brick instanceof BreakableBrick)
                .allMatch(Brick::isDestroyed);
    }


    public boolean isGameComplete() {
        return currentLevel == MAX_LEVEL && isComplete();
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getMaxLevel() {
        return MAX_LEVEL;
    }

    public void setCurrentLevel(int level) {
        this.currentLevel = Math.min(level, MAX_LEVEL);
        generateLevel(this.currentLevel);
    }
}