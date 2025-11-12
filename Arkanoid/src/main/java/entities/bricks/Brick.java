package entities.bricks;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import ui.theme.Colors;

/**
 * Lớp trừu tượng (Abstract Class) Brick
 * Đây là lớp cơ sở cho tất cả các loại gạch trong trò chơi.
 * Nó định nghĩa các thuộc tính và hành vi cơ bản mà mọi viên gạch phải có.
 */
public abstract class Brick {
    protected double x, y;         // Tọa độ tâm viên gạch
    protected double width, height;
    protected int hitPoints;
    protected boolean destroyed = false; // Trạng thái gạch (đã bị vỡ hay chưa)

    public Brick(double x, double y, double width, double height, int hitPoints) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hitPoints = hitPoints;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    // Kiểm tra trạng thái gạch
    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    // Khi trúng
    public void hit() {
        if (hitPoints > 0) {
            hitPoints--;
            if (hitPoints == 0) destroyed = true;
        }
    }

    // Vùng bao quanh gạch
    public Rectangle2D getBounds() {
        return new Rectangle2D(x - width / 2, y - height / 2, width, height);
    }

    public abstract void render(GraphicsContext gc);
}