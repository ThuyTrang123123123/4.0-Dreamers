package ui.screen;

import core.Game;
import javafx.animation.AnimationTimer;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import systems.ScoringSystem;

public class InGame {
    private final Game game;
    private final ScoringSystem scoringSystem;
    private AnimationTimer gameLoop;

    public InGame(Game game) {
        this.game = game;
        this.scoringSystem = new ScoringSystem(); // Tạm thời tạo mới ở đây
    }

    public Parent createContent() {
        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-color: #87CEEB;"); // Màu xanh da trời

        // --- Phần HUD của bạn ---
        Label scoreLabel = new Label();
        scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        scoreLabel.setTextFill(Color.WHITE);

        Label livesLabel = new Label();
        livesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        livesLabel.setTextFill(Color.WHITE);

        // --- ĐÂY LÀ PHÉP THUẬT CỦA DATA BINDING ---
        scoreLabel.textProperty().bind(scoringSystem.scoreProperty().asString("Score: %d"));
        livesLabel.textProperty().bind(scoringSystem.livesProperty().asString("Lives: %d"));

        // Định vị HUD trên màn hình
        AnchorPane.setTopAnchor(scoreLabel, 10.0);
        AnchorPane.setLeftAnchor(scoreLabel, 20.0);
        AnchorPane.setTopAnchor(livesLabel, 10.0);
        AnchorPane.setRightAnchor(livesLabel, 20.0);

        root.getChildren().addAll(scoreLabel, livesLabel);

        // Khởi động vòng lặp game
        startGameLoop();

        return root;
    }

    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Nơi Thành viên A sẽ thêm logic update game
                // ví dụ: world.update();
            }
        };
        gameLoop.start();
    }
}