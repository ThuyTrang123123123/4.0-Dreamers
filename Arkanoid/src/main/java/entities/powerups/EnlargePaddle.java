package entities.powerups;

import core.World;
import core.Config;
import entities.Paddle;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.geometry.Rectangle2D;

/**
 * EnlargePaddle – Mở rộng thanh đỡ bóng, tăng khả năng phòng thủ.
 */

public class EnlargePaddle extends PowerUp {

    private final double enlargeFactor = 1.5;
    private final double duration = 8.0; // giây

    public double getEnlargeFactor() {
        return enlargeFactor;
    }

    public EnlargePaddle(double x, double y) {
        super(x, y, Config.POWERUP_WIDTH, Config.POWERUP_HEIGHT, Color.LIGHTBLUE);
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!isActive()) return;
        try {
            if (image == null && Platform.isFxApplicationThread()) {
                image = new Image(getClass().getResource("/images/EnlargePaddle.png").toExternalForm());
            }
        } catch (Exception e) {
            System.err.println("Không thể tải ảnh EnlargePaddle: " + e.getMessage());
            image = null;
        }
        double drawX = getX() - getWidth() / 2;
        double drawY = getY() - getHeight() / 2;

        gc.drawImage(image, drawX, drawY, getWidth(), getHeight());
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(x - width / 2, y - height / 2, width, height);
    }

    @Override
    public void onCollected(World world) {
        boolean hasFlyingBall = world.getBalls().stream().anyMatch(ball -> !ball.isStickToPaddle());
        if (!hasFlyingBall) return;

        Paddle paddle = world.getPaddle();
        paddle.applyScale(enlargeFactor);

        new Thread(() -> {
            try {
                Thread.sleep((long) (duration * 1000));
            } catch (InterruptedException ignored) {
            }
            paddle.removeScale(enlargeFactor);
        }).start();
    }
}