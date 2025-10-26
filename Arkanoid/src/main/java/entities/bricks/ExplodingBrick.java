package entities.bricks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import ui.theme.Colors;

public class ExplodingBrick extends Brick {
    private boolean exploded = false;

    public ExplodingBrick(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void hit() {
        if (!isDestroyed()) {
            setDestroyed(true);
            exploded = true; // báo cho Level biết để nổ lan
        }
    }

    public boolean hasExploded() {
        return exploded;
    }

    public void resetExplosion() {
        exploded = false;
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!isDestroyed()) {
            double drawX = getX() - getWidth() / 2;
            double drawY = getY() - getHeight() / 2;
            gc.setFill(Color.CRIMSON);
            gc.fillRect(drawX, drawY, getWidth(), getHeight());
            gc.setStroke(Color.DARKRED);
            gc.strokeRect(drawX, drawY, getWidth(), getHeight());
        }
    }
}
