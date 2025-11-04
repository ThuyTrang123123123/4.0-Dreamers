package entities.powerups;

import core.World;
import entities.Ball;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * PowerUp: Double Ball — Tạo thêm 1 quả bóng mới.
 */
public class DoubleBall extends PowerUp {

    public DoubleBall(double x, double y) {
        super(x, y, 18, 18, Color.GOLD);
        image = new Image(getClass().getResource("/images/DoubleBall.png").toExternalForm());
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(x - width / 2, y - height / 2, width, height);
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!isActive()) return;
        if (image.isError()) {
            System.out.println("Không thể tải ảnh: /images/DoubleBall.png");
            return;
        }

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
        }
        world.getBalls().addAll(newBalls);
    }
}