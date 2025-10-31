package entities.powerups;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import systems.ScoringSystem;

/**
 * PowerUp cơ bản (rơi xuống khi phá gạch, va vào paddle để kích hoạt).
 */
public class PowerUp {
    protected double x, y;
    protected double radius = 10;
    protected double speed = 3;
    protected boolean active = true;

    public PowerUp(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        y += speed;
    }

    public void render(GraphicsContext gc) {
        if (!active) return;
        gc.setFill(Color.GOLD);
        gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        gc.setStroke(Color.ORANGE);
        gc.strokeOval(x - radius, y - radius, radius * 2, radius * 2);
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x - radius, y - radius, radius * 2, radius * 2);
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }

    public double getY() {
        return y;
    }

    /**
     * Hiệu ứng mặc định: tăng mạng
     */
    public void applyEffect(ScoringSystem scoring) {
        scoring.addLife();
        deactivate();
    }
}
