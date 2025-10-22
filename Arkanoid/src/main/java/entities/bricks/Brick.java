package entities.bricks;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import ui.theme.Colors;

public class Brick {
    private double x, y;         // Tọa độ tâm viên gạch
    private double width, height;
    private boolean destroyed = false; // Trạng thái gạch (đã bị vỡ hay chưa)

    // ===== Constructor =====
    public Brick(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // ===== Getter / Setter =====
    public double getX() { return x; }
    public void setX(double x) { this.x = x; }

    public double getY() { return y; }
    public void setY(double y) { this.y = y; }

    public double getWidth() { return width; }
    public void setWidth(double width) { this.width = width; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public boolean isDestroyed() { return destroyed; }
    public void setDestroyed(boolean destroyed) { this.destroyed = destroyed; }

    // ===== Logic =====

    // Khi trúng
    public void hit() {
        destroyed = true;
    }

    // Vùng bao quanh gạch (dùng cho va chạm)
    public Rectangle2D getBounds() {
        return new Rectangle2D(x - width / 2, y - height / 2, width, height);
    }

    // Vẽ gạch từ tâm
    public void render(GraphicsContext gc) {
        if (!destroyed) {
            double drawX = x - width / 2;
            double drawY = y - height / 2;

            gc.setFill(Colors.BRICKS);
            gc.fillRect(drawX, drawY, width, height);
            gc.setStroke(Color.DARKRED);
            gc.strokeRect(drawX, drawY, width, height);
        }
    }
}
