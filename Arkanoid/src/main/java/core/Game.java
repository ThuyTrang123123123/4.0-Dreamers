package core;

import engine.Collision;
import engine.GameLoop;
import entities.Ball;
import systems.AudioSystem;

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
 */
public class Game extends Application {
    private Canvas canvas;
    private GraphicsContext gc;
    private final World world = new World();
    private InGame hudLayer;
    private boolean gamePaused = false;

    /**
     * Tạo scene gameplay (màn chơi chính)
     */
    public Scene createGamescene(Stage stage) {
        // === Khởi tạo Canvas ===
        canvas = new Canvas(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
        gc = canvas.getGraphicsContext2D();

        // === Khởi tạo world ===
        world.init(canvas);

        // === Tạo HUD ===
        hudLayer = new InGame(world.getScoring(), world.getAchievements());
        HBox hud = hudLayer.createHUD();

        // === Ghép Canvas và HUD thành StackPane ===
        StackPane root = new StackPane(canvas, hud);
        StackPane.setAlignment(hud, Pos.TOP_LEFT);

        // === Khởi tạo Notification System ===
        world.getAchievements().initNotificationSystem(root);

        // === Đăng ký listeners ===
        world.getAchievements().addListener(achievement -> {
            System.out.println("🎉 THÀNH TỰU MỞ KHÓA: " + achievement.getName());
            System.out.println("   " + achievement.getDescription());
        });

        world.getAchievements().addRankListener((oldRank, newRank, score) -> {
            System.out.println("🎖️ RANK UP!");
            System.out.println("   " + oldRank.getIcon() + " " + oldRank.getName() +
                    " → " + newRank.getIcon() + " " + newRank.getName());
            System.out.println("   Điểm hiện tại: " + score);
        });

        // === Tạo Scene và xử lý input ===
        Scene scene = new Scene(root, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);

        scene.setOnKeyPressed(e -> {
            // ESC: Pause và quay về menu
            if (e.getCode() == KeyCode.ESCAPE) {
                gamePaused = true;
                stage.setScene(new MainMenu().create(stage));
            }

            // R: Restart game
            if (e.getCode() == KeyCode.R &&
                    (world.getBall().isLost() || world.getScoring().isGameOver())) {
                restartGame();
            }

            //SPACE: Bắn bóng
            if (e.getCode() == KeyCode.SPACE) {
                world.getBall().launch();
            }

            world.getPaddle().onKeyPressed(e.getCode());
        });

        scene.setOnKeyReleased(e -> world.getPaddle().onKeyReleased(e.getCode()));

        // Phát nhạc nền
        AudioSystem.getInstance().playBackgroundMusic("background.wav");

        // === Khởi động game loop ===
        new GameLoop(this).start();

        return scene;
    }

    /**
     * Update - Cập nhật logic game mỗi frame
     */
    public void update(double dt) {
        if (gamePaused) return;

        Ball ball = world.getBall();

        // Nếu bóng đang dính với paddle, cập nhật vị trí theo paddle
        if (ball.isStickToPaddle()) {
            ball.updateStickPosition(world.getPaddle().getX(), world.getPaddle().getY());
        }

        // ===== Xử lý khi bóng rơi xuống đáy =====
        if (ball.isLost()) {
            world.getScoring().loseLife();

            if (world.getScoring().isGameOver()) {
                return;
            } else {
                // Reset bóng về trạng thái stick trên paddle
                ball.resetToStick(world.getPaddle().getX(), world.getPaddle().getY());
            }
        }

        // ===== Update vị trí các đối tượng =====
        world.getPaddle().update(dt);
        ball.update(dt);  // Chỉ di chuyển nếu không stick

        //Chỉ kiểm tra va chạm khi bóng đã được bắn
        if (!ball.isStickToPaddle()) {
            Collision.checkWallCollision(ball, canvas.getWidth(), canvas.getHeight());
            Collision.checkPaddleCollision(ball, world.getPaddle());
            Collision.checkBrickCollision(ball, world.getBricks(), world);
        }

        // ===== Kiểm tra điều kiện thắng level =====
        if (world.getLevel().isComplete()) {
            // Chuyển sang level mới - bóng sẽ tự động stick
            world.nextLevel();

            // KHÔNG cần set gì thêm, nextLevel() đã xử lý:
            // - ball.resetToStick() → bóng dính
            // - stickToPaddle = true → chờ nhấn SPACE
        }
    }

    /**
     * Render - Vẽ tất cả mọi thứ lên màn hình
     */
    public void render() {
        gc.setFill(Colors.PRIMARY);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Colors.TEXT);
        gc.setFont(Fonts.main(20));
        gc.fillText("Level " + world.getLevel().getCurrentLevel(),
                Config.SCREEN_WIDTH - 120, 30);

        world.getPaddle().render(gc);
        world.getBall().render(gc);

        world.getBricks().stream()
                .filter(b -> !b.isDestroyed())
                .forEach(b -> b.render(gc));

        // Hiển thị hướng dẫn khi bóng đang stick
        if (world.getBall().isStickToPaddle()) {
            gc.setFill(Colors.TEXT);
            gc.setFont(Fonts.main(18));
            gc.fillText("Press SPACE to launch ball", 260, 400);
        }

        if (world.getScoring().isGameOver()) {
            gc.setFill(Colors.TEXT);
            gc.setFont(Fonts.main(28));
            gc.fillText("GAME OVER", 320, 280);
            gc.setFont(Fonts.main(16));
            gc.fillText("Press R to Restart", 320, 320);
        }
    }

    /**
     * Restart toàn bộ game
     */
    private void restartGame() {
        world.reset();
        gamePaused = false;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Arkanoid");
        stage.setScene(new MainMenu().create(stage));
        stage.show();

        // Cleanup khi đóng game
        stage.setOnCloseRequest(e -> {
            AudioSystem.getInstance().dispose();
        });
    }
}