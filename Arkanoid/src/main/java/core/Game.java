package core;

import engine.Collision;
import engine.GameLoop;
import entities.Ball;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import ui.screen.InGame;
import ui.screen.MainMenu;
import ui.theme.Colors;
import ui.theme.Fonts;

/**
 * Game - Class chính điều khiển toàn bộ game
 * Kế thừa từ JavaFX Application để chạy được GUI
 * Chứa game loop (update + render)
 */
public class Game extends Application {
    private Canvas canvas;                    // Nơi vẽ game
    private GraphicsContext gc;               // Công cụ để vẽ
    private final World world = new World();  // Thế giới game (chứa paddle, ball, bricks)
    private InGame hudLayer;                  // Giao diện HUD (điểm, mạng)
    private boolean gamePaused = false;       // Trạng thái tạm dừng

    public Scene createGamescene(Stage stage) {
        // === Khởi tạo Canvas để vẽ ===
        canvas = new Canvas(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
        gc = canvas.getGraphicsContext2D();

        // === Khởi tạo world (paddle, ball, bricks) ===
        world.init(canvas);

        // === Tạo HUD (hiển thị điểm, mạng) ===
        hudLayer = new InGame(world.getScoring());  // Truyền ScoringSystem vào để bind dữ liệu
        HBox hud = hudLayer.createHUD();

        // === Ghép Canvas và HUD thành 1 StackPane ===
        StackPane root = new StackPane(canvas, hud);
        StackPane.setAlignment(hud, Pos.TOP_LEFT);  // HUD nằm góc trên trái

        // === Tạo Scene và xử lý input ===
        Scene scene = new Scene(root, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);

        // Xử lý khi nhấn phím
        scene.setOnKeyPressed(e -> {
            // ESC: Pause và quay về menu
            if (e.getCode() == KeyCode.ESCAPE) {
                gamePaused = true;
                stage.setScene(new MainMenu().create(stage));
            }

            // R: Restart game (chỉ khi game over hoặc thắng)
            if (e.getCode() == KeyCode.R &&
                    (world.getBall().isLost() || world.getScoring().isGameOver())) {
                restartGame();
            }

            // Truyền input xuống paddle để di chuyển
            world.getPaddle().onKeyPressed(e.getCode());
        });

        // Xử lý khi thả phím
        scene.setOnKeyReleased(e -> world.getPaddle().onKeyReleased(e.getCode()));

        // === Khởi động game loop ===
        new GameLoop(this).start();

        return scene;
    }

    public void update(double dt) {
        // Nếu game đang pause thì không update
        if (gamePaused) return;

        Ball ball = world.getBall();

        // ===== Xử lý khi bóng rơi xuống đáy =====
        if (ball.isLost()) {
            world.getScoring().loseLife();  // Trừ 1 mạng

            // Kiểm tra game over (hết mạng)
            if (world.getScoring().isGameOver()) {
                return; // Dừng update, chỉ còn render "GAME OVER"
            } else {
                // Còn mạng → Reset bóng về vị trí ban đầu
                ball.setX(Config.SCREEN_WIDTH / 2.0);
                ball.setY(Config.SCREEN_HEIGHT - 70);
                ball.setVelocityX(Config.BALL_SPEED);
                ball.setVelocityY(-Config.BALL_SPEED);  // bay lên
                ball.setLost(false);
            }
        }

        // ===== Update vị trí các đối tượng =====
        world.getPaddle().update(dt);  // Di chuyển paddle theo input
        ball.update(dt);                // Di chuyển bóng theo vận tốc

        // ===== Kiểm tra va chạm =====
        Collision.checkWallCollision(ball, canvas.getWidth(), canvas.getHeight());
        Collision.checkPaddleCollision(ball, world.getPaddle());
        Collision.checkBrickCollision(ball, world.getBricks(), world);

        // ===== Kiểm tra điều kiện thắng level =====
        // Nếu tất cả gạch đã bị phá → Chuyển level mới
        if (world.getLevel().isComplete()) {
            world.nextLevel();  // Chuyển sang level tiếp theo (giữ nguyên điểm)
        }
    }

    /**
     * Render - Vẽ tất cả mọi thứ lên màn hình
     * Được gọi liên tục sau update() trong GameLoop
     */
    public void render() {
        // === Vẽ nền ===
        gc.setFill(Colors.PRIMARY);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // === Vẽ thông tin level ở góc trên phải ===
        gc.setFill(Colors.TEXT);
        gc.setFont(Fonts.main(20));
        gc.fillText("Level " + world.getLevel().getCurrentLevel(),
                Config.SCREEN_WIDTH - 120, 30);

        // === Vẽ các đối tượng game ===
        world.getPaddle().render(gc);  // Vẽ thanh chắn
        world.getBall().render(gc);    // Vẽ bóng

        // Chỉ vẽ những viên gạch chưa bị phá (filter destroyed)
        world.getBricks().stream()
                .filter(b -> !b.isDestroyed())
                .forEach(b -> b.render(gc));

        // === Vẽ thông báo Game Over ===
        if (world.getScoring().isGameOver()) {
            gc.setFill(Colors.TEXT);
            gc.setFont(Fonts.main(28));
            gc.fillText("GAME OVER", 320, 280);
            gc.setFont(Fonts.main(16));
            gc.fillText("Press R to Restart", 320, 320);
        }
    }

    /**
     * Restart toàn bộ game về trạng thái ban đầu
     * Được gọi khi người chơi nhấn phím R
     */
    private void restartGame() {
        world.reset();           // Reset world (ball, paddle, bricks, scoring)
        gamePaused = false;      // Bỏ pause để game chạy lại
    }

    /**
     * Getter cho Canvas (dùng trong GameLoop)
     */
    public Canvas getCanvas() {
        return canvas;
    }

    /**
     * start() - Entry point của JavaFX Application
     * Được gọi đầu tiên khi chạy game
     *
     * @param stage Cửa sổ chính
     */
    @Override
    public void start(Stage stage) {
        stage.setTitle("Arkanoid");
        stage.setScene(new MainMenu().create(stage));  // Bắt đầu ở màn hình menu
        stage.show();
    }
}