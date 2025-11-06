package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.geometry.Rectangle2D;

public class Bullet {
    private double x, y;
    private final double speed = 500;
    private final double width = 12, height = 36;
    private boolean active = true;

    private static Image bulletImage = new Image(
            Bullet.class.getResource("/images/Bullet.png").toExternalForm()
    );

    public Bullet(double x, double y) { this.x = x; this.y = y; }

    public boolean isActive() { return active; }
    public void deactivate() { active = false; }
    public Rectangle2D getBounds() { return new Rectangle2D(x, y, width, height); }

    public void update(double dt) {
        y -= speed * dt;
        if (y < 0) active = false;
    }

    public void render(GraphicsContext gc) {
        if (!active) return;

        // Vẽ ảnh viên đạn
        gc.drawImage(bulletImage, x, y, width, height);
    }

}