package ui.theme;

import javafx.scene.text.Font;

public class Fonts {
    // các font nên được đặt trong src/main/resources/themes/fonts/
    public static final Font TITLE = Font.font("System", 28); // tạm; thay bằng loadFont nếu có ttf
    public static final Font BODY = Font.font("System", 16);
    public static final Font HUD = Font.font("System", 14);

    public static Font main(double size) {
        return Font.font("Comic Sans MS", size); // hoặc chọn font vui tươi như Angela
    }
}
