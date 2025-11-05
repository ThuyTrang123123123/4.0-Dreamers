package entities.powerups;

import core.Config;
import core.World;
import entities.Ball;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class EnLargeBall extends PowerUp {
    private double EnLargeFactor = 1.5;
    private final double duration = 8.0;
    private Image image;

    public EnLargeBall(double x, double y) {
        super(x, y, Config.POWERUP_WIDTH, Config.POWERUP_HEIGHT, Color.ORANGE);
        try {
            image = new Image(getClass().getResource("/images/ELB.png").toExternalForm());
        } catch (Exception e) {
            System.err.println("Không thể tạo ảnh EnLargeBall: " + e.getMessage());
        }
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(x - width / 2, y - height / 2, width, height);
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!isActive()) return;

        try {
            if (image == null) {
                System.out.println("tai anh sai");
                gc.setFill(color);
                gc.fillOval(x - width / 2, y - height / 2, width, height);
                return;
            }
            gc.drawImage(image, x - width / 2, y - height / 2, width, height);
        } catch (Exception e) {
            System.err.println("Lỗi khi vẽ EnLargeBall: " + e.getMessage());
        }
    }

    @Override
    public void onCollected(World world) {
        try {
            boolean hasFlyingBall = world.getBalls().stream().anyMatch(b -> !b.isStickToPaddle());
            if (!hasFlyingBall) return;

            for (Ball ball : world.getBalls()) {
                ball.setRadius(Config.BALL_RADIUS * EnLargeFactor);
            }
            System.out.println("Đã làm to bóng!");
        } catch (Exception e) {
            System.err.println("Lỗi khi làm to bóng: " + e.getMessage());
        }

        new Thread(() -> {
            try {
                Thread.sleep((long) (duration * 1000));
                for (Ball ball : world.getBalls()) {
                    ball.setRadius(Config.BALL_RADIUS);
                }
            } catch (InterruptedException e) {
                System.err.println("Lỗi khi chờ hoàn tác EnLargeBall: " + e.getMessage());
            }
        }).start();
    }
}