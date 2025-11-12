package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.AccountManager;
import entities.Bullet;
import entities.bricks.ExplodingBrick;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import engine.Collision;
import engine.GameLoop;
import engine.*;

import entities.Ball;
import entities.Paddle;
import entities.bricks.Brick;
import entities.powerups.PowerUp;

import systems.AchievementSystem;
import systems.AudioSystem;

import data.JsonStorage;
import data.Storage;
import data.repositories.PlayerRepository;
import data.repositories.ScoreRepository;

import net.LeaderboardClient;
import net.MockServer;

import ui.screen.*;
import ui.theme.Colors;
import ui.theme.Fonts;

import javafx.scene.paint.Color;
import javafx.geometry.Insets;

public class Game extends Application {
    private Canvas canvas;
    private GraphicsContext gc;
    private final World world = new World();
    private InGame hudLayer;
    private boolean gamePaused = false;
    private boolean gameWon = false;
    private Stage stage;
    private Scene inGameScene;
    private Scene pauseScene;
    private Scene mainMenuScene;
    private GameLoop loop;
    private Storage storage = new JsonStorage();
    private PlayerRepository playerRepo = new PlayerRepository(storage);
    private ScoreRepository scoreRepo = new ScoreRepository(storage);
    private LeaderboardClient leaderboardClient = new LeaderboardClient();
    private MockServer mockServer;
    private enum Mode { PLAY, PRACTICE }
    private Mode mode = Mode.PLAY;

    private boolean levelFinished = false;
    private boolean isPracticeMode = false;
    private boolean justCompleted = false;
    private boolean scoreSavedForGameOver = false;  // Flag cho game over
    private boolean scoreSavedForWin = false;// Flag cho win
    private boolean endScreenShown = false;

    //private static final String PROGRESS_PLAY = "progress_play";
    private static final String SCORES_PLAY   = "scores_play";
    private static final String SCORES_PRACT  = "scores_practice";


    public Scene createGamescene(Stage stage) {
        this.stage = stage;
        canvas = new Canvas(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        world.init(canvas);

        if (mode == Mode.PLAY) {
            Map<String, Object> progress = storage.load(getDynamicProgressKey());
            if (!progress.isEmpty()) {
                int loadedScore  = ((Number) progress.getOrDefault("score", 0)).intValue();
                int loadedLives  = ((Number) progress.getOrDefault("lives", 1)).intValue();
                int loadedBricks = ((Number) progress.getOrDefault("bricksDestroyed", 0)).intValue();
                int savedLevel   = ((Number) progress.getOrDefault("currentLevel", 1)).intValue();
                int rankIndex    = ((Number) progress.getOrDefault("rankIndex", 0)).intValue();

                world.getScoring().scoreProperty().set(loadedScore);
                world.getScoring().livesProperty().set(loadedLives);
                world.getScoring().bricksDestroyedProperty().set(loadedBricks);
                world.getLevel().setCurrentLevel(savedLevel);

                world.getAchievements().currentRankIndexProperty().set(rankIndex);
                world.getAchievements().currentRankNameProperty().set(world.getAchievements().getCurrentRank().getName());
                world.getAchievements().currentRankIconProperty().set(world.getAchievements().getCurrentRank().getIcon());

                Object unlockedObj = progress.get("unlockedAchievements");
                if (unlockedObj instanceof List) {
                    for (Object idObj : (List<?>) unlockedObj) {
                        if (idObj instanceof String) world.getAchievements().unlockAchievement((String) idObj);
                    }
                }
            }
        }

        System.out.println("Đã load progress: Level " + world.getLevel().getCurrentLevel());

        hudLayer = new InGame(this, world.getScoring(), world.getAchievements());
        HBox hud = hudLayer.createHUD();

        StackPane root = new StackPane(canvas, hud);
        StackPane.setAlignment(hud, Pos.TOP_LEFT);
        world.getAchievements().initNotificationSystem(root);

        Scene scene = new Scene(root, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
        this.inGameScene = scene;

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.M) {
                gamePaused = true;
                if (loop != null) loop.stop();

                Scene menuScene = (mode == Mode.PLAY) ? MainMenu.cachedScene : MainMenu.cachedScenePractice;
                if (menuScene == null) {
                    menuScene = new MainMenu().create(stage);
                    if (mode == Mode.PLAY) MainMenu.cachedScene = menuScene;
                    else MainMenu.cachedScenePractice = menuScene;
                }
                stage.setScene(menuScene);
                return;
            }


            if (e.getCode() == KeyCode.ESCAPE) {
                showPause();
                return;
            }

            if (e.getCode() == KeyCode.C) {
                resumeGame();
                return;
            }

            if (e.getCode() == KeyCode.R) {
                restartGame();
                return;
            }

            if (e.getCode() == KeyCode.SPACE) {
                if (gamePaused || world.getScoring().isGameOver() || gameWon || levelFinished) {
                    return;
                }

                for (Ball ball : world.getBalls()) {
                    if (ball.isStickToPaddle()) ball.launch();
                }
                return;
            }

            if (mode == Mode.PRACTICE && e.getCode() == KeyCode.ENTER ) {

                gamePaused = true;
                if (loop != null) loop.stop();

                var storage = new JsonStorage();
                var playerRepo = new PlayerRepository(storage);
                var scoreRepo  = new ScoreRepository(storage);
                LevelSelect select = new LevelSelect(stage, this, playerRepo, scoreRepo);
                stage.setScene(select.create());
                return;
            }

            world.getPaddle().onKeyPressed(e.getCode());
        });

