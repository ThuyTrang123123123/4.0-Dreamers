package core;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import engine.Collision;
import engine.GameLoop;
import engine.*;

import entities.Ball;
import entities.Paddle;
import entities.bricks.Brick;
import entities.powerups.BonusCoin;
import entities.powerups.EnlargePaddle;
import entities.powerups.ExtraLife;
import entities.powerups.PowerUp;

import systems.AchievementSystem;
import systems.AudioSystem;
import systems.ScoringSystem;

import ui.screen.InGame;
import ui.screen.MainMenu;
import ui.screen.Pause;
import ui.theme.Colors;
import ui.theme.Fonts;

public class Game extends Application {
    private Canvas canvas;
    private GraphicsContext gc;
    private final World world = new World();
    private InGame hudLayer;
    private boolean gamePaused = false;
    private boolean gameWon = false;
    private Stage stage;
    private Scene inGameScene;
    private Scene pauseScene;
    private Scene mainMenuScene;
    private GameLoop loop;

    public Scene createGamescene(Stage stage) {
        canvas = new Canvas(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
        gc = canvas.getGraphicsContext2D();

        world.init(canvas);

        hudLayer = new InGame(this, world.getScoring(), world.getAchievements());
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