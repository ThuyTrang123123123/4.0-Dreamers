package entities.bricks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;

/**
 * Lớp NormalBrick.
 * Đây là loại gạch cơ bản nhất, chỉ cần 1 hitPoint để phá hủy.
 */

public class NormalBrick extends Brick {
    // Màu vàng cố định cho tất cả gạch
    private final Color brickColor = Color.web("#FFD700"); // Vàng trong trẻo

    public NormalBrick(double x, double y, double width, double height) {
        super(x, y, width, height, 1);
    }

    @Override
    public void render(GraphicsContext gc) {
        if (destroyed) return;

        double drawX = x - width / 2;
        double drawY = y - height / 2;

        // Tính toán các màu
        Color darkColor = brickColor.darker();
        Color lightColor = brickColor.brighter();
        // Lớp đậm
        gc.setFill(darkColor.darker());
        gc.fillRect(drawX + 1, drawY + 1, width - 2, height - 2);
        // Vẽ gradient
        LinearGradient verticalGradient = new LinearGradient(
                0, drawY + 3, 0, drawY + height - 3,
                false, CycleMethod.NO_CYCLE,
                new Stop(0, lightColor),
                new Stop(0.45, brickColor),
                new Stop(0.55, brickColor),
                new Stop(1, darkColor)
        );
        gc.setFill(verticalGradient);
        gc.fillRect(drawX + 3, drawY + 3, width - 6, height - 6);

        // Phần sáng trên
        LinearGradient topHighlight = new LinearGradient(
                0, drawY + 3, 0, drawY + height * 0.4,
                false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(255, 255, 255, 0.6)),
                new Stop(0.5, Color.rgb(255, 255, 255, 0.3)),
                new Stop(1, Color.TRANSPARENT)
        );
        gc.setFill(topHighlight);
        gc.fillRect(drawX + 4, drawY + 4, width - 8, height * 0.4);
        // Phần tối dưới
        LinearGradient bottomShadow = new LinearGradient(
                0, drawY + height * 0.6, 0, drawY + height - 3,
                false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.TRANSPARENT),
                new Stop(0.5, Color.rgb(0, 0, 0, 0.2)),
                new Stop(1, Color.rgb(0, 0, 0, 0.4))
        );
        gc.setFill(bottomShadow);
        gc.fillRect(drawX + 4, drawY + height * 0.6, width - 8, height * 0.37);
        // Viền sáng trong
        gc.setStroke(lightColor.brighter());
        gc.setLineWidth(1);
        gc.strokeRect(drawX + 2.5, drawY + 2.5, width - 5, height - 5);
        // Viền đậm trong
        gc.setStroke(darkColor);
        gc.setLineWidth(1);
        gc.strokeRect(drawX + 3.5, drawY + 3.5, width - 7, height - 7);
    }
}