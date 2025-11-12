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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;
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
            // Check 1: Game có đang pause trong RAM không?
            boolean isPaused = (playGame != null && !playGame.isGameFinished());

            // Check 2: Có file save trên đĩa không?
            // (Tạo 1 instance tạm nếu 'playGame' chưa tồn tại để check)
            Game gameChecker = (playGame != null) ? playGame : new Game();
            boolean hasSave = gameChecker.hasDiskSave();

            if (isPaused) {
                // --- KỊCH BẢN 1: ĐANG PAUSE (VD: Level 3 khi bạn nhấn "M") ---
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Game Paused");
                alert.setHeaderText("Bạn có một game đang tạm dừng!");
                alert.setContentText("Bạn muốn tiếp tục hay chơi mới?");

                ButtonType continueBtn = new ButtonType("Chơi tiếp (Level " + playGame.getCurrentMemoryLevel() + ")");
                ButtonType newGameBtn = new ButtonType("Chơi mới (Bắt đầu từ Level 1)");
                ButtonType cancelBtn = new ButtonType("Hủy", ButtonType.CANCEL.getButtonData());

                alert.getButtonTypes().setAll(continueBtn, newGameBtn, cancelBtn);
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == continueBtn) {
                    // --- Chọn "Chơi tiếp" ---
                    stage.setScene(playGame.getOrCreateGameScene(stage));
                    playGame.unpause();
                    playGame.startLoopIfNeeded();
                } else if (result.isPresent() && result.get() == newGameBtn) {
                    // --- Chọn "Chơi mới" ---
                    playGame.wipeDiskSave(); // Xóa save trên đĩa
                    playGame.restartGame();  // Reset instance 'playGame' về L1
                    stage.setScene(playGame.getOrCreateGameScene(stage));
                    playGame.unpause();
                    playGame.startLoopIfNeeded();
                }
                // (Nếu Hủy thì không làm gì)

            } else if (hasSave) {
                // --- KỊCH BẢN 2: KHÔNG PAUSE, NHƯNG CÓ SAVE TRÊN ĐĨA ---
                // (Đây là trường hợp của bạn: Chạy game và nó thấy file L2)
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Load Game");
                alert.setHeaderText("Bạn có một game đã lưu.");
                alert.setContentText("Bạn muốn tải game hay chơi mới?");

                ButtonType loadBtn = new ButtonType("Tải game (Level " + gameChecker.getLevelFromDiskSave() + ")");
                ButtonType newGameBtn = new ButtonType("Chơi mới (Bắt đầu từ Level 1)");
                ButtonType cancelBtn = new ButtonType("Hủy", ButtonType.CANCEL.getButtonData());

                alert.getButtonTypes().setAll(loadBtn, newGameBtn, cancelBtn);
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == loadBtn) {
                    // --- Chọn "Tải game" ---
                    playGame = new Game(); // Phải tạo game MỚI để load
                    playGame.setModePlay();
                    Scene s = playGame.getOrCreateGameScene(stage); // createGamescene() sẽ tự động TẢI
                    stage.setScene(s);
                    playGame.unpause();
                    playGame.startLoopIfNeeded();
                } else if (result.isPresent() && result.get() == newGameBtn) {
                    // --- Chọn "Chơi mới" ---
                    gameChecker.wipeDiskSave(); // Xóa save
                    playGame = new Game(); // Tạo game MỚI
                    playGame.setModePlay();
                    Scene s = playGame.getOrCreateGameScene(stage); // Sẽ bắt đầu L1
                    stage.setScene(s);
                    playGame.unpause();
                    playGame.startLoopIfNeeded();
                }
                // (Nếu Hủy thì không làm gì)

            } else {
                // --- KỊCH BẢN 3: KHÔNG PAUSE, KHÔNG SAVE ---
                // (Lần đầu chơi, hoặc vừa bị Game Over và file save đã bị xóa)
                playGame = new Game();
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

        ButtonUI logoutBtn = new ButtonUI("Log out");
        logoutBtn.setOnAction(e -> {
            AccountManager.logout();
            stage.setScene(create(stage));
        });

        root.getChildren().addAll(introduceBtn, playBtn, levelSelectBtn, settingsBtn, leaderboardBtn, exitBtn);
        root.getChildren().add(logoutBtn);
        return new Scene(root, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
    }
}