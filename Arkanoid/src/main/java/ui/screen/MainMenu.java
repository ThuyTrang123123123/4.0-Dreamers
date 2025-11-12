package ui.screen;

import core.Config;
import core.Game;
import data.AccountManager;
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
import ui.widgets.ButtonUI;

import java.util.Objects;

public class MainMenu {
    public static Scene cachedScene;
    public static Scene cachedScenePractice;
    public static Game playGame;
    public static Game practiceGame;

    public Scene create(Stage stage) {
        AccountManager accountManager = new AccountManager();

        // Kiểm tra đã đăng nhập chưa
        if (AccountManager.getLoggedInUser() == null) {
            LoginScreen loginScreen = new LoginScreen(stage, accountManager, () -> {
                stage.setScene(create(stage)); // Sau khi login thành công
            });
            return loginScreen.create();
        }
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
            AudioSystem.getInstance().playSound("select.mp3");
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

        ButtonUI shopBtn = new ButtonUI("Shop");
        shopBtn.setOnAction(e -> {
            AudioSystem.getInstance().playSound("select.mp3");

            Game g = (playGame != null) ? playGame : MainMenu.practiceGame;

            if (g == null) {
                g = new Game();
                g.setModePlay();
                g.getOrCreateGameScene(stage);
                playGame = g;
            }

            Shop shop = new Shop(g, () -> {
                stage.setScene(cachedScene != null ? cachedScene : create(stage));
            });

            Scene shopScene = shop.create(stage);
            cachedScene = create(stage);
            stage.setScene(shopScene);
        });

        ButtonUI logoutBtn = new ButtonUI("Log out");
        logoutBtn.setOnAction(e -> {
            AccountManager.logout();
            stage.setScene(create(stage));
        });

        root.getChildren().addAll(introduceBtn, playBtn, levelSelectBtn, settingsBtn, leaderboardBtn, exitBtn);
        root.getChildren().add(shopBtn);
        root.getChildren().add(logoutBtn);
        return new Scene(root, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
    }
}