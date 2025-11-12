package ui.screen;

import core.Config;
import core.Game;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import systems.AudioSystem;
import ui.screen.InGame;
import ui.theme.Colors;
import ui.theme.Fonts;

import data.repositories.PlayerRepository;
import data.repositories.ScoreRepository;
import ui.widgets.ButtonUI;

import java.util.ArrayList;
import java.util.List;

/**
 * LevelSelect – Màn hình chọn cấp độ chơi, cho phép người chơi chọn màn chơi cụ thể.
 */
public class LevelSelect {
    private final Stage stage;
    private final Game game;
    private final PlayerRepository playerRepo;
    private final ScoreRepository scoreRepo;
    private final int totalLevels;

    private int highestLevelUnlocked;
    private final int[] bestScores;
    private final List<Button> levelButtons = new ArrayList<>();
    private final List<Label> scoreLabels  = new ArrayList<>();

    public LevelSelect(Stage stage, Game game,
                       PlayerRepository playerRepo,
                       ScoreRepository scoreRepo) {
        this.stage = stage;
        this.game = game;
        this.playerRepo = playerRepo;
        this.scoreRepo = scoreRepo;

        this.totalLevels = 12;
        this.bestScores = new int[totalLevels];

        // === LOAD PROGRESS ===
        this.highestLevelUnlocked = 12;
        for (int lv = 1; lv <= totalLevels; lv++) {
            bestScores[lv - 1] = safeGetBestScore(lv);
        }
    }

    public Scene create() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(24));

        VBox header = new VBox(6);
        Label title = new Label("Select Level");
        title.setTextFill(Colors.TEXTBT);
        title.setFont(Fonts.main(28));
        Label sub = new Label("Practice Mode — Select Any Leve");
        sub.setTextFill(Colors.TEXTBT);
        sub.setFont(Fonts.main(13));
        header.getChildren().addAll(title, sub);

        GridPane grid = buildGrid();

        VBox footer = new VBox(12);
        footer.setAlignment(Pos.CENTER_LEFT);
        ButtonUI backBtn = new ButtonUI("Back");
        backBtn.setOnAction(e -> {
            AudioSystem.getInstance().playSound("select.mp3");
            stage.setScene(MainMenu.cachedScenePractice);
        });
        footer.getChildren().add(backBtn);

        root.setTop(header);
        BorderPane.setMargin(header, new Insets(0, 0, 12, 0));
        root.setCenter(grid);
        root.setBottom(footer);

        Scene scene = new Scene(root, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) stage.setScene(MainMenu.cachedScene);
        });

        refreshButtons();
        return scene;
    }

    /**
     * Gọi khi người chơi WIN 1 màn (từ InGame/End)
     * - Cập nhật best score
     * - Mở khóa level kế tiếp
     * - Lưu tiến độ
     */
    public void onWinLevel(int level, int score) {
        if (level < 1 || level > totalLevels) return;
        // best score
        bestScores[level - 1] = Math.max(bestScores[level - 1], score);
        scoreRepo.saveBestScoreIfHigher(level, bestScores[level - 1]);

        if (level >= highestLevelUnlocked && level < totalLevels) {
            highestLevelUnlocked = level + 1;
            playerRepo.setHighestLevelUnlocked(highestLevelUnlocked);
        } else if (level == totalLevels) {
            playerRepo.setHighestLevelUnlocked(highestLevelUnlocked);
        }

        refreshButtons();
    }

    public void onUpdateScoreOnly(int level, int score) {
        if (level < 1 || level > totalLevels) return;
        if (score > bestScores[level - 1]) {
            bestScores[level - 1] = score;
            scoreRepo.saveBestScoreIfHigher(level, score);
            refreshButtons();
        }
    }

    private GridPane buildGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(18);
        grid.setVgap(22);
        int cols = 5;

        for (int i = 1; i <= totalLevels; i++) {
            VBox cell = new VBox(6);
            cell.setAlignment(Pos.CENTER);

            Button b = makeLevelButton(i);
            Label score = new Label("—");
            score.setFont(Fonts.main(12));
            score.setTextFill(Colors.TEXTBT);

            levelButtons.add(b);
            scoreLabels.add(score);
            cell.getChildren().addAll(b, score);

            grid.add(cell, (i - 1) % cols, (i - 1) / cols);
        }
        return grid;
    }

    private Button makeLevelButton(int level) {
        Button b = makeButton(String.valueOf(level), 78, 78, () -> startLevel(level));
        b.getStyleClass().add("level-btn");
        return b;
    }

    private Button makeButton(String text, double w, double h, Runnable onClick) {
        Button btn = new Button(text);
        btn.setFont(Fonts.main(18));
        btn.setTextFill(Colors.TEXTBT);
        btn.setBackground(new Background(
                new BackgroundFill(Colors.BUTTON, new CornerRadii(12), Insets.EMPTY)
        ));
        btn.setMinSize(w, h);
        btn.setOnAction(e -> onClick.run());
        return btn;
    }

    private void refreshButtons() {
        for (int i = 1; i <= totalLevels; i++) {
            Button b = levelButtons.get(i - 1);
            Label s = scoreLabels.get(i - 1);

            boolean unlocked = true;

            b.setDisable(!unlocked);
            b.setOpacity(unlocked ? 1.0 : 0.45);
            b.setBackground(new Background(new BackgroundFill(
                    Colors.TEXT,
                    new CornerRadii(12), Insets.EMPTY)));

            int best = bestScores[i - 1];
            s.setText(best > 0 ? ("Best: " + best) : "—");
            s.setTextFill(Colors.TEXTBT);
        }
    }

    private void startLevel(int level) {
        game.startLevelFromSelect(stage, level);
    }

    private int safeGetHighest() {
        try {
            int v = playerRepo.getHighestLevelUnlocked();
            return v <= 0 ? 1 : v;
        } catch (Exception e) {
            return 1;
        }
    }

    private int safeGetBestScore(int level) {
        try {
            return Math.max(0, scoreRepo.getBestScore(level));
        } catch (Exception e) {
            return 0;
        }
    }
}
