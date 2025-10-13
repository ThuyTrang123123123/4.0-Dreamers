package core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import engine.GameLoop;

import java.io.IOException;

public class Game extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Khởi tạo game, tạo scene, v.v.
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        primaryStage.setTitle("Hello!");
        primaryStage.setScene(scene);
        primaryStage.show();
        /**
         * mấy cái Scene đang viết để chương trình có thể chạy thôi.
         * cần chỉnh sửa lại scene tile, kích thước stage, icon game vvv
         */
        //Khởi tạo Gameloop
        GameLoop loop = new GameLoop(this);
        loop.start();
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
}
