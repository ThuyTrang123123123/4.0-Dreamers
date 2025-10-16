package ui.screen;

import core.Game;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class MainMenu {
    private final Game game;

    public MainMenu(Game game) {
        this.game = game;
    }

    /**
     * Tạo và trả về layout cho màn hình menu.
     * @return Một Parent node chứa toàn bộ giao diện của menu.
     */
    public Parent createContent() {
        VBox root = new VBox(20); // Layout xếp dọc, khoảng cách 20
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #FFC0CB;"); // Màu hồng pastel

        Button playButton = new Button("Bắt Đầu");
        playButton.setFont(Font.font("Arial", 24));
        playButton.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #E06487;");

        // Xử lý sự kiện khi nhấn nút
        playButton.setOnAction(event -> {
            InGame inGameScreen = new InGame(game);
            game.setScreen(inGameScreen.createContent()); // Chuyển sang màn hình InGame
        });

        root.getChildren().add(playButton);
        return root;
    }
}