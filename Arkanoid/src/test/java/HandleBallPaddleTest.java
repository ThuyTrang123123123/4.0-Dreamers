import entities.Ball;
import entities.Paddle;
import engine.Collision;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
/**
 *   public static void handleBallPaddleCollision(Ball ball, Paddle paddle) {
 *         ball.setY(paddle.getY() - paddle.getHeight() / 2 - ball.getRadius());
 *         ball.reverseY();
 *         double hitPos = (ball.getX() + ball.getDiameter() / 2) - (paddle.getX() + paddle.getWidth() / 2);
 *         ball.setVelocityX(hitPos * 2);
 *     }
 */
public class HandleBallPaddleTest {

    @Test
    public void testBallYPositionAfterCollision() {
        Ball ball = new Ball(100, 200, 10, 5); // bóng đang đi xuống
        Paddle paddle = new Paddle(90, 220, 60, 20); // nằm dưới bóng

        Collision.handleBallPaddleCollision(ball, paddle);

        double expectedY = paddle.getY() - paddle.getHeight() / 2 - ball.getRadius();
        assertEquals(expectedY, ball.getY(), "Ball không được đặt lại đúng vị trí Y sau va chạm với paddle");
    }

    @Test
    public void testBallReversesYDirection() {
        Ball ball = new Ball(100, 200, 10, 5); // bóng đang đi xuống
        Paddle paddle = new Paddle(90, 220, 60, 20);

        Collision.handleBallPaddleCollision(ball, paddle);

        assertEquals(5, ball.getVelocityY(), "Ball không đảo hướng Y sau va chạm với paddle");
    }

    @Test
    public void testBallVelocityXBasedOnHitPosition() {
        Ball ball = new Ball(120, 200, 10, -5); // bóng lệch phải
        Paddle paddle = new Paddle(90, 220, 60, 20);

        Collision.handleBallPaddleCollision(ball, paddle);

        double ballCenter = ball.getX() + ball.getDiameter() / 2;
        double paddleCenter = paddle.getX() + paddle.getWidth() / 2;
        double expectedVelocityX = (ballCenter - paddleCenter) * 2;

        assertEquals(expectedVelocityX, ball.getVelocityX(), "Ball không tính đúng vận tốc X sau va chạm với paddle");
    }
}