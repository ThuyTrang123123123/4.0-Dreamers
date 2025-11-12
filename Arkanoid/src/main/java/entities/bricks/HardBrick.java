package entities.bricks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class HardBrick extends Brick {

    public HardBrick(double x, double y, double width, double height) {
        super(x, y, width, height, 3);
    }

    @Override
    public void render(GraphicsContext gc) {
        if (destroyed) return;

        double drawX = x - width / 2;
        double drawY = y - height / 2;

        //thay đổi độ sáng theo HP
        Color brickColor;
        if (hitPoints == 3) {
            brickColor = Color.web("#2C3E50"); // Xám đậm nhất
        } else if (hitPoints == 2) {
            brickColor = Color.web("#34495E"); // Xám vừa
        } else {
            brickColor = Color.web("#5D6D7E"); // Xám nhạt
        }

        Color darkColor = brickColor.darker();
        Color lightColor = brickColor.brighter();
        gc.setFill(darkColor.darker());
        gc.fillRect(drawX + 1, drawY + 1, width - 2, height - 2);

        //VẼ GRADIENT CHÍNH
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

        //VẼ PHẦN SÁNG PHÍA TRÊN
        LinearGradient topHighlight = new LinearGradient(
                0, drawY + 3, 0, drawY + height * 0.4,
                false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(255, 255, 255, 0.5)),
                new Stop(0.5, Color.rgb(255, 255, 255, 0.25)),
                new Stop(1, Color.TRANSPARENT)
        );
        gc.setFill(topHighlight);
        gc.fillRect(drawX + 4, drawY + 4, width - 8, height * 0.4);

        //VẼ PHẦN TỐI PHÍA DƯỚI
        LinearGradient bottomShadow = new LinearGradient(
                0, drawY + height * 0.6, 0, drawY + height - 3,
                false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.TRANSPARENT),
                new Stop(0.5, Color.rgb(0, 0, 0, 0.3)),
                new Stop(1, Color.rgb(0, 0, 0, 0.5))
        );
        gc.setFill(bottomShadow);
        gc.fillRect(drawX + 4, drawY + height * 0.6, width - 8, height * 0.37);

        //VẼ VIỀN SÁNG BÊN TRONG
        gc.setStroke(lightColor.brighter());
        gc.setLineWidth(1);
        gc.strokeRect(drawX + 2.5, drawY + 2.5, width - 5, height - 5);

        //VẼ VIỀN ĐẬM BÊN TRONG
        gc.setStroke(darkColor);
        gc.setLineWidth(1);
        gc.strokeRect(drawX + 3.5, drawY + 3.5, width - 7, height - 7);

        //HIỆU ỨNG NỨT GẠCH
        gc.setStroke(Color.web("#1C2833"));
        gc.setLineWidth(2.0);
        gc.setLineCap(javafx.scene.shape.StrokeLineCap.ROUND);
        gc.setLineJoin(javafx.scene.shape.StrokeLineJoin.ROUND);

        if (hitPoints <= 2) {
            // Nứt cơ bản cho HP=2 và HP=1
            gc.strokeLine(drawX + width * 0.3, drawY + height * 0.2, drawX + width * 0.7, drawY + height * 0.8);
            gc.strokeLine(drawX + width * 0.4, drawY + height * 0.1, drawX + width * 0.2, drawY + height * 0.6);
            gc.strokeLine(drawX + width * 0.6, drawY + height * 0.9, drawX + width * 0.8, drawY + height * 0.4);
        }

        if (hitPoints == 1) {
            // Thêm nứt nhiều hơn cho HP=1
            gc.strokeLine(drawX + width * 0.1, drawY + height * 0.5, drawX + width * 0.9, drawY + height * 0.5);
            gc.strokeLine(drawX + width * 0.2, drawY + height * 0.8, drawX + width * 0.8, drawY + height * 0.2);
            gc.strokeLine(drawX + width * 0.05, drawY + height * 0.3, drawX + width * 0.25, drawY + height * 0.4);
            gc.strokeLine(drawX + width * 0.75, drawY + height * 0.6, drawX + width * 0.95, drawY + height * 0.7);
        }

        // Vẽ highlight sáng cho nứt để tạo chiều sâu
        gc.setStroke(Color.web("#BDC3C7").deriveColor(0, 1, 1, 0.3)); // Màu sáng mờ
        gc.setLineWidth(1.0);

        if (hitPoints <= 2) {
            gc.strokeLine(drawX + width * 0.3 + 1, drawY + height * 0.2 + 1, drawX + width * 0.7 + 1, drawY + height * 0.8 + 1);
            gc.strokeLine(drawX + width * 0.4 + 1, drawY + height * 0.1 + 1, drawX + width * 0.2 + 1, drawY + height * 0.6 + 1);
            gc.strokeLine(drawX + width * 0.6 + 1, drawY + height * 0.9 + 1, drawX + width * 0.8 + 1, drawY + height * 0.4 + 1);
        }

        if (hitPoints == 1) {
            gc.strokeLine(drawX + width * 0.1 + 1, drawY + height * 0.5 + 1, drawX + width * 0.9 + 1, drawY + height * 0.5 + 1);
            gc.strokeLine(drawX + width * 0.2 + 1, drawY + height * 0.8 + 1, drawX + width * 0.8 + 1, drawY + height * 0.2 + 1);
            gc.strokeLine(drawX + width * 0.05 + 1, drawY + height * 0.3 + 1, drawX + width * 0.25 + 1, drawY + height * 0.4 + 1);
            gc.strokeLine(drawX + width * 0.75 + 1, drawY + height * 0.6 + 1, drawX + width * 0.95 + 1, drawY + height * 0.7 + 1);
        }
    }
}