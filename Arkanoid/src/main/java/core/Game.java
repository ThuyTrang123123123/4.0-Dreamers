package core;

import engine.GameLoop;
import entities.Paddle;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ui.screen.MainMenu;

public class Game extends Application {
    // Kích thước màn hình
    private static final double WIDTH = 800;
    private static final double HEIGHT = 600;

    // Các thành phần chính
    private Stage primaryStage;
    private StackPane rootPane; // Layout gốc, cho phép xếp chồng các lớp
    private Canvas gameCanvas;  // Lớp dưới cùng để vẽ game
    private GraphicsContext gc; // "Bút vẽ" cho canvas
    private Pane uiLayer;       // Lớp trên cùng cho các menu UI

    // Trạng thái game đơn giản
    private boolean isPlaying = false;

    // Đối tượng game
    private Paddle paddle;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        primaryStage.setTitle("Arkanoid - 4.0 Dreamers");

        // 1. Tạo Canvas và công cụ vẽ (Lấy từ phiên bản Canvas)
        gameCanvas = new Canvas(WIDTH, HEIGHT);
        gc = gameCanvas.getGraphicsContext2D();

        // 2. Tạo StackPane để xếp chồng Canvas và UI (Logic kết hợp)
        rootPane = new StackPane();
        rootPane.getChildren().add(gameCanvas);

        // 3. Khởi tạo đối tượng game (Lấy từ phiên bản Canvas)
        paddle = new Paddle(WIDTH / 2 - 50, HEIGHT - 40, 100, 20);

        // 4. Bắt đầu game bằng cách hiển thị MainMenu (Lấy từ phiên bản UI)
        MainMenu mainMenu = new MainMenu(this);
        setScreen(mainMenu.createContent());

        // 5. Thiết lập Scene và hiển thị (Logic kết hợp)
        Scene scene = new Scene(rootPane, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();

        // 6. Bắt sự kiện bàn phím (Lấy từ phiên bản Canvas)
        scene.setOnKeyPressed(e -> {
            if (isPlaying) {
                paddle.onKeyPressed(e.getCode());
            }
        });
        scene.setOnKeyReleased(e -> {
            if (isPlaying) {
                paddle.onKeyReleased(e.getCode());
            }
        });

        // 7. Khởi động GameLoop (Lấy từ phiên bản Canvas)
        GameLoop gameLoop = new GameLoop(this);
        gameLoop.start();
    }

    /**
     * Hàm quan trọng để đổi màn hình UI (lớp trên cùng). (Lấy từ phiên bản UI)
     * @param uiNode Layout mới cho lớp UI (ví dụ: VBox của MainMenu).
     */
    public void setScreen(Parent uiNode) {
        if (uiLayer != null) {
            rootPane.getChildren().remove(uiLayer);
        }
        this.uiLayer = (Pane) uiNode;
        rootPane.getChildren().add(uiLayer);
    }

    // Hàm này được MainMenu gọi khi người dùng nhấn "Start"
    public void startGame() {
        this.isPlaying = true;
        // Ẩn lớp UI menu đi
        if (uiLayer != null) {
            uiLayer.setVisible(false);
        }
    }

    /**
     * Cập nhật logic game, được gọi liên tục bởi GameLoop. (Lấy từ phiên bản Canvas)
     */
    public void update(double deltaTime) {
        if (isPlaying) {
            paddle.update(deltaTime, gameCanvas.getWidth());
        }
    }

    /**
     * Vẽ game lên Canvas, được gọi liên tục bởi GameLoop. (Lấy từ phiên bản Canvas)
     */
    public void render() {
        // Xóa sạch Canvas
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        if (isPlaying) {
            paddle.render(gc);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

