package core;

import engine.Collision;
import engine.GameLoop;
import entities.Ball;
import entities.Paddle;
import entities.powerups.PowerUp;
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

import java.util.ArrayList;
import java.util.List;

public class Game extends Application {
    private Canvas canvas;
    private GraphicsContext gc;
    private final World world = new World();
    private InGame hudLayer;
    private boolean gamePaused = false;
    private boolean gameWon = false;

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
            // println removed
        });

        world.getAchievements().addRankListener((oldRank, newRank, score) -> {
            // println removed
        });

        Scene scene = new Scene(root, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                gamePaused = true;
                stage.setScene(new MainMenu().create(stage));
            }

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
        if (gamePaused || gameWon) return;

        Ball ball = world.getBall();
        Paddle paddle = world.getPaddle();

        if (ball.isStickToPaddle()) {
            ball.updateStickPosition(paddle.getX(), paddle.getY());
        }

        if (ball.isLost()) {
            world.getScoring().loseLife();
            if (world.getScoring().isGameOver()) return;
            ball.resetToStick(paddle.getX(), paddle.getY());
        }

        paddle.update(dt);
        ball.update(dt);

        List<PowerUp> toRemove = new ArrayList<>();
        for (PowerUp pu : world.getPowerUps()) {
            pu.update(dt);

            if (!ball.isStickToPaddle()) {
                if (Collision.isPowerUpTouchingPaddle(pu, paddle)) {
                    Collision.handlePowerUpCollision(pu, paddle, world);
                    world.getPowerUpPool().release(pu);
                    toRemove.add(pu);
                }
            }
        }
        world.getPowerUps().removeAll(toRemove);

        if (!ball.isStickToPaddle()) {
            Collision.handleBallWallCollision(ball, canvas.getWidth(), canvas.getHeight());

            if (Collision.isBallTouchingPaddle(ball, paddle)) {
                Collision.handleBallPaddleCollision(ball, paddle);
            }

            Collision.handleBallBrickCollision(ball, world.getBricks(), world);
        }

        if (world.getLevel().isComplete()) {
            if (world.getLevel().isGameComplete()) {
                gameWon = true;
            } else {
                world.nextLevel();
            }
        }
    }

    public void render() {
        gc.setFill(Colors.PRIMARY);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Colors.TEXT);
        gc.setFont(Fonts.main(20));
        gc.fillText("Level " + world.getLevel().getCurrentLevel() + " / " + world.getLevel().getMaxLevel(),
                Config.SCREEN_WIDTH - 150, 30);

        world.getPaddle().render(gc);
        world.getBall().render(gc);

        world.getBricks().stream()
                .filter(b -> !b.isDestroyed())
                .forEach(b -> b.render(gc));

        for (PowerUp pu : world.getPowerUps()) {
            pu.render(gc);
        }

        if (world.getBall().isStickToPaddle()) {
            gc.setFill(Colors.TEXT);
            gc.setFont(Fonts.main(18));
            gc.fillText("Press SPACE to launch ball", 260, 400);
        }

        if (gameWon) {
            gc.setFill(Colors.TEXT);
            gc.setFont(Fonts.main(32));
            gc.fillText("YOU WIN!", 280, 260);
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
        gameWon = false;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public boolean getGamestatus() {
        return !gamePaused;
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Arkanoid - 12 Levels");
        stage.setScene(new MainMenu().create(stage));
        stage.show();

        stage.setOnCloseRequest(e -> AudioSystem.getInstance().dispose());
    }
}