package entities.bricks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

import java.util.List;

/**
 * Lớp ExplodingBrick.
 * Đại diện cho một viên gạch nổ. Khi bị phá hủy, nó sẽ kích hoạt vụ nổ.
 * phá hủy các viên gạch lân cận và có thể tạo ra phản ứng dây chuyền .
 * Kế thừa từ BreakableBrick và chỉ cần 1 hitPoint.
 */

public class ExplodingBrick extends BreakableBrick {
    private static final int GRID_RANGE = 1; // nổ lan 8 ô xung quanh (1 ô mỗi hướng)
    private boolean hasExploded = false;

    public boolean hasExploded() {
        return hasExploded;
    }

    public void setHasExploded(boolean value) {
        this.hasExploded = value;
    }

    public ExplodingBrick(double x, double y, double width, double height) {
        super(x, y, width, height, 1);
    }

    @Override
    public void hit() {
        super.hit();
    }

    /**
     * Nổ lan ra 8 ô xung quanh (và đệ quy nếu gặp ExplodingBrick).
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
        if (isDestroyed()) return;

        double drawX = getX() - getWidth() / 2;
        double drawY = getY() - getHeight() / 2;

        // Màu chủ đạo đỏ
        Color brickColor = Color.web("#C0392B"); // đỏ đậm
        Color darkColor = brickColor.darker();
        Color lightColor = brickColor.brighter();

        // Lớp nền đậm
        gc.setFill(darkColor.darker());
        gc.fillRect(drawX + 1, drawY + 1, getWidth() - 2, getHeight() - 2);

        // Hiệu ứng 3D
        LinearGradient gradient = new LinearGradient(
                0, drawY + 3, 0, drawY + getHeight() - 3,
                false, CycleMethod.NO_CYCLE,
                new Stop(0, lightColor),
                new Stop(0.5, brickColor),
                new Stop(1, darkColor)
        );
        gc.setFill(gradient);
        gc.fillRect(drawX + 3, drawY + 3, getWidth() - 6, getHeight() - 6);

        // Sáng phía trên
        LinearGradient topHighlight = new LinearGradient(
                0, drawY + 3, 0, drawY + getHeight() * 0.4,
                false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(255, 255, 255, 0.35)),
                new Stop(0.5, Color.rgb(255, 255, 255, 0.15)),
                new Stop(1, Color.TRANSPARENT)
        );
        gc.setFill(topHighlight);
        gc.fillRect(drawX + 4, drawY + 4, getWidth() - 8, getHeight() * 0.4);

        // Bóng tối phía dưới
        LinearGradient bottomShadow = new LinearGradient(
                0, drawY + getHeight() * 0.6, 0, drawY + getHeight() - 3,
                false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.TRANSPARENT),
                new Stop(0.5, Color.rgb(0, 0, 0, 0.25)),
                new Stop(1, Color.rgb(0, 0, 0, 0.45))
        );
        gc.setFill(bottomShadow);
        gc.fillRect(drawX + 4, drawY + getHeight() * 0.6, getWidth() - 8, getHeight() * 0.37);

        // Viền trong sáng/tối
        gc.setStroke(lightColor);
        gc.setLineWidth(1);
        gc.strokeRect(drawX + 2.5, drawY + 2.5, getWidth() - 5, getHeight() - 5);

        gc.setStroke(darkColor);
        gc.strokeRect(drawX + 3.5, drawY + 3.5, getWidth() - 7, getHeight() - 7);
    }

}