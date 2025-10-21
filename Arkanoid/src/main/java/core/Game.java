package core;

import engine.Collision;
import engine.GameLoop;
import entities.Ball;
import entities.Paddle;
import entities.bricks.Brick;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import ui.screen.InGame;
import ui.screen.MainMenu;
import ui.theme.Colors;
import ui.theme.Fonts;

public class Game extends Application {
    private Canvas canvas;
    private GraphicsContext gc;
    private Paddle paddle;
    private Ball ball;
    private List<Brick> bricks;

    private final World world = new World();

    public Scene createGamescene(Stage stage) {

        canvas = new Canvas(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
        gc = canvas.getGraphicsContext2D();

        world.init(canvas);

        InGame hudLayer = new InGame();
        HBox hud = hudLayer.createHUD();

        StackPane root = new StackPane(canvas, hud);
        StackPane.setAlignment(hud, Pos.TOP_LEFT); // HUD ở trên cùng góc trái

        Scene scene = new Scene(root, 800, 600);
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) stage.close();
            world.getPaddle().onKeyPressed(e.getCode());
        });
        scene.setOnKeyReleased(e -> world.getPaddle().onKeyReleased(e.getCode()));

        new GameLoop(this).start();

        return scene;
    }

    public void update(double dt) {
        Ball ball = world.getBall();
        if (ball.isLost()) return;

        world.getPaddle().update(dt);
        ball.update(dt);

        Collision.checkWallCollision(ball, canvas.getWidth(), canvas.getHeight());
        Collision.checkPaddleCollision(ball, world.getPaddle());
        Collision.checkBrickCollision(ball, world.getBricks());
    }

    public void render() {
        gc.setFill(Colors.PRIMARY);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        world.getPaddle().render(gc);
        world.getBall().render(gc);
        world.getBricks().forEach(b -> b.render(gc));

        if (world.getBall().isLost()) {
            gc.setFill(Colors.TEXT);
            gc.setFont(Fonts.main(18));
            gc.fillText("GAME OVER", 360, 300);
        }
    }

    public Canvas getCanvas() {
        return canvas;
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Arkanoid");
        stage.setScene(new ui.screen.MainMenu().create(stage));
        stage.show();
    }
}