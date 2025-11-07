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
        this.stage = stage;
        canvas = new Canvas(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        world.init(canvas);

        hudLayer = new InGame(this, world.getScoring(), world.getAchievements());
        HBox hud = hudLayer.createHUD();

        StackPane root = new StackPane(canvas, hud);
        StackPane.setAlignment(hud, Pos.TOP_LEFT);
        world.getAchievements().initNotificationSystem(root);

        Scene scene = new Scene(root, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
        this.inGameScene = scene;

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                showPause();
                return;
            }

            if (e.getCode() == KeyCode.C) {
                resumeGame();
                return;
            }

            // M: Back to Menu
            if (e.getCode() == KeyCode.M) {
                gamePaused = true;
//                systems.AudioSystem.getInstance().stopMusic();
                Scene menuScene = MainMenu.cachedScene;
                if (menuScene == null)
                {
                    menuScene = new MainMenu().create(stage);
                    MainMenu.cachedScene = menuScene;
                }
                stage.setScene(menuScene);
                return;
            }

            if (e.getCode() == KeyCode.R || world.getScoring().isGameOver()) {
                restartGame();
                return;
            }

            if (e.getCode() == KeyCode.SPACE) {
                for (Ball ball : world.getBalls()) {
                    if (ball.isStickToPaddle()) ball.launch();
                }
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

    public void update(double dt) {
        if (gamePaused || gameWon) return;

        Paddle paddle = world.getPaddle();
        List<Ball> balls = world.getBalls();

        List<Ball> lostBalls = new ArrayList<>();

        for (Ball ball : balls) {
            if (ball.isStickToPaddle()) {
                ball.updateStickPosition(paddle.getX(), paddle.getY());
            }

            if (ball.isLost()) {
                lostBalls.add(ball);
                continue;
            }

            ball.update(dt);

            if (!ball.isStickToPaddle()) {
                Collision.handleBallWallCollision(ball, canvas.getWidth(), canvas.getHeight());
                if (Collision.isBallTouchingPaddle(ball, paddle)) {
                    Collision.handleBallPaddleCollision(ball, paddle);
                }
                Collision.handleBallBrickCollision(ball, world.getBricks(), world);
            }
        }

        world.getBalls().removeAll(lostBalls);
        if (!lostBalls.isEmpty() && world.getBalls().isEmpty()) {
            world.getScoring().loseLife();
        }

        if (world.getBalls().isEmpty() && !world.getScoring().isGameOver()) {
            Ball newBall = new Ball(
                    Config.SCREEN_WIDTH / 2.0,
                    Config.SCREEN_HEIGHT - 70,
                    Config.BALL_RADIUS,
                    Config.BALL_SPEED
            );
            newBall.setStickToPaddle(true);
            world.getBalls().add(newBall);
        }

        paddle.update(dt);

        List<PowerUp> toRemove = new ArrayList<>();
        for (PowerUp pu : world.getPowerUps()) {
            pu.update(dt);
            if (Collision.isPowerUpTouchingPaddle(pu, paddle)) {
                Collision.handlePowerUpCollision(pu, paddle, world);
                world.getPowerUpPool().release(pu);
                toRemove.add(pu);
            }
        }
        world.getPowerUps().removeAll(toRemove);

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

        for (Ball ball : world.getBalls()) {
            ball.render(gc);
        }

        for (Brick b : world.getBricks()) {
            if (!b.isDestroyed()) b.render(gc);
        }

        for (PowerUp pu : world.getPowerUps()) {
            pu.render(gc);
        }

        if (world.getBalls().stream().anyMatch(Ball::isStickToPaddle)) {
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

    public void showPause() {
        gamePaused = true;
    }

    public void resumeGame() {
        gamePaused = false;
    }

    private void restartGameFromPause() {
        restartGame();
        stage.setScene(inGameScene);
    }

    private void restartGame() {
        world.reset();
        gamePaused = false;
        gameWon = false;
    }

    public void showMainMenu() {
        gamePaused = false;
        stage.setScene(mainMenuScene);
    }

    public Scene getOrCreateGameScene(Stage stage) {
        if (inGameScene == null) {
            inGameScene = createGamescene(stage);
        }
        return inGameScene;
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

        stage.setOnCloseRequest(e -> AudioSystem.getInstance().dispose());
    }
}