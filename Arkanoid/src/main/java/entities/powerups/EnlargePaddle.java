package entities.powerups;

import core.World;
import entities.Paddle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.geometry.Rectangle2D;

/**
 * PowerUp loại: Enlarge Paddle — làm paddle to hơn trong vài giây.
 */
public class EnlargePaddle extends PowerUp {

    private final double enlargeFactor = 1.5;
    private final double duration = 8.0; // giây

    public EnlargePaddle(double x, double y) {
        super(x, y, 18, 18, Color.LIGHTBLUE);
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!isActive()) return;
        gc.setFill(Color.LIGHTBLUE);
        gc.fillOval(getX(), getY(), getWidth(), getHeight());
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
        double originalWidth = paddle.getWidth();
        paddle.setWidth(originalWidth * enlargeFactor);

        new Thread(() -> {
            try {
                Thread.sleep((long) (duration * 1000));
            } catch (InterruptedException ignored) {}
            paddle.setWidth(originalWidth);
        }).start();



    }
}