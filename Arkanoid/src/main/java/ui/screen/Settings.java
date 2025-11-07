package ui.screen;

import core.Config;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
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
        Label title = new Label("🎵 Settings – Select music you want");
        title.setFont(Fonts.main(18));
        title.setTextFill(Colors.PRIMARY);

        // === Danh sách nhạc có trong /sounds/ ===
        ComboBox<String> musicBox = new ComboBox<>(FXCollections.observableArrayList(
                "nhac1.wav",
                "nhac2.wav",
                "nhac3.wav"
        ));
        musicBox.setValue(audio.getSelectedMusicOrDefault(Config.DEFAULT_MUSIC));

        // Khi người chơi chọn nhạc
        musicBox.setOnAction(e -> {
            String selected = musicBox.getValue();
            audio.setSelectedMusic(selected);
            audio.playIfChanged(selected);
            listener.onClose();
        });

        VBox root = new VBox(30, title, musicBox);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        Scene scene =  new Scene(root, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);

        return scene;
    }
}
