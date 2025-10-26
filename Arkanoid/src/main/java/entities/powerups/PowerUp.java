package entities.powerups;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Lớp cơ sở cho tất cả Power-Up
 */
public abstract class PowerUp {
    protected double x, y;
    protected double width = 20;
    protected double height = 20;
    protected double speed = 100;   // pixel / giây
    protected boolean active = false; // đang rơi hay đã bị ăn

    public PowerUp(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Logic cơ bản
    public void update(double deltaTime) {
        if (!active) return;
        y += speed * deltaTime; // Rơi xuống
    }

    public void render(GraphicsContext gc) {
        if (!active) return;
        gc.setFill(getColor());
        gc.fillOval(x - width / 2, y - height / 2, width, height);
        gc.setStroke(Color.BLACK);
        gc.strokeOval(x - width / 2, y - height / 2, width, height);
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x - width / 2, y - height / 2, width, height);
    }

    // Trạng thái
    public void activate() { active = true; }
    public void deactivate() { active = false; }
    public boolean isActive() { return active; }

    public abstract void applyEffect(Object target); // target có thể là paddle, ball, v.v.
    protected abstract Color getColor();             // mỗi loại power-up 1 màu riêng
}
