package entities.powerups;

import core.World;
import entities.Paddle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.geometry.Rectangle2D;

/**
 * PowerUp: Shrink Paddle — làm paddle nhỏ đi.
 */
public class ShrinkPaddle extends PowerUp {

    private final double ShrinkFactor = 0.8;
    private final double duration = 8.0; // giây

    public ShrinkPaddle(double x, double y) {
        super(x, y, 18, 18, Color.LIGHTBLUE);
        image = new Image(getClass().getResource("/images/ShrinkPaddle.png").toExternalForm());

    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(x - width / 2, y - height / 2, width, height);
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!isActive()) return;

        if (image.isError()) {
            System.out.println("Không thể tải ảnh: /images/ShrinkPaddle.png");
            return;
        }

        double drawX = getX() - getWidth() / 2;
        double drawY = getY() - getHeight() / 2;

        gc.drawImage(image, drawX, drawY, getWidth(), getHeight());
    }

    @Override
    public void onCollected(World world) {
        //kiem tra xem trang thai bong truoc
        boolean hasFlyingBall = world.getBalls().stream().anyMatch(ball -> !ball.isStickToPaddle());
        if (!hasFlyingBall) return;

        Paddle paddle = world.getPaddle();
        double originalWidth = paddle.getWidth();
        paddle.setWidth(originalWidth * ShrinkFactor);

        // Sau "duration" giây, trả paddle về kích thước cũ
        new Thread(() -> {
            try {
                Thread.sleep((long) (duration * 1000));
            } catch (InterruptedException ignored) {}
            paddle.setWidth(originalWidth);
        }).start();
    }
}