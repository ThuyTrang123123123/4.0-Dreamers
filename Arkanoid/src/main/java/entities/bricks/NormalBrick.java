package entities.bricks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class NormalBrick extends Brick {

    public NormalBrick(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void hit() {
        setDestroyed(true); // Vỡ ngay sau 1 lần chạm
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!isDestroyed()) {
            double drawX = getX() - getWidth() / 2;
            double drawY = getY() - getHeight() / 2;
            gc.setFill(Color.ORANGE);
            gc.fillRect(drawX, drawY, getWidth(), getHeight());
            gc.setStroke(Color.DARKORANGE);
            gc.strokeRect(drawX, drawY, getWidth(), getHeight());
        }
    }
}