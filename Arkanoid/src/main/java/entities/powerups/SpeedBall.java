package entities.powerups;

import core.World;
import entities.Ball;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * PowerUp: Speed Ball — Tăng tốc độ bóng.
 */
public class SpeedBall extends PowerUp {
    private final double speedFactor =1.5 ; // tăng tốc
    private final double duration = 8.0;
    private final Image image;

    public SpeedBall(double x, double y) {
        super(x, y, 18, 18, Color.ORANGE);
        image = new Image(getClass().getResource("/images/SpeedBall.png").toExternalForm());
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(x - width / 2, y - height / 2, width, height);
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!isActive()) return;
        gc.drawImage(image, x - width / 2, y - height / 2, width, height);
    }

    @Override
    public void onCollected(World world) {
        boolean hasFlyingBall = world.getBalls().stream().anyMatch(ball -> !ball.isStickToPaddle());
        if (!hasFlyingBall) return;

        for (Ball ball : world.getBalls()) {
            ball.setSpeedMultiplier(speedFactor);
        }

        new Thread(() -> {
            try {
                Thread.sleep((long) (duration * 1000));
            } catch (InterruptedException ignored) {}

            Platform.runLater(() -> {
                for (Ball ball : world.getBalls()) {
                    ball.setSpeedMultiplier(1.0);
                }
            });
        }).start();
    }

}
