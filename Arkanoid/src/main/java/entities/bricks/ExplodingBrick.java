package entities.bricks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.List;

public class ExplodingBrick extends BreakableBrick {
    private static final int GRID_RANGE = 1; // nổ lan 8 ô xung quanh (1 ô mỗi hướng)

    public ExplodingBrick(double x, double y, double width, double height) {
        super(x, y, width, height,1);
    }

    @Override
    public void hit() {
        super.hit();

    }

    /**
     * Nổ lan ra 8 ô xung quanh (và đệ quy nếu gặp ExplodingBrick)
     */
    public int exployNearbyAndCount(List<Brick> bricks) {
        int destroyedCount = 0;
        // Nếu bản thân chưa bị phá → phá và tính
        if (!isDestroyed()) {
            hit();
            if (isDestroyed()) {
                destroyedCount++;
            }
        }

        double neighborRangeX = getWidth() + 10;
        double neighborRangeY = getHeight() + 10;

        for (Brick other : bricks) {
            if (!other.isDestroyed() && other != this) {
                double dx = Math.abs(other.getX() - getX());
                double dy = Math.abs(other.getY() - getY());

                if (dx <= neighborRangeX && dy <= neighborRangeY) {
                    other.hit();

                    if (other.isDestroyed()) {
                        destroyedCount++;

                        if (other instanceof ExplodingBrick nextExploding) {
                            destroyedCount += nextExploding.exployNearbyAndCount(bricks);
                        }
                    }
                }
            }
        }

        return destroyedCount;
    }



    @Override
    public void render(GraphicsContext gc) {
        if (!isDestroyed()) {
            double drawX = getX() - getWidth() / 2;
            double drawY = getY() - getHeight() / 2;

            gc.setFill(Color.RED);
            gc.fillRect(drawX, drawY, getWidth(), getHeight());
            gc.setStroke(Color.DARKRED);
            gc.strokeRect(drawX, drawY, getWidth(), getHeight());
        }
    }
}