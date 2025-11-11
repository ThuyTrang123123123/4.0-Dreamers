package ui.screen;

import core.Config;
import core.Game;
import data.JsonStorage;
import data.Storage;
import data.repositories.PlayerRepository;
import data.repositories.ScoreRepository;
import javafx.geometry.Insets;
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
    public static Scene cachedScenePractice;
    public static Game playGame;
    public static Game practiceGame;

    public Scene create(Stage stage) {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(24));

        Image bg = new Image(
                Objects.requireNonNull(
                        getClass().getResource("/images/BACKGROUND.png")
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

        ButtonUI introduceBtn = new ButtonUI("Introduce");
        introduceBtn.setOnAction(e -> {
//            AudioSystem.getInstance().playSound("select");
            Introduce introduce = new Introduce();
            cachedScene = create(stage);
            stage.setScene(introduce.create(stage));
        });

        ButtonUI playBtn = new ButtonUI("Play");
        playBtn.setOnAction(e -> {
            AudioSystem.getInstance().playSound("select.mp3");
            if (playGame == null) {
                playGame = new Game();
            }
            cachedScene = create(stage);

            if (playGame.isPlayMode() && playGame.hasExistingScene()) {
                playGame.resumeFromMenu(stage);
            } else {
                playGame.setModePlay();
                Scene s = playGame.getOrCreateGameScene(stage);
                stage.setScene(s);
                playGame.unpause();
                playGame.startLoopIfNeeded();
            }
        });

        ButtonUI levelSelectBtn = new ButtonUI("Practice");
        levelSelectBtn.setOnAction(e -> {
            AudioSystem.getInstance().playSound("select.mp3");
            if (practiceGame == null) practiceGame = new Game();

            practiceGame.setModePractice();
            Storage storage = new JsonStorage();
            PlayerRepository playerRepo = new PlayerRepository(storage);
            ScoreRepository scoreRepo  = new ScoreRepository(storage);

            LevelSelect select = new LevelSelect(stage, practiceGame, playerRepo, scoreRepo);
            cachedScenePractice = create(stage);
            stage.setScene(select.create());
        });

        ButtonUI settingsBtn = new ButtonUI("Settings");
        settingsBtn.setOnAction(e -> {
            AudioSystem.getInstance().playSound("select.mp3");
            AudioSystem audio = AudioSystem.getInstance();
            Config cfg = new Config();

            Settings settings = new Settings(audio, cfg, () -> {
                stage.setScene(create(stage));
            });

            stage.setScene(settings.create(stage));
        });

        ButtonUI leaderboardBtn = new ButtonUI("View");
        leaderboardBtn.setOnAction(e -> {
            AudioSystem.getInstance().playSound("select.mp3");
            Game gameInstance = new Game();
            gameInstance.showLeaderboard();
        });

        ButtonUI exitBtn = new ButtonUI("Exit");
        exitBtn.setOnAction(e -> {
            AudioSystem.getInstance().playSound("select.mp3");
            stage.close();
        });

        root.getChildren().addAll(introduceBtn, playBtn, levelSelectBtn, settingsBtn, leaderboardBtn, exitBtn);
        return new Scene(root, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
    }

    private String colorToHex(javafx.scene.paint.Color color) {
        return color.toString().substring(2, 8);
    }
}