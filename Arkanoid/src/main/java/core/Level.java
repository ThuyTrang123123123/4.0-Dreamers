package core;

import entities.bricks.Brick;
import java.util.ArrayList;
import java.util.List;

public class Level {
    private final List<Brick> bricks;

    public Level(int rows, int cols) {
        bricks = new ArrayList<>();
        generateLevel(rows, cols);
    }

    private void generateLevel(int rows, int cols) {
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

    public List<Brick> getBricks() {
        return bricks;
    }
}
