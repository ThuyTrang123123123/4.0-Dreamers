package core;

import engine.GameLoop;
import entities.Ball;
import entities.bricks.Brick;
import entities.Paddle;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Game extends Application {
    private Canvas canvas;
    private GraphicsContext gc;

    private Paddle paddle;
    private Ball ball;
    private List<Brick> bricks;

    private int score = 0;

    @Override
    public void start(Stage primaryStage) {
        canvas = new Canvas(800, 600);
        gc = canvas.getGraphicsContext2D();

        paddle = new Paddle(350, 550, 100, 15);
        ball = new Ball(395, 530, 10, 250);
        bricks = new ArrayList<>();

        // Tạo hàng gạch
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                bricks.add(new Brick(60 + col * 70, 50 + row * 30, 60, 20));
            }
        }

        Scene scene = new Scene(new StackPane(canvas));
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) primaryStage.close();
            paddle.onKeyPressed(e.getCode());
        });
        scene.setOnKeyReleased(e -> paddle.onKeyReleased(e.getCode()));

        primaryStage.setScene(scene);
        primaryStage.setTitle("Arkanoid");
        primaryStage.show();

        new GameLoop(this).start();
    }

    public void update(double deltaTime) {
        if (ball.isLost()) return; // nếu bóng đã rơi thì ngừng

        paddle.update(deltaTime);
        ball.update(deltaTime);

        // Va chạm tường
        if (ball.getLeft() <= 0 || ball.getRight() >= canvas.getWidth())
            ball.reverseX();
        if (ball.getTop() <= 0)
            ball.reverseY();

        // Bóng rơi xuống dưới game over
        if (ball.getBottom() >= canvas.getHeight()) {
            ball.setLost(true);
            System.out.println("GAME OVER!");
        }

        // Va chạm paddle
        if (ball.getBounds().intersects(paddle.getBounds())) {
            ball.reverseY();
            // Góc đập nhẹ theo vị trí tương đối
            double hitPos = (ball.getX() + ball.getDiameter()/2) - (paddle.getX() + paddle.getWidth()/2);
            ball.setVelocityX(hitPos * 2); // thay đổi hướng tùy theo vị trí va chạm
        }

        // Va chạm gạch
        for (Brick brick : bricks) {
            if (!brick.isDestroyed() && ball.getBounds().intersects(brick.getBounds())) {
                brick.hit();
                ball.reverseY();
                score += 10;
                break;
            }
        }
    }

    public void render() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Vẽ các thực thể
        paddle.render(gc);
        ball.render(gc);
        bricks.forEach(brick -> brick.render(gc));

        // Vẽ điểm
        gc.setFill(Color.WHITE);
        gc.fillText("Score: " + score, 10, 20);

        // Vẽ thông báo nếu bóng rơi
        if (ball.isLost()) {
            gc.setFill(Color.RED);
            gc.fillText("GAME OVER", 360, 300);
        }
    }
}
