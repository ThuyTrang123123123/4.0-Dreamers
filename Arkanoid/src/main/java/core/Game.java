package core;

import engine.GameLoop;
import entities.Ball;
import entities.Paddle;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Game extends Application {

    // Kích thước màn hình game
    private static final double WIDTH = 800;
    private static final double HEIGHT = 600;

    // Các thành phần cốt lõi để vẽ
    private Canvas canvas;
    private GraphicsContext gc;

    // Các đối tượng trong game
    private Paddle paddle;
    private Ball ball;
    private Level currentLevel;

    @Override
    public void start(Stage primaryStage) {
        // 1. Thiết lập "sân khấu" để vẽ
        canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();

        // 2. Khởi tạo các đối tượng game
        paddle = new Paddle(WIDTH / 2 - 50, HEIGHT - 40, 100, 20);
        ball = new Ball(WIDTH / 2, HEIGHT / 2, 10);
        currentLevel = new Level("level1.json"); // Tải màn chơi đầu tiên

        // 3. Đặt Canvas vào trong Scene và hiển thị
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        primaryStage.setTitle("Arkanoid - 4.0 Dreamers");
        primaryStage.setScene(scene);
        primaryStage.show();

        // 4. Bắt sự kiện bàn phím để điều khiển paddle
        scene.setOnKeyPressed(event -> paddle.onKeyPressed(event.getCode()));
        scene.setOnKeyReleased(event -> paddle.onKeyReleased(event.getCode()));

        // 5. Khởi động vòng lặp game
        GameLoop gameLoop = new GameLoop(this);
        gameLoop.start();
    }

    /**
     * Cập nhật logic của tất cả đối tượng trong game.
     * Được gọi liên tục bởi GameLoop.
     * @param deltaTime Thời gian trôi qua kể từ khung hình trước.
     */
    public void update(double deltaTime) {
        paddle.update(deltaTime, canvas.getWidth());
        ball.update(deltaTime, canvas.getWidth(), canvas.getHeight());
        // TODO: Thêm logic kiểm tra va chạm ở đây (trong engine/Collision.java)
    }

    /**
     * Vẽ tất cả đối tượng lên Canvas.
     * Được gọi liên tục bởi GameLoop.
     */
    public void render() {
        // Xóa sạch màn hình với màu đen
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        // Yêu cầu từng đối tượng tự vẽ
        paddle.render(gc);
        ball.render(gc);

        // Vẽ tất cả các viên gạch trong màn chơi
        if (currentLevel != null) {
            currentLevel.getBricks().forEach(brick -> brick.render(gc));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
