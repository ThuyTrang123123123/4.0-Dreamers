package ui.widgets;

import javafx.scene.control.Button;
import ui.theme.Colors;
import ui.theme.Fonts;

public class ButtonUI extends Button {
    public ButtonUI(String text) {
        super(text);
        setFont(Fonts.main(18));
        setStyle(defaultStyle());

        setOnMouseEntered(e -> setStyle(hoverStyle()));
        setOnMouseExited(e -> setStyle(defaultStyle()));
    }

    private String defaultStyle() {
        return String.format("-fx-background-color: #%s; -fx-text-fill: #%s; -fx-background-radius: 10;",
                colorToHex(Colors.BUTTON), colorToHex(Colors.TEXT));
    }

    private String hoverStyle() {
        return String.format("-fx-background-color: #%s; -fx-text-fill: #%s;",
                colorToHex(Colors.BUTTON), colorToHex(Colors.TEXT));
    }

    private String colorToHex(javafx.scene.paint.Color color) {
        return color.toString().substring(2, 8);
    }
}
