package entities.bricks;

import javafx.scene.canvas.GraphicsContext;
/**
 * Lớp BreakableBrick
 * Đại diện cho một loại gạch có thể bị phá hủy trong trò chơi (ví dụ: gạch Normal, gạch Hard).
 * Đây là một lớp trừu tượng (hoặc lớp cơ sở).
 * Lớp này kế thừa (extends) từ lớp Brick.
 */

public class BreakableBrick extends Brick{
    public BreakableBrick(double x, double y, double width, double height,int hitPoints) {
        super(x, y, width, height,hitPoints);
    }
    @Override
    public void render(GraphicsContext gc){};
}
