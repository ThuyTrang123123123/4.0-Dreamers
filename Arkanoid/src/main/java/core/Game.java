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
import ui.screen.Pause;
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
    private Stage stage;
    private Scene inGameScene;
    private Scene pauseScene;
    private Scene mainMenuScene;
    private GameLoop loop;


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
        hudLayer = new InGame(this, world.getScoring(), world.getAchievements());
        HBox hud = hudLayer.createHUD();

        StackPane root = new StackPane(canvas, hud);
        StackPane.setAlignment(hud, Pos.TOP_LEFT);

        world.getAchievements().initNotificationSystem(root);

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
        this.inGameScene = scene;

        scene.setOnKeyPressed(e -> {
            // ESC: Pause
            if (e.getCode() == KeyCode.ESCAPE) {
                showPause();
                return;
            }

            //C: Resume game
            if (e.getCode() == KeyCode.C) {
                resumeGame();
                return;
            }

            // R: Restart game
            if (e.getCode() == KeyCode.R ||
                    (world.getBall().isLost() || world.getScoring().isGameOver())) {
                restartGame();
                return;
            }

            //SPACE: Bắn bóng
            if (e.getCode() == KeyCode.SPACE) {
                world.getBall().launch();
                return;
            }

            world.getPaddle().onKeyPressed(e.getCode());
        });

        scene.setOnKeyReleased(e -> world.getPaddle().onKeyReleased(e.getCode()));


        AudioSystem audio = AudioSystem.getInstance();
        audio.playIfChanged(audio.getSelectedMusicOrDefault(Config.DEFAULT_MUSIC));

        loop = new GameLoop(this);
        loop.start();

        return scene;
    }

    public void showPause() {
        gamePaused = true;
        stage.setScene(pauseScene);
    }

    public void resumeGame() {
        gamePaused = false;
        stage.setScene(inGameScene);
        inGameScene.getRoot().requestFocus();
    }

    private void restartGameFromPause() {
        restartGame();
        stage.setScene(inGameScene);
    }

    public void showMainMenu() {
        gamePaused = false;
        stage.setScene(mainMenuScene);
    }

    public Scene getOrCreateGameScene(Stage stage) {
        if (inGameScene == null) {
            inGameScene = createGamescene(stage); // hàm bạn đã có
        }
        return inGameScene;
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
        ball.update(dt);

        if (!ball.isStickToPaddle()) {
            Collision.checkWallCollision(ball, canvas.getWidth(), canvas.getHeight());
            Collision.checkPaddleCollision(ball, world.getPaddle());
            Collision.checkBrickCollision(ball, world.getBricks(), world);
        }

        // ===== Kiểm tra điều kiện thắng level =====
        if (world.getLevel().isComplete()) {
            world.nextLevel();
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
        this.stage = stage;
        stage.setTitle("Arkanoid");
        stage.setScene(new MainMenu().create(stage));
        stage.show();

        // Cleanup khi đóng game
        stage.setOnCloseRequest(e -> {
            AudioSystem.getInstance().dispose();
        });
    }
}