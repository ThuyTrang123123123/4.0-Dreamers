package ui.screen;

import core.Config;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class End {
    public static Scene create(
            Stage stage,
            int score,
            Integer bestScore,
            Runnable onReplay,
            Runnable onMainMenu,
            String backgroundPngPath
    ) {
        ImageView bgView = new ImageView();
        try {
            Image bg = new Image(
                    Objects.requireNonNull(
                            End.class.getResource(backgroundPngPath),
                            "Background not found: " + backgroundPngPath
                    ).toExternalForm()
            );
            bgView.setImage(bg);
            bgView.setPreserveRatio(false);
            bgView.setFitWidth(Config.SCREEN_WIDTH);
            bgView.setFitHeight(Config.SCREEN_HEIGHT);
        } catch (Exception e) {
            System.err.println("⚠️ Cannot load end background: " + e.getMessage());
        }

        Label title = new Label("LEVEL COMPLETE");
        title.setStyle("-fx-font-size: 36px; -fx-text-fill: white; -fx-font-weight: bold;");

        Label scoreLb = new Label("Score: " + score);
        scoreLb.setStyle("-fx-font-size: 22px; -fx-text-fill: white;");

        Label bestLb = new Label(
                (bestScore == null) ? "" : ("Best: " + bestScore)
        );
        bestLb.setStyle("-fx-font-size: 16px; -fx-text-fill: #dddddd;");

        Button replayBtn = new Button("Play Again");
        replayBtn.setMinWidth(220);
        replayBtn.setStyle("-fx-font-size: 16px; -fx-background-radius: 12px;");

        Button menuBtn = new Button("Main Menu");
        menuBtn.setMinWidth(220);
        menuBtn.setStyle("-fx-font-size: 16px; -fx-background-radius: 12px;");

        replayBtn.setOnAction(e -> {
            if (onReplay != null) onReplay.run();
        });
        menuBtn.setOnAction(e -> {
            if (onMainMenu != null) onMainMenu.run();
        });

        VBox box = new VBox(10, title, scoreLb, bestLb, replayBtn, menuBtn);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: rgba(0,0,0,0.35); -fx-padding: 24px; -fx-background-radius: 16px;");

        StackPane root = new StackPane(bgView, box);
        root.setPrefSize(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);

        Scene scene = new Scene(root, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);

        scene.setOnKeyPressed(ev -> {
            switch (ev.getCode()) {
                case ENTER:
                case R:
                    if (onReplay != null) onReplay.run();
                    break;
                case M:
                case ESCAPE:
                    if (onMainMenu != null) onMainMenu.run();
                    break;
            }
        });

        return scene;
    }
}
