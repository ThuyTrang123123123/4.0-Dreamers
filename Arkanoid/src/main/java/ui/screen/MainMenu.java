package ui.screen;

import core.Game;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ui.theme.Colors;
import ui.widgets.ButtonUI;

public class MainMenu {
    public Scene create(Stage stage) {
        VBox root = new VBox(20);
        root.setStyle("-fx-background-color: #" + colorToHex(Colors.BACKGROUND) + "; -fx-alignment: center;");

        ButtonUI playBtn = new ButtonUI("Play");
        playBtn.setOnAction(e -> {
            Game game = new Game();
            stage.setScene(game.createGamescene(stage));
            stage.show();
        });

        ButtonUI exitBtn = new ButtonUI("Exit");
        exitBtn.setOnAction(e -> stage.close());

        root.getChildren().addAll(playBtn, exitBtn);
        return new Scene(root, 800, 600);
    }

    private String colorToHex(javafx.scene.paint.Color color) {
        return color.toString().substring(2, 8);
    }
}