package ui.screen;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ui.widgets.ButtonUI;
import core.Config;

public class Pause {
//
//    public Scene create(Stage stage, Runnable onResume, Runnable onRestart) {
//        ButtonUI resumeBtn  = new ButtonUI("Resume");
//        ButtonUI restartBtn = new ButtonUI("Restart");
//
//
//        resumeBtn.setOnAction(e -> onResume.run());
//        restartBtn.setOnAction(e -> onRestart.run());
//
//        VBox box = new VBox(14, resumeBtn, restartBtn);
//        box.setAlignment(Pos.CENTER);
//
//        StackPane root = new StackPane(box);
//        root.setPrefSize(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
//        root.setStyle("-fx-background-color: rgba(0,0,0,0.45);");
//
//        Scene scene = new Scene(root, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
//
//        scene.setOnKeyPressed(e -> {
//            switch (e.getCode()) {
//                case C -> onResume.run();
//                case R -> onRestart.run();
//                default -> { }
//            }
//        });
//
//        root.setEffect(new BoxBlur(6, 6, 2));
//        return scene;
//    }
}