        scene.setOnKeyReleased(e -> world.getPaddle().onKeyReleased(e.getCode()));

        AudioSystem audio = AudioSystem.getInstance();
        audio.playIfChanged(audio.getSelectedMusicOrDefault(Config.DEFAULT_MUSIC));

        loop = new GameLoop(this);
        loop.start();

        return scene;
    }

    public void update(double dt) {
        if (gamePaused || gameWon) return;
        if (mode == Mode.PRACTICE && levelFinished) return;

        Paddle paddle = world.getPaddle();
        List<Ball> balls = world.getBalls();

        List<Ball> lostBalls = new ArrayList<>();

        for (Ball ball : balls) {
            if (ball.isStickToPaddle()) {
                ball.updateStickPosition(paddle.getX(), paddle.getY());
            }

            if (ball.isLost()) {
                lostBalls.add(ball);
                continue;
            }

            ball.update(dt);

            if (!ball.isStickToPaddle()) {
                Collision.handleBallWallCollision(ball, canvas.getWidth(), canvas.getHeight());
                if (Collision.isBallTouchingPaddle(ball, paddle)) {
                    Collision.handleBallPaddleCollision(ball, paddle);
                }
                Collision.handleBallBrickCollision(ball, world.getBricks(), world);
            }
        }
        for (Brick brick : world.getBricks()) {
            if (brick instanceof ExplodingBrick) {
                ExplodingBrick eb = (ExplodingBrick) brick;
                if (brick.isDestroyed() && !eb.hasExploded()) {
                    PlayExploding explosion = new PlayExploding(3, 3, brick.getX(), brick.getY());
                    explosion.start(gc); // bắt đầu animation
                    eb.setHasExploded(true); // chỉ gọi 1 lần hiệu ứng
                }
            }
        }


        // hết sửa

        world.getBalls().removeAll(lostBalls);
        if (!lostBalls.isEmpty() && world.getBalls().isEmpty()) {
            world.getScoring().loseLife();
        }

        if (world.getBalls().isEmpty() && !world.getScoring().isGameOver()) {
            Ball newBall = new Ball(
                    Config.SCREEN_WIDTH / 2.0,
                    Config.SCREEN_HEIGHT - 70,
                    Config.BALL_RADIUS,
                    Config.BALL_SPEED
            );
            newBall.setStickToPaddle(true);
            world.getBalls().add(newBall);
        }

        paddle.update(dt);

        List<Bullet> bullets = world.getBullets();

        Bullet b = paddle.tryShoot();
        if (b != null) bullets.add(b);

        bullets.removeIf(bu -> {
            bu.update(dt);
            return !bu.isActive();
        });

        Collision.handleBulletBrickCollision(world.getBullets(), world.getBricks(), world);

        List<PowerUp> toRemove = new ArrayList<>();
        for (PowerUp pu : world.getPowerUps()) {
            pu.update(dt);
            if (Collision.isPowerUpTouchingPaddle(pu, paddle)) {
                Collision.handlePowerUpCollision(pu, paddle, world);
                world.getPowerUpPool().release(pu);
                toRemove.add(pu);
            }
        }
        world.getPowerUps().removeAll(toRemove);

        if (world.getLevel().isComplete() && !justCompleted) {
            justCompleted = true;

            if (mode == Mode.PRACTICE) {
                levelFinished = true;

                scoreRepo.saveBestScoreIfHigher(world.getLevel().getCurrentLevel(),
                        world.getScoring().getScore());
            } else {
                if (world.getLevel().isGameComplete()) {
                    gameWon = true;
                } else {
                    world.nextLevel();
                }
                saveProgress();
            }
        } else if (!world.getLevel().isComplete()) {
            justCompleted = false;
        }
    }

    public void render() {
        Image bg = world.getLevel().getBackgroundImage();
        gc.drawImage(bg, 0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Colors.TEXT);
        gc.setFont(Fonts.main(20));
        gc.fillText("Level " + world.getLevel().getCurrentLevel() + " / " + world.getLevel().getMaxLevel(),
                Config.SCREEN_WIDTH - 170, 30);

        world.getPaddle().render(gc);
        hudLayer.updateHUD();

        for (Ball ball : world.getBalls()) {
            ball.render(gc);
        }

        for (Brick b : world.getBricks()) {
            if (!b.isDestroyed()) b.render(gc);
        }

        for (Bullet b : world.getBullets()) {
            b.render(gc);
        }

        for (PowerUp pu : world.getPowerUps()) {
            pu.render(gc);
        }

        if (world.getBalls().stream().anyMatch(Ball::isStickToPaddle)) {
            gc.setFill(Colors.TEXT);
            gc.setFont(Fonts.main(18));
            gc.fillText("Press SPACE to launch ball", 320, 260);
        }

        if (gameWon && !scoreSavedForWin) {
            scoreSavedForWin = true;
            saveScoreOnce("WIN");
        }

        if (gameWon) {
            endScreenShown = true;
            goToEndScreen(world.getScoring().getScore());
            return;
        }

        if (world.getScoring().isGameOver() && !scoreSavedForGameOver) {
            scoreSavedForGameOver = true;
            saveScoreOnce("GAME_OVER");
        }

        if (world.getScoring().isGameOver()) {
            gc.setFill(Colors.TEXT);
            gc.setFont(Fonts.main(28));
            gc.fillText("GAME OVER", 320, 280);
            gc.setFont(Fonts.main(16));
            gc.fillText("Press R to Restart", 320, 320);
        }

        if (gamePaused) {
            gc.setFill(Colors.TEXT);
            gc.setFont(Fonts.main(24));
            gc.fillText("GAME PAUSED", 320, 260);
            gc.setFont(Fonts.main(16));
            gc.fillText("Press C to Continue", 330, 300);
        }

        if (mode == Mode.PRACTICE && levelFinished) {
            gc.setFill(Colors.TEXT);
            gc.setFont(Fonts.main(18));
            gc.fillText("Level finished! Press ENTER to choose another level", 210, 420);
        }
    }

    /**
     * Lấy key (tên file) để lưu/tải progress, dựa trên người dùng đã đăng nhập.
     */
    private String getDynamicProgressKey() {
        String username = AccountManager.getLoggedInUser();
        if (username == null || username.isEmpty()) {
            return "progress_guest"; // Dùng file riêng cho khách (Guest)
        }
        // Thay thế các ký tự không an toàn trong tên file
        String safeUsername = username.replaceAll("[^a-zA-Z0-9_.-]", "_");
        return "progress_" + safeUsername;
    }

    private void saveProgress() {
        if (mode != Mode.PLAY) return;

        Map<String, Object> progress = new HashMap<>();
        progress.put("score", world.getScoring().getScore());
        progress.put("lives", world.getScoring().getLives());
        progress.put("bricksDestroyed", world.getScoring().getBricksDestroyed());
        progress.put("currentLevel", world.getLevel().getCurrentLevel());
        progress.put("rankIndex", world.getAchievements().getCurrentRankIndex());

        List<String> unlockedIds = new ArrayList<>();
        for (AchievementSystem.Achievement ach : world.getAchievements().getUnlockedAchievements()) {
            unlockedIds.add(ach.getId());
        }
        progress.put("unlockedAchievements", unlockedIds);

        storage.save(getDynamicProgressKey(), progress);

        scoreRepo.saveBestScoreIfHigher(world.getLevel().getCurrentLevel(), world.getScoring().getScore());

        System.out.println("Progress saved: Level " + world.getLevel().getCurrentLevel());
    }

    private void saveScoreOnce(String reason) {
        int finalScore = world.getScoring().getScore();
        int finalLevel = world.getLevel().getCurrentLevel();

        // Lưu local
        scoreRepo.saveScore(finalLevel, finalScore);
        String playerName = AccountManager.getLoggedInUser();
        if (playerName == null || playerName.isEmpty()) {
            playerName = playerRepo.getPlayerName(); // Fallback về "Guest"
        }

        // Gửi lên leaderboard
        leaderboardClient.submitScore(playerName, finalScore);

        System.out.println("Score saved ONCE for " + reason + " as " + playerName + ": " + finalScore);

        // Nếu game kết thúc (thắng hoặc thua), xóa file "progress" (run này đã kết thúc)
        if (reason.equals("GAME_OVER") || reason.equals("WIN")) {
            storage.delete(getDynamicProgressKey());
            System.out.println("Run finished. Deleting progress file: " + getDynamicProgressKey());
        }
    }

    public void showPause() {
        gamePaused = true;
    }

    public void resumeGame() {
        gamePaused = false;
    }

    private void restartGameFromPause() {
        restartGame();
        stage.setScene(inGameScene);
    }

    public void restartGame() {
        gamePaused = false;
        gameWon = false;
        justCompleted = false;
        scoreSavedForGameOver = false;
        scoreSavedForWin = false;

        if (mode == Mode.PLAY) {
            world.reset();
            world.getLevel().setCurrentLevel(1);
            storage.delete(getDynamicProgressKey());
            System.out.println("Restart PLAY mode → back to Level 1");
        }
        else if (mode == Mode.PRACTICE) {
            int currentLevel = world.getLevel().getCurrentLevel();
            world.reset();
            world.getLevel().setCurrentLevel(currentLevel);
            System.out.println("Restart PRACTICE mode → stay at Level " + currentLevel);
        }
    }

    public void showMainMenu() {
        gamePaused = false;
        stage.setScene(mainMenuScene);
    }

    public Scene getOrCreateGameScene(Stage stage) {
        if (inGameScene == null) {
            inGameScene = createGamescene(stage);
        }
        return inGameScene;
    }

    public void unpause() {
        this.gamePaused = false;
    }

    public void startLoopIfNeeded() {
        if (loop == null || !loop.isRunning()) {
            loop = new GameLoop(this);
            loop.start();
        }
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public boolean getGamestatus() {
        return !gamePaused;
    }

    public void showLeaderboard() {
        // 1. Lấy Top 10 Toàn Cầu (Giữ nguyên)
        List<Map<String, Object>> topScores = leaderboardClient.getTopScores(10);

        VBox leaderboardBox = new VBox(10);
        leaderboardBox.setAlignment(Pos.CENTER);
        leaderboardBox.setStyle("-fx-padding: 20; -fx-background-color: #FFF8F6;"); // Cập nhật màu nền

        Label title = new Label("BXH");
        title.setFont(Fonts.main(28));
        title.setTextFill(Colors.PRIMARY);
        leaderboardBox.getChildren().add(title);

        // 2. Hiển thị Top 10 Toàn Cầu (Giữ nguyên)
        for (int i = 0; i < topScores.size(); i++) {
            Map<String, Object> entry = topScores.get(i);

            int rank = i + 1;
            String player = (String) entry.get("player");
            int score = ((Number) entry.get("score")).intValue();

            String text = rank + ". " + player + ": " + score;
            Label scoreLabel = new Label(text);
            scoreLabel.setFont(Fonts.main(16));

            if (rank == 1) {
                scoreLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #D4AF37;"); // Vàng
            } else if (rank == 2) {
                scoreLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #A9A9A9;"); // Bạc
            } else if (rank == 3) {
                scoreLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #CD7F32;"); // Đồng
            }

            leaderboardBox.getChildren().add(scoreLabel);
        }

        // 3. Thêm một đường kẻ phân cách
        // (Tạo khoảng cách trực quan)
        VBox.setMargin(title, new Insets(0, 0, 15, 0)); // Thêm margin dưới title
        javafx.scene.shape.Line separator = new javafx.scene.shape.Line(0, 0, 300, 0);
        separator.setStroke(Color.web("#E0E0E0"));
        VBox.setMargin(separator, new Insets(15, 0, 15, 0)); // Thêm margin trên và dưới
        leaderboardBox.getChildren().add(separator);


        // 4. LOGIC MỚI: Hiển thị Thành Tích Cá Nhân
        String currentUser = AccountManager.getLoggedInUser();

        // Chỉ hiển thị nếu có người dùng đăng nhập (không phải Guest)
        if (currentUser != null && !currentUser.isEmpty()) {

            // Lấy Level cao nhất (từ file progress_User.json)
            // Phương thức này đã được chúng ta sửa để đọc theo tài khoản
            int personalBestLevel = getHighestLevelUnlocked();

            // Lấy Điểm cao nhất (từ file scores_User.json)
            // Chúng ta cần duyệt qua file điểm cá nhân để tìm điểm cao nhất
            ScoreRepository personalScoreRepo = new ScoreRepository(storage);
            List<Map<String, Object>> allMyScores = personalScoreRepo.getAllHighScores();

            int personalBestScore = 0;
            for (Map<String, Object> entry : allMyScores) {
                int score = ((Number) entry.get("score")).intValue();
                if (score > personalBestScore) {
                    personalBestScore = score;
                }
            }

            // Tạo các Label UI
            Label personalTitle = new Label(currentUser + "'s Records");
            personalTitle.setFont(Fonts.main(22));
            personalTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #2C7A7B;"); // Màu teal
            VBox.setMargin(personalTitle, new Insets(0, 0, 10, 0));

            Label bestScoreLabel = new Label("Best Score: " + personalBestScore);
            bestScoreLabel.setFont(Fonts.main(18));

            Label bestLevelLabel = new Label("Highest Level: " + personalBestLevel);
            bestLevelLabel.setFont(Fonts.main(18));

            leaderboardBox.getChildren().addAll(personalTitle, bestScoreLabel, bestLevelLabel);

        } else {
            // Nếu là Guest, hiển thị lời nhắc
            Label guestLabel = new Label("Log in to view your records!");
            guestLabel.setFont(Fonts.main(16));
            guestLabel.setStyle("-fx-text-fill: #777777;");
            leaderboardBox.getChildren().add(guestLabel);
        }
        // Kết thúc Logic mới


        //  5. Hiển thị cửa sổ (Giữ nguyên)
        Scene leaderboardScene = new Scene(leaderboardBox, 400, 600);
        Stage leaderboardStage = new Stage();
        leaderboardStage.setTitle("Leaderboard");
        leaderboardStage.setScene(leaderboardScene);
        leaderboardStage.show();
    }

    public void startLevelFromSelect(Stage stage, int level) {
        mode = Mode.PRACTICE;
        gamePaused = false;
        gameWon = false;
        justCompleted = false;
        scoreSavedForGameOver = false;
        scoreSavedForWin = false;
        levelFinished = false;

        if (canvas == null) {
            canvas = new Canvas(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
            gc = canvas.getGraphicsContext2D();
        }

        world.init(canvas);
        world.reset();
        world.getLevel().setCurrentLevel(level);

        inGameScene = createGamescene(stage);
        stage.setScene(inGameScene);
        startLoopIfNeeded();
    }


    public int getHighestLevelUnlocked() {
        try {
            // Đọc từ file scores vĩnh viễn thay vì file progress
            ScoreRepository personalScoreRepo = new ScoreRepository(storage);
            List<Map<String, Object>> allMyScores = personalScoreRepo.getAllHighScores();

            int highestLevelFound = 1; // Mặc định là 1

            // Duyệt qua tất cả các điểm đã lưu
            for (Map<String, Object> entry : allMyScores) {
                // Chúng ta cần ép kiểu (cast) an toàn
                int level = ((Number) entry.get("level")).intValue();
                if (level > highestLevelFound) {
                    highestLevelFound = level;
                }
            }
            // Trả về level cao nhất bạn từng chơi
            return highestLevelFound;

        } catch (Exception e) {
            System.err.println("Lỗi khi đọc highest level: " + e.getMessage());
            return 1; // Trả về 1 nếu có lỗi
        }
    }


    public void setModePlay()     {
        mode = Mode.PLAY;
        inGameScene = null;
        gamePaused = false;
        gameWon = false;
        justCompleted = false;
        scoreSavedForGameOver = false;
        scoreSavedForWin = false;
    }

    public void setModePractice() {
        mode = Mode.PRACTICE;
        inGameScene = null;
        gamePaused = false;
        gameWon = false;
        justCompleted = false;
        scoreSavedForGameOver = false;
        scoreSavedForWin = false;
    }

    private void goToEndScreen(int finalScore) {
        if (loop != null) loop.stop();
        Integer best = null;

        Runnable onReplay = () -> {
            startNewRun();
        };

        Runnable onMain = () -> {
            openMainMenu();
        };

        Platform.runLater(() -> {
            Scene endScene = End.create(
                    stage,
                    finalScore,
                    best,
                    onReplay,
                    onMain,
                    "/images/END.png"
            );
            stage.setScene(endScene);
        });
    }

    private void startNewRun() {
        gamePaused = false;
        gameWon = false;
        justCompleted = false;
        scoreSavedForGameOver = false;
        scoreSavedForWin = false;
        endScreenShown = false;

        if (mode == Mode.PLAY) {
            world.reset();
            world.getLevel().setCurrentLevel(1);
            storage.delete(getDynamicProgressKey());
        } else {
            int currentLevel = world.getLevel().getCurrentLevel();
            world.reset();
            world.getLevel().setCurrentLevel(currentLevel);
        }

        inGameScene = createGamescene(stage);
        stage.setScene(inGameScene);
        startLoopIfNeeded();
    }

    private void openMainMenu() {
        gamePaused = false;
        gameWon = false;
        justCompleted = false;
        scoreSavedForGameOver = false;
        scoreSavedForWin = false;
        endScreenShown = false;

        Scene menuScene = (mode == Mode.PLAY) ? MainMenu.cachedScene : MainMenu.cachedScenePractice;
        if (menuScene == null) {
            menuScene = new MainMenu().create(stage);
            if (mode == Mode.PLAY) MainMenu.cachedScene = menuScene;
            else MainMenu.cachedScenePractice = menuScene;
        }
        stage.setScene(menuScene);
    }

    public boolean hasExistingScene() {
        return inGameScene != null;
    }

    public boolean isPlayMode() {
        return mode == Mode.PLAY;
    }

    public void resumeFromMenu(Stage stage) {
        if (inGameScene == null) {
            inGameScene = createGamescene(stage);
        }
        stage.setScene(inGameScene);
        unpause();
        startLoopIfNeeded();
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        mockServer = new MockServer();
        try {
            mockServer.start();
        } catch (Exception e) {
            System.err.println("Lỗi khởi động MockServer: " + e.getMessage());
        }

        stage.setTitle("Arkanoid");
        stage.setScene(new MainMenu().create(stage));
        stage.show();

        stage.setOnCloseRequest(e -> {
            AudioSystem.getInstance().dispose();
            mockServer.stop();
        });
    }
    // Trong core/Game.java

    /** Kiểm tra xem có file save trên đĩa không */
    public boolean hasDiskSave() {
        Map<String, Object> progress = storage.load(getDynamicProgressKey());
        return !progress.isEmpty();
    }

    /** Lấy level từ file save trên đĩa */
    public int getLevelFromDiskSave() {
        Map<String, Object> progress = storage.load(getDynamicProgressKey());
        if (!progress.isEmpty()) {
            return ((Number) progress.getOrDefault("currentLevel", 1)).intValue();
        }
        return 1;
    }

    /** Lấy level hiện tại đang chạy trong RAM (khi pause) */
    public int getCurrentMemoryLevel() {
        // Sửa lỗi: Phải là world.getLevel()
        if (world != null && world.getLevel() != null) {
            return world.getLevel().getCurrentLevel();
        }
        return 1;
    }

    /** Xóa file save (dùng cho nút "New Game") */
    public void wipeDiskSave() {
        storage.delete(getDynamicProgressKey());
        System.out.println("Disk save wiped: " + getDynamicProgressKey());
    }

    /** Kiểm tra xem game đã "kết thúc" (Game Over/Win) chưa */
    public boolean isGameFinished() {
        // Đảm bảo world và scoring đã được khởi tạo
        if (world == null || world.getScoring() == null) {
            return false; // Mặc định là false nếu chưa load
        }
        return world.getScoring().isGameOver() || gameWon;
    }
}