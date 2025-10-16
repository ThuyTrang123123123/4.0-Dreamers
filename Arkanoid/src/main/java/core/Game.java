package core;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.screen.MainMenu; // Import màn hình menu chính

import java.io.IOException;

public class Game extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        primaryStage.setTitle("Arkanoid - 4.0 Dreamers");

        // Bắt đầu game bằng cách hiển thị MainMenu
        MainMenu mainMenu = new MainMenu(this);
        setScreen(mainMenu.createContent()); // Lấy layout từ MainMenu và hiển thị

        primaryStage.show();
    }

    /**
     * Hàm quan trọng để đổi màn hình (Scene).
     * @param screenNode Layout gốc của màn hình mới (ví dụ: VBox, AnchorPane).
     */
    public void setScreen(Parent screenNode) {
        Scene scene = new Scene(screenNode, 800, 600);
        primaryStage.setScene(scene);
        screenNode.requestFocus();
    }

    /**cần viết định nghĩa hàm update,renđer để  có thể gọi trong GameLoop
     *
     *
     */
    public void update(double deltaTime) {
        // Cập nhật game
    }

    public void render() {
        // Vẽ game
    }

    public static void main(String[] args) {
        launch(args); // Phương thức chuẩn để khởi chạy ứng dụng JavaFX
    }
}