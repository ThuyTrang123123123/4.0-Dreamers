package entities.powerups;

import core.World;
import entities.Paddle;
import javafx.scene.paint.Color;

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
    public void onCollected(World world) {
        Paddle paddle = world.getPaddle();
        double originalWidth = paddle.getWidth();
        paddle.setWidth(originalWidth * enlargeFactor);

        // Sau "duration" giây, trả paddle về kích thước cũ
        new Thread(() -> {
            try {
                Thread.sleep((long) (duration * 1000));
            } catch (InterruptedException ignored) {}
            paddle.setWidth(originalWidth);
        }).start();
    }
}
