package entities.powerups;

import core.World;
import entities.Ball;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.geometry.Rectangle2D;

/**
 * PowerUp: Slow Ball — giảm tốc độ bóng.
 */
public class SlowBall extends PowerUp {

    private final double slowFactor = 0.7; // chậm lại 30%
    private final double duration = 8.0;

    public SlowBall(double x, double y) {
        super(x, y, 18, 18, Color.PURPLE);
        image = new Image(getClass().getResource("/images/SlowBall.png").toExternalForm());
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(x - width / 2, y - height / 2, width, height);
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!isActive()) return;

        if (image.isError()) {
            System.out.println("Không thể tải ảnh: /images/SlowBall.png");
            return;
        }

        gc.drawImage(image, x - width / 2, y - height / 2, width, height);
    }

    @Override
    public void onCollected(World world) {
        boolean hasFlyingBall = world.getBalls().stream().anyMatch(ball -> !ball.isStickToPaddle());
        if (!hasFlyingBall) return;

        for (Ball ball : world.getBalls()) {
            ball.setSpeedMultiplier(slowFactor);
        }

        new Thread(() -> {
            try {
                Thread.sleep((long) (duration * 1000));
            } catch (InterruptedException ignored) {}

            for (Ball ball : world.getBalls()) {
                ball.setSpeedMultiplier(1.0);
            }
        }).start();
    }
}