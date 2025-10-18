package core;

import engine.GameLoop;
import entities.Ball;
import entities.bricks.Brick;
import entities.Paddle;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ui.screen.MainMenu;

import java.util.ArrayList;
import java.util.List;

public class Game extends Application {
    private Canvas canvas;
    private GraphicsContext gc;
    private Paddle paddle;
    private Ball ball;
    private List<Brick> bricks;

    public Scene createGamescene(Stage stage) {

        canvas = new Canvas(800, 600);
        gc = canvas.getGraphicsContext2D();

        paddle = new Paddle(350, 550, 100, 15);
        ball = new Ball(395, 530, 10, 200);
        bricks = new ArrayList<>();

        // Tạo hàng gạch
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                bricks.add(new Brick(60 + col * 70, 50 + row * 30, 60, 20));
            }
        }

        Scene scene = new Scene(new StackPane(canvas));
        scene.setOnKeyPressed(e -> paddle.onKeyPressed(e.getCode()));
        scene.setOnKeyReleased(e -> paddle.onKeyReleased(e.getCode()));


        new GameLoop(this).start();

        return scene;
    }

    public void update(double deltaTime) {
        paddle.update(deltaTime);
        ball.update(deltaTime);

        // Va chạm tường
        if (ball.getX() <= 0 || ball.getX() + ball.getRadius() * 2 >= 800)
            ball.reverseX();
        if (ball.getY() <= 0)
            ball.reverseY();

        // Va chạm paddle
        if (ball.getBounds().intersects(paddle.getBounds()))
            ball.reverseY();

        // Va chạm gạch
        bricks.removeIf(brick -> {
            if (ball.getBounds().intersects(brick.getBounds())) {
                ball.reverseY();
                return true;
            }
            return false;
        });
    }

    public void render() {
        // Vẽ nền đen
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        paddle.render(gc);
        ball.render(gc);
        bricks.forEach(brick -> brick.render(gc));
    }
    @Override
    public void start(Stage stage) {
        stage.setTitle("Arkanoid");
        stage.setScene(new ui.screen.MainMenu().create(stage));
        stage.show();
    }
}
