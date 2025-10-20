package ui.widgets;

import javafx.scene.control.Slider;
import ui.theme.Colors;

public class SliderUI extends Slider {
    public SliderUI(double min, double max, double value) {
        super(min, max, value);
        setStyle(String.format("-fx-control-inner-background: #%s;", colorToHex(Colors.BUTTON)));
    }

    private String colorToHex(javafx.scene.paint.Color color) {
        return color.toString().substring(2, 8);
    }
}
