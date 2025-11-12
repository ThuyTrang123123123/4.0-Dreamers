package ui.screen;

import core.Config;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import systems.AudioSystem;
import ui.widgets.ButtonUI;

import java.util.Objects;

public class Introduce {
    public Scene create(Stage stage) {
        Image bg = new Image(
                Objects.requireNonNull(
                        getClass().getResource("/images/introduce.png")
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

        BorderPane root = new BorderPane();
        root.setBackground(new Background(bgi));

        VBox footer = new VBox(12);
        footer.setAlignment(Pos.CENTER);

        VBox mider = new VBox(12);
        mider.setAlignment(Pos.CENTER);

        ButtonUI backBtn = new ButtonUI("Menu");
        backBtn.setOnAction( e -> {
            AudioSystem.getInstance().playSound("select.mp3");
            stage.setScene(MainMenu.cachedScene);
        });

        ButtonUI nextBtn = new ButtonUI("Guide");
        nextBtn.setOnAction(e -> {
            AudioSystem.getInstance().playSound("select.mp3");
            stage.setScene(Guide.create(stage));
        });

        footer.getChildren().add(backBtn);
        mider.getChildren().add(nextBtn);

        root.setLeft(footer);
        root.setRight(mider);

        return new Scene(root, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
    }
}
