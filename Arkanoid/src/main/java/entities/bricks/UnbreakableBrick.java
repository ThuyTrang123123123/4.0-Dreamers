package entities.bricks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;

public class UnbreakableBrick extends Brick {
    public UnbreakableBrick(double x, double y, double width, double height) {
        super(x, y, width, height, 1);
    }

    @Override
    public void hit() {
        // Không thể phá
        setDestroyed(false);
    }

    @Override
    public void render(GraphicsContext gc) {
        if (isDestroyed()) return;

        double drawX = getX() - getWidth() / 2;
        double drawY = getY() - getHeight() / 2;

        // Màu chủ đạo nâu
        Color brickColor = Color.web("#8B4513"); // nâu đất
        Color darkColor = brickColor.darker();
        Color lightColor = brickColor.brighter();

        gc.setFill(darkColor.darker());
        gc.fillRect(drawX + 1, drawY + 1, getWidth() - 2, getHeight() - 2);

        LinearGradient gradient = new LinearGradient(
                0, drawY + 3, 0, drawY + getHeight() - 3,
                false, CycleMethod.NO_CYCLE,
                new Stop(0, lightColor),
                new Stop(0.5, brickColor),
                new Stop(1, darkColor)
        );
        gc.setFill(gradient);
        gc.fillRect(drawX + 3, drawY + 3, getWidth() - 6, getHeight() - 6);

        // Sáng phía trên
        LinearGradient topHighlight = new LinearGradient(
                0, drawY + 3, 0, drawY + getHeight() * 0.4,
                false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(255, 255, 255, 0.3)),
                new Stop(1, Color.TRANSPARENT)
        );
        gc.setFill(topHighlight);
        gc.fillRect(drawX + 4, drawY + 4, getWidth() - 8, getHeight() * 0.4);

        // Bóng phía dưới
        LinearGradient bottomShadow = new LinearGradient(
                0, drawY + getHeight() * 0.6, 0, drawY + getHeight() - 3,
                false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.TRANSPARENT),
                new Stop(0.5, Color.rgb(0, 0, 0, 0.25)),
                new Stop(1, Color.rgb(0, 0, 0, 0.45))
        );
        gc.setFill(bottomShadow);
        gc.fillRect(drawX + 4, drawY + getHeight() * 0.6, getWidth() - 8, getHeight() * 0.37);

        gc.setStroke(lightColor);
        gc.setLineWidth(1);
        gc.strokeRect(drawX + 2.5, drawY + 2.5, getWidth() - 5, getHeight() - 5);

        gc.setStroke(darkColor);
        gc.strokeRect(drawX + 3.5, drawY + 3.5, getWidth() - 7, getHeight() - 7);
    }
}
