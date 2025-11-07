import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import core.Config;
import entities.Ball;
import engine.Collision;

public class HandleBallWallTest {
    @Test
    public void testBallHitsLeftWall() {
        Ball ball = new Ball(0, 100, 10, 5); // chạm mép trái
        Collision.handleBallWallCollision(ball, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);

        assertEquals(ball.getRadius(), ball.getX(), "Ball không được đặt lại đúng vị trí sau va chạm trái");
        assertEquals(-5, ball.getVelocityX(), "Ball không đảo hướng vận tốc X sau va chạm trái");
    }

    @Test
    public void testBallHitsRightWall() {
        Ball ball = new Ball(Config.SCREEN_WIDTH, 100, 10, 5); // chạm mép phải
        Collision.handleBallWallCollision(ball, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);

        assertEquals(Config.SCREEN_WIDTH - ball.getRadius(), ball.getX(), "Ball không được đặt lại đúng vị trí sau va chạm phải");
        assertEquals(-5, ball.getVelocityX(), "Ball không đảo hướng X sau va chạm phải");
    }
    @Test
    public void testBallHitsTopWall() {
        Ball ball = new Ball(100, 0, 10, -5); // chạm mép trên
        Collision.handleBallWallCollision(ball, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);

        assertEquals(ball.getRadius(), ball.getY(), "Ball không được đặt lại đúng vị trí sau va chạm trên");
        assertEquals(-5, ball.getVelocityY(), "Ball không đảo hướng Y sau va chạm trên");
    }
    @Test
    public void testBallHitsBottomWall() {
        Ball ball = new Ball(100, Config.SCREEN_HEIGHT, 10, 5); // chạm mép dưới
        Collision.handleBallWallCollision(ball,Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
        assertTrue(ball.isLost(), "Ball không bị đánh dấu là mất sau va chạm dưới");
    }
}
