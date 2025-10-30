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

public class Game extends Application {
    private Canvas canvas;
    private GraphicsContext gc;
    private final World world = new World();
    private InGame hudLayer;
    private boolean gamePaused = false;
    private boolean gameWon = false;  // ⭐ THÊM: Trạng thái thắng game

    public Scene createGamescene(Stage stage) {
        canvas = new Canvas(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
        gc = canvas.getGraphicsContext2D();

        world.init(canvas);

        hudLayer = new InGame(world.getScoring(), world.getAchievements());
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

        Scene scene = new Scene(root, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                gamePaused = true;
                stage.setScene(new MainMenu().create(stage));
            }

            // ⭐ R: Restart (cả khi game over hoặc thắng game)
            if (e.getCode() == KeyCode.R &&
                    (world.getBall().isLost() || world.getScoring().isGameOver() || gameWon)) {
                restartGame();
            }

            if (e.getCode() == KeyCode.SPACE) {
                world.getBall().launch();
            }

            world.getPaddle().onKeyPressed(e.getCode());
        });

        scene.setOnKeyReleased(e -> world.getPaddle().onKeyReleased(e.getCode()));

        AudioSystem.getInstance().playBackgroundMusic("background.wav");

        new GameLoop(this).start();

        return scene;
    }

    public void update(double dt) {
        if (gamePaused || gameWon) return;  // ⭐ Dừng update nếu thắng game

        Ball ball = world.getBall();

        if (ball.isStickToPaddle()) {
            ball.updateStickPosition(world.getPaddle().getX(), world.getPaddle().getY());
        }

        if (ball.isLost()) {
            world.getScoring().loseLife();

            if (world.getScoring().isGameOver()) {
                return;
            } else {
                ball.resetToStick(world.getPaddle().getX(), world.getPaddle().getY());
            }
        }

        world.getPaddle().update(dt);
        ball.update(dt);

        if (!ball.isStickToPaddle()) {
            Collision.checkWallCollision(ball, canvas.getWidth(), canvas.getHeight());
            Collision.checkPaddleCollision(ball, world.getPaddle());
            Collision.checkBrickCollision(ball, world.getBricks(), world);
        }

        // ⭐ Kiểm tra điều kiện thắng level
        if (world.getLevel().isComplete()) {
            // ⭐ Kiểm tra xem đã hoàn thành TẤT CẢ 12 level chưa
            if (world.getLevel().isGameComplete()) {
                gameWon = true;
                System.out.println("🏆🎉 CHÚC MỪNG! BẠN ĐÃ HOÀN THÀNH TẤT CẢ 12 LEVEL!");
            } else {
                // Chưa hết game, chuyển level tiếp theo
                world.nextLevel();
            }
        }
    }

    public void render() {
        gc.setFill(Colors.PRIMARY);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Colors.TEXT);
        gc.setFont(Fonts.main(20));

        // ⭐ Hiển thị level hiện tại / tổng số level
        gc.fillText("Level " + world.getLevel().getCurrentLevel() + " / " + world.getLevel().getMaxLevel(),
                Config.SCREEN_WIDTH - 150, 30);

        world.getPaddle().render(gc);
        world.getBall().render(gc);

        world.getBricks().stream()
                .filter(b -> !b.isDestroyed())
                .forEach(b -> b.render(gc));

        if (world.getBall().isStickToPaddle()) {
            gc.setFill(Colors.TEXT);
            gc.setFont(Fonts.main(18));
            gc.fillText("Press SPACE to launch ball", 260, 400);
        }

        // ⭐ Hiển thị màn hình thắng game
        if (gameWon) {
            gc.setFill(Colors.TEXT);
            gc.setFont(Fonts.main(32));
            gc.fillText("🏆 YOU WIN! 🏆", 280, 260);
            gc.setFont(Fonts.main(20));
            gc.fillText("Hoàn thành tất cả 12 level!", 250, 300);
            gc.setFont(Fonts.main(16));
            gc.fillText("Final Score: " + world.getScoring().getScore(), 310, 340);
            gc.fillText("Press R to Restart", 310, 370);
        }

        if (world.getScoring().isGameOver()) {
            gc.setFill(Colors.TEXT);
            gc.setFont(Fonts.main(28));
            gc.fillText("GAME OVER", 320, 280);
            gc.setFont(Fonts.main(16));
            gc.fillText("Press R to Restart", 320, 320);
        }
    }

    private void restartGame() {
        world.reset();
        gamePaused = false;
        gameWon = false;  // ⭐ Reset trạng thái thắng game
    }

    public Canvas getCanvas() {
        return canvas;
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Arkanoid - 12 Levels");
        stage.setScene(new MainMenu().create(stage));
        stage.show();

        stage.setOnCloseRequest(e -> {
            AudioSystem.getInstance().dispose();
        });
    }
}