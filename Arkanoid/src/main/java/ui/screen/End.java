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
import systems.AudioSystem;
import ui.widgets.ButtonUI;

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
            System.err.println("Cannot load end background: " + e.getMessage());
        }

        Label scoreLb = new Label("Score: " + score);
        scoreLb.setStyle("-fx-font-size: 50px; -fx-text-fill: white;");

        Label bestLb = new Label(
                (bestScore == null) ? "" : ("Best: " + bestScore)
        );
        bestLb.setStyle("-fx-font-size: 16px; -fx-text-fill: #dddddd;");

        ButtonUI replayBtn = new ButtonUI("Play Again");
        ButtonUI menuBtn = new ButtonUI("Main Menu");

        replayBtn.setOnAction(e -> {
            AudioSystem.getInstance().playSound("select.mp3");
            if (onReplay != null) onReplay.run();
        });
        menuBtn.setOnAction(e -> {
            AudioSystem.getInstance().playSound("select.mp3");
            if (onMainMenu != null) onMainMenu.run();
        });

        VBox box = new VBox(10, scoreLb, bestLb, replayBtn, menuBtn);
        box.setAlignment(Pos.CENTER);

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
