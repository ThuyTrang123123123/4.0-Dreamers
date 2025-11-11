package ui.screen;

import core.Config;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import ui.widgets.ButtonUI;

import java.util.Objects;

public class Guide {
    public static Scene create(Stage stage) {
        Image bg = new Image(
                Objects.requireNonNull(
                        Guide.class.getResource("/images/guide.png")
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

        VBox footer = new VBox();
        footer.setAlignment(Pos.CENTER_LEFT);

        ButtonUI backBtn = new ButtonUI("Menu");
        backBtn.setOnAction(e -> stage.setScene(MainMenu.cachedScene));

        footer.getChildren().add(backBtn);

        root.setLeft(footer);

        return new Scene(root, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
    }
}
