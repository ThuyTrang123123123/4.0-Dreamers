import entities.Ball;
import entities.Bullet;
import entities.Paddle;
import entities.bricks.Brick;
import engine.Collision;
import entities.bricks.NormalBrick;
import entities.powerups.EnlargePaddle;
import entities.powerups.PowerUp;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CollisionTest {
    @Test
    public void testBallTouchesWall() {
        Ball ball = new Ball(0, 50, 10, 0);
        boolean result = Collision.isBallTouchingWall(ball, 800, 600);
        assertTrue(result, "Ball không phát hiện va chạm với tường");
    }
    @Test
    public void testBallTouchesBrick() {
        Ball ball = new Ball(100, 110, 10, 0); // mép phải của gạch
        Brick brick = new NormalBrick(100, 100, 50, 20);
        boolean result = Collision.isBallTouchingBrick(ball, brick);
        assertTrue(result, "Lỗi va chạm Ball, Brick");
    }
    @Test
    public void testBallTouchesPaddle() {
        Ball ball = new Ball(100, 100, 10, 0);
        Paddle paddle = new Paddle(90, 100, 60, 20); // nằm dưới bóng
        boolean result = Collision.isBallTouchingPaddle(ball, paddle);
        assertTrue(result, "Ball không phát hiện va chạm với paddle");
    }
    @Test
    public void testPowerUpTouchesPaddle() {
        Paddle paddle = new Paddle(100, 100, 60, 20);
        EnlargePaddle pu = new EnlargePaddle(100, 100);
        pu.setActive(true);

        boolean result = Collision.isPowerUpTouchingPaddle(pu, paddle);
        assertTrue(result, "Paddle không phát hiện va chạm với PowerUP");
    }
    @Test
    public void testBulletTouchesBrick() {
        Bullet bullet = new Bullet(100, 100);
        Brick brick = new NormalBrick(100, 100, 50, 20);
        boolean result = Collision.isBulletTouchingBrick(bullet, brick);
        assertTrue(result, "Bullet không phát hiện va chạm với brick");
    }
}
