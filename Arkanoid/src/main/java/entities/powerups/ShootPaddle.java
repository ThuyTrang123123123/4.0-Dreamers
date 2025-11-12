package entities.powerups;

import core.Config;
import core.World;
import entities.Paddle;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * PowerUp: ShootPaddle - bắn 1 viên đạn từ Paddle.
 */

public class ShootPaddle extends PowerUp {

    private final double duration = 10.0; // 10 giây

    public ShootPaddle(double x, double y) {
        super(x, y, Config.POWERUP_WIDTH, Config.POWERUP_HEIGHT, Color.RED);
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(x - width / 2, y - height / 2, width, height);
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!isActive()) return;
        try {
            if (image == null && Platform.isFxApplicationThread()) {
                image = new Image(getClass().getResource("/images/ShootPaddle.png").toExternalForm());
            }
        } catch (Exception e) {
            System.err.println("Không thể tải ảnh ShootPaddle: " + e.getMessage());
            image = null;
        }
        double drawX = getX() - getWidth() / 2;
        double drawY = getY() - getHeight() / 2;
        gc.drawImage(image, x - width / 2, y - height / 2, width, height);
    }

    @Override
    public void onCollected(World world) {
        Paddle paddle = world.getPaddle();

        this.active = false;

        if (!paddle.isShooting()) {
            paddle.setShooting(true);

            new Thread(() -> {
                try {
                    while (System.currentTimeMillis() < paddle.getShootEndTime()) {
                        boolean hasFlyingBall = world.getBalls().stream().anyMatch(b -> !b.isStickToPaddle());
                        if (!hasFlyingBall) break;
                        Thread.sleep(200);
                    }
                    paddle.setShooting(false);
                } catch (InterruptedException ignored) {
                }
            }).start();
        }

        paddle.setShootEndTime(System.currentTimeMillis() + (long) (duration * 1000));


    }
}