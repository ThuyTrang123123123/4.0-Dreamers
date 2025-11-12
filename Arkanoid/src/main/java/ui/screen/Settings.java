package ui.screen;

import core.Config;
import entities.Paddle;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import systems.AudioSystem;
import ui.theme.Colors;
import ui.theme.Fonts;
import ui.widgets.ButtonUI;

import java.util.Objects;

/**
 * Settings – Màn hình cài đặt, cho phép người chơi điều chỉnh âm thanh, hình ảnh.
 */
public class Settings {
    public interface Listener { void onClose(); }

    private final AudioSystem audio;
    private final Config config;
    private final Listener listener;

    public Settings(AudioSystem audio, Config config, Listener listener) {
        this.audio = audio;
        this.config = config;
        this.listener = listener;
    }

    public Scene create(Stage stage) {
        Image bg = new Image(
                Objects.requireNonNull(
                        getClass().getResource("/images/SETTING.png")
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

        Label title = new Label("Settings");
        title.setFont(Fonts.main(50));
        title.setTextFill(Colors.TEXT);

        CheckBox soundToggle = new CheckBox("TURN MUSIC OR NOT");
        soundToggle.setSelected(audio.isEnabled());
        soundToggle.setTextFill(Colors.TEXT);
        soundToggle.setFont(Fonts.main(20));
        soundToggle.selectedProperty().addListener((obs, oldV, enabled) -> {
            audio.setEnabled(enabled);
            if (!enabled) {
                audio.stopMusic();
            } else {
                // Phát lại bài đã chọn (nếu có)
                String sel = audio.getSelectedMusic();
                if (sel != null && !sel.isEmpty()) {
                    audio.playIfChanged(sel);
                }
            }
        });

        Label musicLabel = new Label("SELECT MUSIC");
        musicLabel.setTextFill(Colors.TEXT);
        musicLabel.setFont(Fonts.main(20));
        ComboBox<String> musicBox = new ComboBox<>(FXCollections.observableArrayList(
                "TRUYỆN AUDIO.mp3",
                "REVIEW PHIM.mp3",
                "NHẠC TẾT.mp3",
                "CHRISTMAS SONGS.mp3",
                "NHẠC JACK.mp3",
                "NHẠC MTP.mp3",
                "Bóng lá rơi.mp3"
        ));
        musicBox.setPrefWidth(260);
        String currentSel = audio.getSelectedMusic();
        if (currentSel != null && !currentSel.isEmpty() && musicBox.getItems().contains(currentSel)) {
            musicBox.setValue(currentSel);
        } else {
            musicBox.getSelectionModel().selectFirst();
        }

        ButtonUI applyMusicBtn = new ButtonUI("ADD SOUND");
        applyMusicBtn.setOnAction(e -> {
            AudioSystem.getInstance().playSound("select.mp3");
            String selected = musicBox.getValue();
            audio.setSelectedMusic(selected);
            audio.playIfChanged(selected);
        });

        HBox musicRow = new HBox(12, musicBox, applyMusicBtn);
        musicRow.setAlignment(Pos.CENTER);

        Label paddleLabel = new Label("COLORS OF PADDLE");
        paddleLabel.setTextFill(Colors.TEXT);
        paddleLabel.setFont(Fonts.main(20));

        RadioButton r1 = new RadioButton("Hồng");
        RadioButton r2 = new RadioButton("Xanh ngọc");
        RadioButton r3 = new RadioButton("Cam hồng");

        r1.setTextFill(Colors.TEXT);
        r2.setTextFill(Colors.TEXT);
        r3.setTextFill(Colors.TEXT);

        r1.setUserData(Colors.PRIMARY);
        r2.setUserData(Colors.BUTTON);
        r3.setUserData(Colors.SECONDARY);

        ToggleGroup colorGroup = new ToggleGroup();
        r1.setToggleGroup(colorGroup);
        r2.setToggleGroup(colorGroup);
        r3.setToggleGroup(colorGroup);

        // Preselect theo màu hiện tại
        Color curColor = Paddle.getDefaultColor();
        if (sameColor(curColor, Colors.PRIMARY)) colorGroup.selectToggle(r1);
        else if (sameColor(curColor, Colors.BUTTON)) colorGroup.selectToggle(r2);
        else colorGroup.selectToggle(r3);

        HBox colorRow = new HBox(16, r1, r2, r3);
        colorRow.setAlignment(Pos.CENTER);

        ButtonUI backBtn = new ButtonUI("SAVE AND BACK");
        backBtn.setOnAction(e -> {
            AudioSystem.getInstance().playSound("select.mp3");
            Object selected = colorGroup.getSelectedToggle() != null
                    ? colorGroup.getSelectedToggle().getUserData()
                    : Colors.SECONDARY;
            if (selected instanceof Color) {
                Paddle.setDefaultColor((Color) selected);
            }
            listener.onClose();
        });

        VBox root = new VBox(22,
                title,
                soundToggle,
                musicLabel, musicRow,
                paddleLabel, colorRow,
                backBtn
        );
        root.setBackground(new Background(bgi));
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        Scene scene =  new Scene(root, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);

        return scene;
    }

    private static boolean sameColor(Color a, Color b) {
        if (a == null || b == null) return false;
        return Math.abs(a.getRed()-b.getRed())   < 1e-6
                && Math.abs(a.getGreen()-b.getGreen()) < 1e-6
                && Math.abs(a.getBlue()-b.getBlue())   < 1e-6
                && Math.abs(a.getOpacity()-b.getOpacity()) < 1e-6;
    }
}
