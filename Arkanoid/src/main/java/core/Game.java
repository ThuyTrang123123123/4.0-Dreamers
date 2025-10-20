
import javafx.stage.Stage;
import ui.screen.MainMenu;

public class Game extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Arkanoid");
        stage.setScene(new MainMenu().create(stage));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
