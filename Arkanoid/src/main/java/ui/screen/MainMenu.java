package ui.screen;

import core.Config;
import core.Game;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import systems.AudioSystem;
import ui.theme.Colors;
import ui.widgets.ButtonUI;

import java.util.Objects;

public class MainMenu {
    public static Scene cachedScene;
    public static Game currentGame;

    public Scene create(Stage stage) {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);

        Image bg = new Image(
                Objects.requireNonNull(
                        getClass().getResource("/themes/BACKGROUND.png")
                ).toExternalForm()
        );
        BackgroundImage bgi = new BackgroundImage(
                bg,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(
                        BackgroundSize.AUTO, BackgroundSize.AUTO,
                        false, false, true, true
                )
        );
        root.setBackground(new Background(bgi));

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