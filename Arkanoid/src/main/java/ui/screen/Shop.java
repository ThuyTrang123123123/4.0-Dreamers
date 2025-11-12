package ui.screen;

import core.Config;
import core.Game;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private final Config config = new Config();
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
       Label pointsLbl = new Label();
        pointsLbl.setFont(Fonts.main(22));
        pointsLbl.setTextFill(Colors.TEXTBT);
        Runnable refreshPoints = () -> {
            int pts = game.getWorld().getScoring().getPoints();
            pointsLbl.setText("Scores you have: " + pts + "\n"
                    + "You need 30 scores to buy Christmas Mode"
                    + "\nYou need 50 scores to buy LunarYear Mode");
        };
        refreshPoints.run();

        ToggleGroup g = new ToggleGroup();
        ToggleButton cBtn = new ToggleButton("Christmas Mode");
        ToggleButton tBtn = new ToggleButton("LunarYear Mode");
        for (ToggleButton b : new ToggleButton[]{cBtn, tBtn}) {
            b.setFont(Fonts.main(20));
            b.setPrefSize(180, 60);
        }
        cBtn.setToggleGroup(g); tBtn.setToggleGroup(g);
        cBtn.setSelected(true);
        String[] currentKind = {"C"};

        g.selectedToggleProperty().addListener((o, ov, nv) -> {
            currentKind[0] = (nv == tBtn) ? "T" : "C";
            audio.playSound("select.mp3");
        });

        ComboBox<Integer> cbLevel = new ComboBox<>();
        for (int i = 1; i <= 12; i++) cbLevel.getItems().add(i);
        cbLevel.getSelectionModel().selectFirst();
        Label lblLv = new Label("Level:");
        lblLv.setFont(Fonts.main(20));
        lblLv.setTextFill(Colors.TEXTBT);
        HBox levelRow = new HBox(10, lblLv, cbLevel);
        levelRow.setAlignment(Pos.CENTER);

        ButtonUI btnPreview = new ButtonUI("Preview");
        ButtonUI btnApply   = new ButtonUI("Apply");
        ButtonUI btnBack    = new ButtonUI("Back");
        ButtonUI btnReset = new ButtonUI("Set up");

        HBox actions = new HBox(20, btnPreview, btnApply, btnBack, btnReset);
        actions.setAlignment(Pos.CENTER);

        Label notEnough = new Label("YOU HAVE NO ENOUGH SCORES!");
        notEnough.setFont(Fonts.main(28));
        notEnough.setTextFill(Colors.TEXTBT);
        notEnough.setVisible(false);
        StackPane overlay = new StackPane(notEnough);
        StackPane.setAlignment(notEnough, Pos.CENTER);

        overlay.setMouseTransparent(true);

        HBox modes = new HBox(20, cBtn, tBtn);
        modes.setAlignment(Pos.CENTER);

        VBox bottom = new VBox(16,
                pointsLbl,
                levelRow,
                new HBox(20, btnPreview, btnApply, btnBack, btnReset)
        );
        ((HBox) bottom.getChildren().get(2)).setAlignment(Pos.CENTER);
        bottom.setAlignment(Pos.CENTER);
        bottom.setPadding(new Insets(10, 0, 20, 0));

        BorderPane bp = new BorderPane();
        bp.setCenter(modes);
        bp.setBottom(bottom);

        StackPane root = new StackPane(bp, overlay);

        Scene scene = new Scene(root, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);

        Image bg = new Image(
                Objects.requireNonNull(
                        getClass().getResource("/images/SHOP.png")
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

        btnPreview.setOnAction(e -> {
            audio.playSound("select.mp3");
            openPreview(stage, scene, cbLevel.getValue(), currentKind[0]);
        });

        btnApply.setOnAction(e -> {
//            audio.playSound("ok.mp3");
            audio.playSound("select.mp3");
            int cost = currentKind[0].equals("T") ? COST_T : COST_C;
            if (!game.getWorld().getScoring().spendPoints(cost)) {
                notEnough.setVisible(true);
                new Thread(() -> {
                    try { Thread.sleep(1200); } catch (InterruptedException ignored) {}
                    javafx.application.Platform.runLater(() -> notEnough.setVisible(false));
                }).start();
//                audio.playSound("error.mp3");
                return;
            }
            ThemeManager.setThemeForLevel(cbLevel.getValue(), currentKind[0]);
            refreshPoints.run();
//            audio.playSound("success.mp3");
        });

        btnBack.setOnAction(e -> {
//            audio.playSound("back.mp3");
            audio.playSound("select.mp3");
            if (listener != null) listener.onClose();
        });

        btnReset.setOnAction(e -> {
            audio.playSound("select.mp3");
            ThemeManager.resetThemeForLevel(cbLevel.getValue());
            game.getWorld().getLevel().invalidateBackground();  // xoá cache ảnh cũ
//            audio.playSound("success.mp3");
        });

        return scene;
    }

    private void openPreview(Stage stage, Scene backScene, int level, String kind) {
        String path = ThemeManager.pathForKind(level, kind);
        StackPane root = new StackPane();
        try {
            ImageView iv = new ImageView(new Image(
                    Objects.requireNonNull(getClass().getResource(path)).toExternalForm()));
            iv.setFitWidth(Config.SCREEN_WIDTH);
            iv.setFitHeight(Config.SCREEN_HEIGHT);
            root.getChildren().add(iv);
        } catch (Exception e) {
            Label err = new Label("Không tìm thấy ảnh: " + path);
            err.setFont(Fonts.main(20));
            err.setTextFill(Colors.TEXT);
            root.getChildren().add(err);
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
//                audio.playSound("back.mp3");
                stage.setScene(backScene);
            }
        });
        stage.setScene(previewScene);
    }
}
