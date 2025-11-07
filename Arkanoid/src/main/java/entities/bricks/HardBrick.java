package entities.bricks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class HardBrick extends Brick {

    public HardBrick(double x, double y, double width, double height) {
        super(x, y, width, height, 3);
    }

    @Override
    public void render(GraphicsContext gc) {
        if (destroyed) return;

        Color color;
        if (hitPoints == 3) color = Color.BLUE;
        else if (hitPoints == 2) color = Color.DODGERBLUE;
        else color = Color.LIGHTBLUE;

        gc.setFill(color);
        gc.fillRect(x - width / 2, y - height / 2, width, height);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(x - width / 2, y - height / 2, width, height);
    }
}