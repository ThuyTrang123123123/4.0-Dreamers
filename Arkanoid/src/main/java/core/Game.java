package core;

import engine.GameLoop;
import entities.Paddle;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Game extends Application {
    private Canvas canvas;
    private GraphicsContext gc;
    private Paddle paddle;

    @Override
    public void start(Stage primaryStage) {
        double width = 800;
        double height = 600;

        // Canvas để vẽ game
        canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();

        // Khởi tạo Paddle
        paddle = new Paddle(width / 2 - 50, height - 40, 100, 20);

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, width, height);
        primaryStage.setTitle("Arkanoid");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Bắt sự kiện phím
        scene.setOnKeyPressed(e -> paddle.onKeyPressed(e.getCode()));
        scene.setOnKeyReleased(e -> paddle.onKeyReleased(e.getCode()));

        // Khởi tạo GameLoop
        GameLoop loop = new GameLoop(this);
        loop.start();
    }

    // --- Update: cập nhật logic game ---
    public void update(double deltaTime) {
        paddle.update(deltaTime, canvas.getWidth());
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        paddle.render(gc);
    }
}
