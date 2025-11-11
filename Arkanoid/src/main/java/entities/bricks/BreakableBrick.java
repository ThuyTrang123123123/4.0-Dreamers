package entities.bricks;

import javafx.scene.canvas.GraphicsContext;

public class BreakableBrick extends Brick{
    public BreakableBrick(double x, double y, double width, double height,int hitPoints) {
        super(x, y, width, height,hitPoints);
    }
    @Override
    public void render(GraphicsContext gc){};
}
