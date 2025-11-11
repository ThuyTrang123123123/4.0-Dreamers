package entities.bricks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class NormalBrick extends BreakableBrick {

    public NormalBrick(double x, double y, double width, double height) {
        super(x, y, width, height,1);
    }

    @Override
    public void render(GraphicsContext gc) {
        if (destroyed) return;
        gc.setFill(Color.ORANGE);
        gc.fillRect(x - width / 2, y - height / 2, width, height);
        gc.setStroke(Color.LIGHTYELLOW);
        gc.strokeRect(x - width / 2, y - height / 2, width, height);
    }
}