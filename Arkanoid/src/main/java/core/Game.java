package core;

import engine.Collision;
import engine.GameLoop;
import entities.Ball;
import entities.Paddle;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Game extends Application {
    private Canvas canvas;
    private GraphicsContext gc;

    private final World world = new World();
    private int score = 0;

    @Override
    public void start(Stage stage) {
        canvas = new Canvas(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
        gc = canvas.getGraphicsContext2D();

        world.init(canvas); // khởi tạo thế giới

        Scene scene = new Scene(new StackPane(canvas));
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) stage.close();
            world.getPaddle().onKeyPressed(e.getCode());
        });
        scene.setOnKeyReleased(e -> world.getPaddle().onKeyReleased(e.getCode()));

        stage.setScene(scene);
        stage.setTitle("Arkanoid JavaFX");
        stage.show();

        new GameLoop(this).start();
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
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        world.getPaddle().render(gc);
        world.getBall().render(gc);
        world.getBricks().forEach(b -> b.render(gc));

        gc.setFill(Color.WHITE);
        gc.fillText("Score: " + score, 10, 20);

        if (world.getBall().isLost()) {
            gc.setFill(Color.RED);
            gc.fillText("GAME OVER", 360, 300);
        }
    }

    public Canvas getCanvas() {
        return canvas;
    }
}
