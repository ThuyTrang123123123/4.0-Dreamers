import core.World;
import entities.Ball;
import entities.Paddle;
import entities.powerups.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PowerupEffectTest {
    //test_bonuscoin
    @Test
    public void testBonusCoin1() {
        World world = new World();
        Ball ball = new Ball(100, 100, 10, 5);
        ball.setStickToPaddle(false); // bóng đang bay
        world.getBalls().add(ball);
        BonusCoin coin = new BonusCoin(100, 100, 10, 100);
        coin.setValue(15);
        coin.onCollected(world);
        assertEquals(15, world.getScoring().getScore(), "Lỗi: không cộng điểm khi bóng đang bay");
    }

    @Test
    public void testBonusCoin2() {
        World world = new World();
        Ball ball = new Ball(100, 100, 10, 5);
        ball.setStickToPaddle(true); // bóng đang bay
        world.getBalls().add(ball);
        BonusCoin coin = new BonusCoin(100, 100, 10, 100);
        coin.setValue(15);
        coin.onCollected(world);
        assertEquals(0, world.getScoring().getScore(), "Lỗi: cộng điểm khi bóng đang ở paddle");
    }

    //test-doubleBall
    @Test
    public void testDoubleBall1() {
        World world = new World();
        Ball ball = new Ball(100, 100, 10, 5);
        ball.setStickToPaddle(false);// đang bay
        world.getBalls().add(ball);
        PowerUp doubleBall = new DoubleBall(100, 100);
        doubleBall.onCollected(world);
        assertEquals(2, world.getBalls().size(), "Không tạo bóng clone đúng số lượng");
    }

    @Test
    public void testDoubleBall2() {
        World world = new World();
        Ball ball = new Ball(100, 100, 10, 5);
        ball.setStickToPaddle(true);// đang bay
        world.getBalls().add(ball);
        PowerUp doubleBall = new DoubleBall(100, 100);
        doubleBall.onCollected(world);
        assertEquals(1, world.getBalls().size(), "Không tạo bóng clone đúng số lượng");
    }

    //Enlarge Ball
    @Test
    public void testEnlargeBall1() {
        World world = new World();
        Ball flyingBall = new Ball(100, 100, 10, 5);
        flyingBall.setStickToPaddle(false);// đang bay
        world.getBalls().add(flyingBall);
        EnLargeBall enLargeBall = new EnLargeBall(100, 100);
        enLargeBall.onCollected(world);
        double x = enLargeBall.getEnLargeFactor();
        assertEquals(10 * x, flyingBall.getRadius(), "Lỗi :ko làm to bóng");
    }

    @Test
    public void testEnlargeBall2() {
        World world = new World();
        Ball flyingBall = new Ball(100, 100, 10, 5);
        flyingBall.setStickToPaddle(true);// đang bay
        world.getBalls().add(flyingBall);
        EnLargeBall enLargeBall = new EnLargeBall(100, 100);
        enLargeBall.onCollected(world);
        double x = enLargeBall.getEnLargeFactor();
        assertEquals(10, flyingBall.getRadius(), "Lỗi làm to bóng khi bóng ở paddle");
    }

    //Enlarge Paddle
    @Test
    public void testEnlargePaddle1() {
        World world = new World();
        Ball ball = new Ball(100, 100, 10, 5);
        Paddle paddle = new Paddle(90, 220, 60, 20);
        ball.setStickToPaddle(false); // bóng đang bay
        world.getBalls().add(ball);
        world.setPaddle(paddle);
        double originalWidth = paddle.getWidth();

        EnlargePaddle powerUp = new EnlargePaddle(100, 100);
        powerUp.onCollected(world);
        assertEquals(originalWidth * powerUp.getEnlargeFactor(), paddle.getWidth(), "Không làm to paddle khi bóng đang bay");
    }

    @Test
    public void testEnlargePaddle2() {
        World world = new World();
        Ball ball = new Ball(100, 100, 10, 5);
        Paddle paddle = new Paddle(90, 220, 60, 20);
        ball.setStickToPaddle(true); // bóng đang bay
        world.getBalls().add(ball);
        world.setPaddle(paddle);
        double originalWidth = paddle.getWidth();

        EnlargePaddle powerUp = new EnlargePaddle(100, 100);
        powerUp.onCollected(world);
        assertEquals(originalWidth, paddle.getWidth(), "Lỗi làm to paddle khi bóng đang ở Paddle");
    }

    //ShrinkPadle
    @Test
    public void testShinkPaddle1() {
        World world = new World();
        Ball ball = new Ball(100, 100, 10, 5);
        Paddle paddle = new Paddle(90, 220, 60, 20);
        ball.setStickToPaddle(false); // bóng đang bay
        world.getBalls().add(ball);
        world.setPaddle(paddle);
        double originalWidth = paddle.getWidth();

        ShrinkPaddle powerUp = new ShrinkPaddle(100, 100);
        powerUp.onCollected(world);
        assertEquals(originalWidth * powerUp.getShrinkFactor(), paddle.getWidth(),
                "Lỗi ko làm nhỏ paddle khi bóng bay");
    }

    public void testShinkPaddle2() {
        World world = new World();
        Ball ball = new Ball(100, 100, 10, 5);
        Paddle paddle = new Paddle(90, 220, 60, 20);
        ball.setStickToPaddle(true); // bóng đang bay
        world.getBalls().add(ball);
        world.setPaddle(paddle);
        double originalWidth = paddle.getWidth();

        ShrinkPaddle powerUp = new ShrinkPaddle(100, 100);
        powerUp.onCollected(world);
        assertEquals(originalWidth, paddle.getWidth(),
                "Lỗi làm nhỏ paddle khi bóng đang ở Paddle");
    }

    @Test
    public void testSlowBall() {
        World world = new World();
        Ball ball = new Ball(100, 100, 10, 5);
        ball.setStickToPaddle(false); // bóng đang bay
        world.getBalls().add(ball);

        SlowBall powerUp = new SlowBall(100, 100);
        powerUp.onCollected(world);
        double x = powerUp.getSlowFactor();
        assertEquals(powerUp.getSlowFactor(), ball.getSpeedMultiplier(),
                "Lỗi:Không làm chậm bóng khi đang bay");
    }

    @Test
    public void testSpeedBall() {
        World world = new World();
        Ball ball = new Ball(100, 100, 10, 5);
        ball.setStickToPaddle(false); // bóng đang bay
        world.getBalls().add(ball);

        SpeedBall powerUp = new SpeedBall(100, 100);
        powerUp.onCollected(world);
        double x = powerUp.getSpeedFactor();
        assertEquals(powerUp.getSpeedFactor(), ball.getSpeedMultiplier(),
                "Lỗi Không làm chậm bóng khi đang bay");
    }

    @Test
    public void testExtraLife() {
        World world = new World();

        Ball ball = new Ball(100, 100, 10, 5);
        ball.setStickToPaddle(false); // bóng đang bay
        world.getBalls().add(ball);

        int initialLives = world.getScoring().getLives();

        ExtraLife powerUp = new ExtraLife(100, 100);
        powerUp.onCollected(world);

        assertEquals(initialLives + 1, world.getScoring().getLives(),
                "Lỗi: Không cộng thêm mạng khi bóng đang bay");
    }

    @Test
    public void ShootPaddleTest() {
        Paddle paddle = new Paddle(90, 220, 60, 20);; // giả sử có constructor phù hợp
        World world = new World();
        world.setPaddle(paddle);
        ShootPaddle shootPaddle = new ShootPaddle(90, 220);
        shootPaddle.onCollected(world);
        assertTrue(paddle.isShooting(), "Lỗi:Paddle ko bật shooting");
    }


}
