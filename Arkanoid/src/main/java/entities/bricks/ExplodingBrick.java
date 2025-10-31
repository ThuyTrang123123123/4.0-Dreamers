package entities.bricks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * Gạch nổ lan: khi bị phá sẽ phá luôn các gạch xung quanh.
 */
public class ExplodingBrick extends Brick {
    private static final double EXPLOSION_RADIUS = 80; // Bán kính nổ (pixel)

    public ExplodingBrick(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void hit() {
        super.hit();
    }

    /**
     * Phá lan ra các gạch gần đó (nếu chưa bị phá)
     */
    public void explodeNearby(List<Brick> bricks) {
        for (Brick other : bricks) {
            if (!other.isDestroyed() && other != this) {
                double dx = other.getX() - getX();
                double dy = other.getY() - getY();
                double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance <= EXPLOSION_RADIUS) {
                    other.hit();
                }
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!isDestroyed()) {
            double drawX = getX() - getWidth() / 2;
            double drawY = getY() - getHeight() / 2;

            // Gạch nổ — màu đỏ nổi bật
            gc.setFill(Color.RED);
            gc.fillRect(drawX, drawY, getWidth(), getHeight());
            gc.setStroke(Color.DARKRED);
            gc.strokeRect(drawX, drawY, getWidth(), getHeight());
        }
    }
}
