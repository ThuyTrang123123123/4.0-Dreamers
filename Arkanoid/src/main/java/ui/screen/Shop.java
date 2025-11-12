package ui.screen;

import core.Config;
import core.Game;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import systems.AudioSystem;
import ui.theme.Colors;
import ui.theme.Fonts;
import ui.theme.ThemeManager;
import ui.widgets.ButtonUI;

import java.util.Objects;

public class Shop {
    private final AudioSystem audio = AudioSystem.getInstance();
    private final Game game;
    public interface Listener { void onClose(); }
    private final Listener listener;

    public Shop(Game game, Listener listener) {
        this.game = game;
        this.listener = listener;
    }

    private static final int COST_C = 30;
    private static final int COST_T = 50;

    public Scene create(Stage stage) {
        if (game == null || game.getWorld() == null) {
            throw new IllegalStateException("Shop cần Game hiện tại (world != null). Hãy truyền instance Game đang chạy khi mở Shop.");
        }

        Label pointsLbl = new Label();
        pointsLbl.setFont(Fonts.main(22));
        pointsLbl.setTextFill(Colors.TEXTBT);
        Runnable refreshPoints = () -> {
            int wallet = game.getWorld().getScoring().getWallet();
            pointsLbl.setText("Scores you have: " + wallet
                    + "\n You need 30 score to buy Christmas Mode"
                    + "\n You need 50 score to buy LunarYear Mode"
                    );
        };
        refreshPoints.run();

        ToggleGroup g = new ToggleGroup();
        ToggleButton cBtn = new ToggleButton("Christmas Mode");
        ToggleButton tBtn = new ToggleButton("LunarYear Mode");
        for (ToggleButton b : new ToggleButton[]{cBtn, tBtn}) {
            b.setFont(Fonts.main(22));
            b.setPrefSize(200, 70);
        }
        cBtn.setToggleGroup(g); tBtn.setToggleGroup(g);
        cBtn.setSelected(true);
        final String[] currentKind = {"C"};

        g.selectedToggleProperty().addListener((o, ov, nv) -> {
            currentKind[0] = (nv == tBtn) ? "T" : "C";
            AudioSystem.getInstance().playSound("select.mp3");
        });

        HBox modes = new HBox(20, cBtn, tBtn);
        modes.setAlignment(Pos.CENTER);

        int maxLevel = Math.max(1, game.getWorld().getLevel().getMaxLevel());
        ComboBox<Integer> cbLevel = new ComboBox<>();
        for (int i = 1; i <= maxLevel; i++) cbLevel.getItems().add(i);
        cbLevel.getSelectionModel().selectFirst();

        Label lblLv = new Label("Level:");
        lblLv.setFont(Fonts.main(20));
        lblLv.setTextFill(Colors.TEXTBT);
        HBox levelRow = new HBox(10, lblLv, cbLevel);
        levelRow.setAlignment(Pos.CENTER);

        ButtonUI btnPreview = new ButtonUI("Preview");
        ButtonUI btnApply   = new ButtonUI("Apply");
        ButtonUI btnBack    = new ButtonUI("Back");
        ButtonUI btnReset   = new ButtonUI("Reset to Default");

        HBox actions = new HBox(20, btnPreview, btnApply, btnBack, btnReset);
        actions.setAlignment(Pos.CENTER);

        Label notEnough = new Label("YOU HAVE NO ENOUGH SCORES!");
        notEnough.setFont(Fonts.main(28));
        notEnough.setTextFill(Colors.TEXTBT);
        notEnough.setVisible(false);
        StackPane overlay = new StackPane(notEnough);
        StackPane.setAlignment(notEnough, Pos.CENTER);
        overlay.setMouseTransparent(true);

        VBox bottom = new VBox(16,
                pointsLbl,
                levelRow,
                actions
        );
        bottom.setAlignment(Pos.CENTER);
        bottom.setPadding(new Insets(10, 0, 20, 0));

        BorderPane bp = new BorderPane();
        bp.setCenter(modes);
        bp.setBottom(bottom);

        StackPane root = new StackPane(bp, overlay);
        Scene scene = new Scene(root, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);

        try {
            Image bg = new Image(Objects.requireNonNull(getClass().getResource("/images/SHOP.png")).toExternalForm());
            BackgroundImage bgi = new BackgroundImage(
                    bg,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
            );
            root.setBackground(new Background(bgi));
        } catch (Exception e) {
            System.out.println("KHÔNG LOAD ĐƯỢC ẢNH SHOP");
        }

        btnPreview.setOnAction(e -> {
            AudioSystem.getInstance().playSound("select.mp3");
            openPreview(stage, scene, cbLevel.getValue(), currentKind[0]);
        });

        btnApply.setOnAction(e -> {
            AudioSystem.getInstance().playSound("select.mp3");
            int cost = "T".equals(currentKind[0]) ? COST_T : COST_C;

            if (!game.getWorld().getScoring().spendWallet(cost)) {
                notEnough.setVisible(true);
                new Thread(() -> {
                    try { Thread.sleep(1200); } catch (InterruptedException ignored) {}
                    javafx.application.Platform.runLater(() -> notEnough.setVisible(false));
                }).start();
                return;
            }

            ThemeManager.setThemeForLevel(cbLevel.getValue(), currentKind[0]);
            game.getWorld().getLevel().invalidateBackground();
            refreshPoints.run();
        });

        btnBack.setOnAction(e -> {
            AudioSystem.getInstance().playSound("select.mp3");
            if (listener != null) listener.onClose();
        });

        btnReset.setOnAction(e -> {
            AudioSystem.getInstance().playSound("select.mp3");
            ThemeManager.resetThemeForLevel(cbLevel.getValue());
            game.getWorld().getLevel().invalidateBackground();;
        });

        return scene;
    }

    private void openPreview(Stage stage, Scene backScene, int level, String kind) {
        String path = ThemeManager.pathForKind(level, kind);
        StackPane root = new StackPane();
        try {
            Image img = new Image(Objects.requireNonNull(getClass().getResource(path)).toExternalForm());
            ImageView iv = new ImageView(img);
            iv.setFitWidth(Config.SCREEN_WIDTH);
            iv.setFitHeight(Config.SCREEN_HEIGHT);
            iv.setPreserveRatio(false);
            root.getChildren().add(iv);
        } catch (Exception e) {
            Label err = new Label("Không tìm thấy ảnh: " + path);
            err.setFont(Fonts.main(20));
            err.setTextFill(Colors.TEXT);
            root.getChildren().add(err);
            StackPane.setAlignment(err, Pos.CENTER);
        }

        Label hint = new Label("Press ENTER to back to menu");
        hint.setFont(Fonts.main(18));
        hint.setTextFill(Colors.TEXTBT);
        StackPane.setAlignment(hint, Pos.BOTTOM_CENTER);
        StackPane.setMargin(hint, new Insets(20));
        root.getChildren().add(hint);

        Scene previewScene = new Scene(root, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
        previewScene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                AudioSystem.getInstance().playSound("select.mp3");
                stage.setScene(backScene);
            }
        });
        stage.setScene(previewScene);
    }
}
