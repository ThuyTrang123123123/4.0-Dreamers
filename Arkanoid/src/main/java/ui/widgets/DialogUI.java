package ui.widgets;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DialogUI {
    public static void showInfo(String title, String msg) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
