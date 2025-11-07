package ui.screen;

import core.Config;
import core.Game;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import systems.AudioSystem;
import ui.theme.Colors;
import ui.widgets.ButtonUI;

public class MainMenu {
    public static Scene cachedScene;
    public static Game currentGame;

    public Scene create(Stage stage) {
        VBox root = new VBox(20);
        root.setStyle("-fx-background-color: #" + colorToHex(Colors.BACKGROUND) + "; -fx-alignment: center;");

        ButtonUI playBtn = new ButtonUI("Play");
        playBtn.setOnAction(e -> {
            if (currentGame == null) currentGame = new Game();
            Scene s = currentGame.getOrCreateGameScene(stage);
            stage.setScene(s);
            currentGame.unpause();
            currentGame.startLoopIfNeeded();
        });

        ButtonUI settingsBtn = new ButtonUI("Settings");
        settingsBtn.setOnAction(e -> {
            AudioSystem audio = AudioSystem.getInstance();
            Config cfg = new Config();

            Settings settings = new Settings(audio, cfg, () -> {
                stage.setScene(create(stage));
            });

            stage.setScene(settings.create(stage));
        });

        ButtonUI exitBtn = new ButtonUI("Exit");
        exitBtn.setOnAction(e -> stage.close());

        root.getChildren().addAll(playBtn, settingsBtn, exitBtn);
        return new Scene(root, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
    }

    private String colorToHex(javafx.scene.paint.Color color) {
        return color.toString().substring(2, 8);
    }
}