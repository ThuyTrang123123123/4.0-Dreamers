package core;

import entities.Ball;
import entities.Paddle;
import entities.bricks.Brick;
import javafx.scene.canvas.Canvas;
import java.util.List;

public class World {
    private Paddle paddle;
    private Ball ball;
    private List<Brick> bricks;

    public void init(Canvas canvas) {
        paddle = new Paddle(
                (Config.SCREEN_WIDTH - Config.PADDLE_WIDTH) / 2,
                Config.SCREEN_HEIGHT - 50,
                Config.PADDLE_WIDTH,
                Config.PADDLE_HEIGHT
        );
        ball = new Ball(
                (Config.SCREEN_WIDTH - Config.BALL_RADIUS * 2) / 2,
                Config.SCREEN_HEIGHT - 70,
                Config.BALL_RADIUS,
                Config.BALL_SPEED
        );

        Level level = new Level(Config.BRICK_ROWS, Config.BRICK_COLS);
        bricks = level.getBricks();
    }

    public Paddle getPaddle() { return paddle; }
    public Ball getBall() { return ball; }
    public List<Brick> getBricks() { return bricks; }
}
