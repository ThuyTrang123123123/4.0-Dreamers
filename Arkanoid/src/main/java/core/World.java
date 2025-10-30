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
 * Quản lý tất cả các đối tượng trong game:
 * - Paddle (thanh chắn)
 * - Ball (bóng)
 * - Level (quản lý level và gạch)
 * - ScoringSystem (hệ thống điểm)
 * - AchievementSystem (hệ thống thành tựu và rank)
 */
public class World {
    private Paddle paddle;                        // Thanh chắn
    private Ball ball;                            // Quả bóng
    private Level level;                          // Quản lý level
    private final ScoringSystem scoring;          // Hệ thống điểm
    private final AchievementSystem achievements; // Hệ thống thành tựu

    /**
     * Constructor - Khởi tạo World
     * Tạo sẵn ScoringSystem, Level và AchievementSystem để sử dụng xuyên suốt game
     */
    public World() {
        this.scoring = new ScoringSystem();
        this.level = new Level(Config.BRICK_ROWS, Config.BRICK_COLS);
        this.achievements = new AchievementSystem();

        // ===== Listener cho thành tựu =====
        achievements.addListener(achievement -> {
            System.out.println("🎉 ACHIEVEMENT UNLOCKED: " + achievement.getName());
        });

        // ===== Listener cho rank up =====
        achievements.addRankListener((oldRank, newRank, score) -> {
            System.out.println("⬆️ " + oldRank.getIcon() + oldRank.getName() +
                    " → " + newRank.getIcon() + newRank.getName());
        });
    }

    /**
     * Khởi tạo tất cả đối tượng trong world
     * Được gọi khi bắt đầu game hoặc chuyển level mới
     *
     * @param canvas Canvas để lấy kích thước màn hình
     */
    public void init(Canvas canvas) {
        // === Tạo Paddle ở giữa đáy màn hình ===
        // Tọa độ (x,y) là TÂM của paddle
        paddle = new Paddle(
                Config.SCREEN_WIDTH / 2.0,           // x: giữa màn hình
                Config.SCREEN_HEIGHT - 50,           // y: cách đáy 50px
                Config.PADDLE_WIDTH,                 // chiều rộng
                Config.PADDLE_HEIGHT                 // chiều cao
        );

        // === Tạo Ball ngay trên paddle ===
        // Tọa độ (x,y) là TÂM của ball
        ball = new Ball(
                Config.SCREEN_WIDTH / 2.0,           // x: giữa màn hình
                Config.SCREEN_HEIGHT - 70,           // y: cách đáy 70px (trên paddle)
                Config.BALL_RADIUS,                  // bán kính
                Config.BALL_SPEED                    // tốc độ
        );

        // ⭐ ĐẢM BẢO bóng ở trạng thái STICK ngay từ đầu
        ball.setStickToPaddle(true);

    }

    /**
     * Reset world về trạng thái ban đầu
     * Được gọi khi người chơi nhấn R để restart toàn bộ game
     */
    public void reset() {
        // === Reset vị trí và vận tốc bóng ===
        ball.setX(Config.SCREEN_WIDTH / 2.0);
        ball.setY(Config.SCREEN_HEIGHT - 70);
        ball.setVelocityX(Config.BALL_SPEED);
        ball.setVelocityY(-Config.BALL_SPEED);     // bay lên trên (âm)
        ball.setLost(false);                        // chưa rơi

        // === Reset vị trí paddle ===
        paddle.setX(Config.SCREEN_WIDTH / 2.0);     // về giữa màn hình
// ⭐ Reset bóng về trạng thái STICK trên paddle
        ball.resetToStick(paddle.getX(), paddle.getY());
        // === Reset về level 1 ===
        level.reset();

        // === Reset điểm số, mạng về ban đầu ===
        scoring.reset();

        // === Reset thành tựu và rank ===
        achievements.resetAll();
    }

    /**
     * Chuyển sang level tiếp theo
     * Được gọi khi người chơi phá hết gạch trong level hiện tại
     * ⭐ QUAN TRỌNG: Điểm số và mạng KHÔNG bị reset
     */
    public void nextLevel() {
        // === Tạo lại gạch mới cho level tiếp theo ===
        level.regenerate();

        // === Reset vị trí bóng và paddle ===
        ball.setX(Config.SCREEN_WIDTH / 2.0);
        ball.resetToStick(paddle.getX(), paddle.getY());
        ball.setY(Config.SCREEN_HEIGHT - 70);
        ball.setVelocityX(Config.BALL_SPEED);
        ball.setVelocityY(-Config.BALL_SPEED);
        ball.setLost(false);

        paddle.setX(Config.SCREEN_WIDTH / 2.0);

        // ⚠️ LƯU Ý: KHÔNG gọi scoring.reset() → giữ nguyên điểm và mạng
    }

    // ===== Getters - Cho phép các class khác truy cập =====

    /** Lấy thanh chắn */
    public Paddle getPaddle() { return paddle; }

    /** Lấy quả bóng */
    public Ball getBall() { return ball; }

    /** Lấy danh sách gạch từ Level */
    public List<Brick> getBricks() { return level.getBricks(); }

    /** Lấy hệ thống điểm */
    public ScoringSystem getScoring() { return scoring; }

    /** Lấy Level */
    public Level getLevel() { return level; }

    /** Lấy hệ thống thành tựu */
    public AchievementSystem getAchievements() { return achievements; }
}