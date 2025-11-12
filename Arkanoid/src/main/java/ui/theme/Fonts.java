package ui.theme;

import javafx.scene.text.Font;

/**
 * Fonts – Quản lý kiểu chữ và kích thước chữ trong giao diện.
 */
public class Fonts {

    public static final Font TITLE = Font.font("System", 28);
    public static final Font BODY = Font.font("System", 16);
    public static final Font HUD = Font.font("System", 14);

    public static Font main(double size) {
        return Font.font("Comic Sans MS", size);
    }

    public static Font title() {
        return TITLE;
    }

}