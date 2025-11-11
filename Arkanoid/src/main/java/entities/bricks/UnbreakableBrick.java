package entities.bricks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class UnbreakableBrick extends Brick{
    public UnbreakableBrick(double x, double y, double width, double height) {
        super(x, y, width, height,0);
    }

    @Override
    public void hit() {
        setDestroyed(false);
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!isDestroyed()) {
            double drawX = getX() - getWidth() / 2;
            double drawY = getY() - getHeight() / 2;
            gc.setFill(Color.GRAY);
            gc.fillRect(drawX, drawY, getWidth(), getHeight());
        }
    }
}
