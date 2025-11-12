package entities.powerups;

import core.World;
import core.Config;

import entities.Ball;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;


/**
 * PowerUp: Double Ball — Tạo thêm 1 quả bóng mới.
 */
public class DoubleBall extends PowerUp {

    private final double duration = 8.0;

    public DoubleBall(double x, double y) {
        super(x, y, Config.POWERUP_WIDTH, Config.POWERUP_HEIGHT, Color.GOLD);
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
                image = new Image(getClass().getResource("/images/DoubleBall.png").toExternalForm());
            }
        } catch (Exception e) {
            System.err.println("Không thể tải ảnh DoubleBall: " + e.getMessage());
            image = null;
        }

        double drawX = getX() - getWidth() / 2;
        double drawY = getY() - getHeight() / 2;

        gc.drawImage(image, x - width / 2, y - height / 2, width, height);
    }

    @Override
    public void onCollected(World world) {
        var newBalls = new java.util.ArrayList<Ball>();
        for (Ball ball : world.getBalls()) {
            if (ball.isStickToPaddle()) continue; // bỏ qua bóng đang gắn paddle
            // Tạo 1 bóng mới sao chép trạng thái
            Ball clone = new Ball(ball.getX(), ball.getY(), ball.getRadius(),
                    Math.sqrt(ball.getVelocityX() * ball.getVelocityX() + ball.getVelocityY() * ball.getVelocityY()));
            clone.setVelocityX(-ball.getVelocityX()); // đi ngược hướng
            clone.setVelocityY(ball.getVelocityY());
            clone.setStickToPaddle(false);
            newBalls.add(clone);

            new Thread(() -> {
                try {
                    Thread.sleep((long) (duration * 1000));
                    Platform.runLater(() -> world.getBalls().remove(clone));
                } catch (InterruptedException ignored) {
                }
            }).start();
        }
        world.getBalls().addAll(newBalls);
    }
}