package core;

import engine.*;
import entities.Ball;
import entities.Paddle;
import entities.bricks.Brick;
import entities.powerups.BonusCoin;
import entities.powerups.EnlargePaddle;
import entities.powerups.ExtraLife;
import entities.powerups.PowerUp;
import systems.ScoringSystem;
import systems.AchievementSystem;
import javafx.scene.canvas.Canvas;

import java.util.ArrayList;
import java.util.List;

public class World {
    private Paddle paddle;
    private Ball ball;
    private final List<PowerUp> powerUps = new ArrayList<>();
    private final PowerUpPool powerUpPool = new PowerUpPool();
    private Level level;
    private final ScoringSystem scoring;
    private final AchievementSystem achievements;

    public World() {
        this.scoring = new ScoringSystem();
        this.level = new Level(Config.BRICK_ROWS, Config.BRICK_COLS);
        this.achievements = new AchievementSystem();
    }

    public void init(Canvas canvas) {
        paddle = new Paddle(
                Config.SCREEN_WIDTH / 2.0,
                Config.SCREEN_HEIGHT - 50,
                Config.PADDLE_WIDTH,
                Config.PADDLE_HEIGHT
        );

        ball = new Ball(
                Config.SCREEN_WIDTH / 2.0,
                Config.SCREEN_HEIGHT - 70,
                Config.BALL_RADIUS,
                Config.BALL_SPEED
        );

        powerUps.clear(); // danh sách trống, không sinh sẵn
        ball.setStickToPaddle(true);
    }

    public void reset() {
        ball.setX(Config.SCREEN_WIDTH / 2.0);
        ball.setY(Config.SCREEN_HEIGHT - 70);
        ball.setVelocityX(Config.BALL_SPEED);
        ball.setVelocityY(-Config.BALL_SPEED);
        ball.setLost(false);

        paddle.setX(Config.SCREEN_WIDTH / 2.0);
        ball.resetToStick(paddle.getX(), paddle.getY());

        // Thu hồi tất cả PowerUp đang dùng
        for (PowerUp pu : powerUps) {
            powerUpPool.release(pu);
        }
        powerUps.clear();

        level.reset();
        scoring.reset();
        achievements.resetAll();
    }

    public void nextLevel() {
        level.regenerate();
        // Thu hồi tất cả PowerUp đang dùng
        for (PowerUp pu : powerUps) {
            powerUpPool.release(pu);
        }
        powerUps.clear();
        ball.setX(Config.SCREEN_WIDTH / 2.0);
        ball.resetToStick(paddle.getX(), paddle.getY());
        ball.setY(Config.SCREEN_HEIGHT - 70);
        ball.setVelocityX(Config.BALL_SPEED);
        ball.setVelocityY(-Config.BALL_SPEED);
        ball.setLost(false);

        paddle.setX(Config.SCREEN_WIDTH / 2.0);
    }

    // Getters
    public Paddle getPaddle() { return paddle; }
    public Ball getBall() { return ball; }
    public List<PowerUp> getPowerUps() { return powerUps; }
    public List<Brick> getBricks() { return level.getBricks(); }
    public ScoringSystem getScoring() { return scoring; }
    public Level getLevel() { return level; }
    public AchievementSystem getAchievements() { return achievements; }
    public PowerUpPool getPowerUpPool() { return powerUpPool; }
}