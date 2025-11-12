package ui.screen;

import core.Config;
import core.Game;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import systems.ScoringSystem;
import systems.AchievementSystem;
import ui.theme.Colors;
import ui.theme.Fonts;
import ui.widgets.ButtonUI;

/**
 * InGame – Màn hình chính khi đang chơi, hiển thị gameplay và HUD.
 */
public class InGame {
    private Label scoreLabel;
    private Label livesLabel;
    private Label bricksLabel;
   // private Label rankLabel;
    private final Game game;
    private final ScoringSystem scoring;
    private final AchievementSystem achievements;

    public InGame(Game game, ScoringSystem scoring, AchievementSystem achievements) {
        this.game = game;
        this.scoring = scoring;
        this.achievements = achievements;
    }

    public Scene create(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #" + colorToHex(Colors.BACKGROUND) + ";");

        Canvas canvas = new Canvas(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);

        scoreLabel = new Label("Score: 0");
        scoreLabel.setFont(Fonts.main(18));
        scoreLabel.setTextFill(Colors.TEXT);

        livesLabel = new Label("Lives: 0");
        livesLabel.setFont(Fonts.main(18));
        livesLabel.setTextFill(Colors.TEXT);

        bricksLabel = new Label("Bricks: 0");
        bricksLabel.setFont(Fonts.main(18));
        bricksLabel.setTextFill(Colors.TEXT);

        ButtonUI pauseBtn = new ButtonUI("Pause");
        pauseBtn.setOnAction(e -> game.showPause());

        HBox hudBar = new HBox(30, scoreLabel, livesLabel, bricksLabel, pauseBtn);
        hudBar.setAlignment(Pos.CENTER);
        hudBar.setStyle("-fx-padding: 10; -fx-background-color: #" + colorToHex(Colors.SECONDARY) + ";");

        root.setTop(hudBar);
        root.setCenter(canvas);

        updateHUD(); // cập nhật HUD lần đầu

        return new Scene(root, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
    }

    public HBox createHUD() {
        scoreLabel = new Label("Score: 0");
        scoreLabel.setFont(Fonts.main(18));
        scoreLabel.setTextFill(Colors.TEXT);

        livesLabel = new Label("Lives: 0");
        livesLabel.setFont(Fonts.main(18));
        livesLabel.setTextFill(Colors.TEXT);

        bricksLabel = new Label("Bricks: 0");
        bricksLabel.setFont(Fonts.main(18));
        bricksLabel.setTextFill(Colors.TEXT);

        HBox hudBar = new HBox(40, scoreLabel, livesLabel, bricksLabel);
        hudBar.setAlignment(Pos.TOP_LEFT);
        hudBar.setPadding(new Insets(10));
        hudBar.setStyle("-fx-background-color: rgba(255,255,255,0.2); -fx-background-radius: 8;");
        hudBar.setMouseTransparent(true);

        updateHUD(); // cập nhật HUD lần đầu

        return hudBar;
    }

    public void updateHUD() {
        scoreLabel.setText("Score: " + scoring.getScore());
        livesLabel.setText("Lives: " + scoring.getLives());
        bricksLabel.setText("Bricks: " + scoring.getBricksDestroyed());
       // rankLabel.setText(achievements.getCurrentRankIcon() + " " + achievements.getCurrentRankName());
    }

    private String colorToHex(Color color) {
        return color.toString().substring(2, 8);
    }
}