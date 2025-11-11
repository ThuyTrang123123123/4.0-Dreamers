package ui.screen;

import core.Config;
import entities.Paddle;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import systems.AudioSystem;
import ui.theme.Colors;
import ui.theme.Fonts;

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
        Label title = new Label("Settings");
        title.setFont(Fonts.main(50));
        title.setTextFill(Colors.PRIMARY);

        CheckBox soundToggle = new CheckBox("TURN MUSIC OR NOT");
        soundToggle.setSelected(audio.isEnabled());
        soundToggle.setTextFill(Colors.TEXTBT);
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
        musicLabel.setTextFill(Colors.TEXTBT);
        musicLabel.setFont(Fonts.main(20));
        ComboBox<String> musicBox = new ComboBox<>(FXCollections.observableArrayList(
                "nhac1.wav",
                "nhac2.wav",
                "nhac3.wav"
        ));
        musicBox.setPrefWidth(260);
        String currentSel = audio.getSelectedMusic();
        if (currentSel != null && !currentSel.isEmpty() && musicBox.getItems().contains(currentSel)) {
            musicBox.setValue(currentSel);
        } else {
            musicBox.getSelectionModel().selectFirst();
        }

        Button applyMusicBtn = new Button("ADD MUSIC");
        applyMusicBtn.setOnAction(e -> {
            String selected = musicBox.getValue();
            audio.setSelectedMusic(selected);
            audio.playIfChanged(selected);
        });

        HBox musicRow = new HBox(12, musicBox, applyMusicBtn);
        musicRow.setAlignment(Pos.CENTER);

        Label paddleLabel = new Label("COLORS OF PADDLE");
        paddleLabel.setTextFill(Colors.TEXTBT);
        paddleLabel.setFont(Fonts.main(20));

        RadioButton r1 = new RadioButton("Hồng");
        RadioButton r2 = new RadioButton("Xanh ngọc");
        RadioButton r3 = new RadioButton("Cam hồng");

        r1.setTextFill(Colors.TEXTBT);
        r2.setTextFill(Colors.TEXTBT);
        r3.setTextFill(Colors.TEXTBT);

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

        Button backBtn = new Button("SAVE AND BACK");
        backBtn.setOnAction(e -> {
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
