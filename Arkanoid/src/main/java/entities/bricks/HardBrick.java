package entities.bricks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class HardBrick extends Brick {
    private int hitCount = 0;
    private final int maxHits = 2; // cần 2 lần để phá

    public HardBrick(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void hit() {
        hitCount++;
        if (hitCount >= maxHits) {
            setDestroyed(true);
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!isDestroyed()) {
            double drawX = getX() - getWidth() / 2;
            double drawY = getY() - getHeight() / 2;
            gc.setFill(hitCount == 0 ? Color.DARKBLUE : Color.LIGHTBLUE);
            gc.fillRect(drawX, drawY, getWidth(), getHeight());
            gc.setStroke(Color.BLACK);
            gc.strokeRect(drawX, drawY, getWidth(), getHeight());
        }
    }
}