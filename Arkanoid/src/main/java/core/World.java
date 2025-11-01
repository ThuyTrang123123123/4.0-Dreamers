package core;

import entities.Ball;
import entities.Paddle;
import entities.bricks.Brick;
import systems.ScoringSystem;
import systems.AchievementSystem;
import javafx.scene.canvas.Canvas;
import java.util.List;

/**
 * World - Thế giới game
 * Quản lý tất cả các đối tượng trong game
 */
public class World {
    private Paddle paddle;
    private Ball ball;
    private Level level;
    private final ScoringSystem scoring;
    private final AchievementSystem achievements;

    /**
     * Constructor - Khởi tạo World
     */
    public World() {
        this.scoring = new ScoringSystem();
        this.level = new Level(Config.BRICK_ROWS, Config.BRICK_COLS);
        this.achievements = new AchievementSystem();
    }

    /**
     * Khởi tạo tất cả đối tượng trong world
     */
    public void init(Canvas canvas) {
        // Tạo Paddle ở giữa đáy màn hình
        paddle = new Paddle(
                Config.SCREEN_WIDTH / 2.0,
                Config.SCREEN_HEIGHT - 50,
                Config.PADDLE_WIDTH,
                Config.PADDLE_HEIGHT
        );

        // Tạo Ball ngay trên paddle
        ball = new Ball(
                Config.SCREEN_WIDTH / 2.0,
                Config.SCREEN_HEIGHT - 70,
                Config.BALL_RADIUS,
                Config.BALL_SPEED
        );
    }

    /**
     * Reset world về trạng thái ban đầu
     */
    public void reset() {
        // Reset vị trí paddle
        paddle.setX(Config.SCREEN_WIDTH / 2.0);

        // ⭐ Reset bóng về trạng thái stick trên paddle
        ball.resetToStick(paddle.getX(), paddle.getY());

        // Reset về level 1
        level.reset();

        // Reset điểm số, mạng
        scoring.reset();

        // Reset thành tựu và rank
        achievements.resetAll();
    }

    /**
     * Chuyển sang level tiếp theo
     * ⭐ QUAN TRỌNG: Điểm số và mạng KHÔNG bị reset
     * ⭐ Bóng sẽ dính trên paddle, chờ nhấn SPACE để bắn
     */
    public void nextLevel() {
        // Tạo lại gạch mới cho level tiếp theo
        level.regenerate();

        // Reset vị trí paddle về giữa
        paddle.setX(Config.SCREEN_WIDTH / 2.0);

        // ⭐ Reset bóng về trạng thái STICK trên paddle
        // Bóng sẽ dính và KHÔNG tự động bay
        ball.resetToStick(paddle.getX(), paddle.getY());

        // Kiểm tra thành tựu level mới
        achievements.checkAchievements(
                scoring,
                level.getCurrentLevel()
        );

        System.out.println("🎯 Level " + level.getCurrentLevel() + " - Press SPACE to launch!");
    }

    // ===== Getters =====

    public Paddle getPaddle() { return paddle; }
    public Ball getBall() { return ball; }
    public List<Brick> getBricks() { return level.getBricks(); }
    public ScoringSystem getScoring() { return scoring; }
    public Level getLevel() { return level; }
    public AchievementSystem getAchievements() { return achievements; }
}