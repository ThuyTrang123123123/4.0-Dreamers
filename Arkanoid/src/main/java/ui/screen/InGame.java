package ui.screen;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ui.theme.Colors;
import ui.theme.Fonts;
import ui.widgets.ButtonUI;

public class InGame {

    private Label scoreLabel;
    private Label livesLabel;
    private int score = 0;
    private int lives = 3;

    public Scene create(Stage stage) {

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #" + colorToHex(Colors.BACKGROUND) + ";");

        // === Canvas gameplay ===
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, 800, 600); // tạm vẽ nền gameplay

        // === HUD (score, lives, pause) ===
        scoreLabel = new Label("Score: " + score);
        scoreLabel.setFont(Fonts.main(18));
        scoreLabel.setTextFill(Colors.TEXT);

        livesLabel = new Label("Lives: " + lives);
        livesLabel.setFont(Fonts.main(18));
        livesLabel.setTextFill(Colors.TEXT);

        ButtonUI pauseBtn = new ButtonUI("Pause");
        pauseBtn.setOnAction(e -> stage.setScene(new MainMenu().create(stage)));

        HBox hudBar = new HBox(30, scoreLabel, livesLabel, pauseBtn);
        hudBar.setAlignment(Pos.CENTER);
        hudBar.setStyle("-fx-padding: 10; -fx-background-color: #" + colorToHex(Colors.SECONDARY) + ";");

        // === Đặt layout ===
        root.setTop(hudBar);
        root.setCenter(canvas);

        return new Scene(root, 800, 600);
    }

    /** Cập nhật HUD */
    public void updateHUD(int newScore, int newLives) {
        score = newScore;
        lives = newLives;
        scoreLabel.setText("Score: " + score);
        livesLabel.setText("Lives: " + lives);
    }

    private String colorToHex(javafx.scene.paint.Color color) {
        return color.toString().substring(2, 8);
    }
}
