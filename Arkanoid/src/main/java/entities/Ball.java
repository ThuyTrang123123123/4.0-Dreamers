package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.geometry.Rectangle2D;

public class Ball {
    private double x, y, radius, speedX, speedY;

    public Ball(double x, double y, double radius, double speed) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.speedX = speed;
        this.speedY = -speed;
    }

    public void update(double deltaTime) {
        x += speedX * deltaTime;
        y += speedY * deltaTime;
    }

    public void render(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.fillOval(x, y, radius * 2, radius * 2);
    }

    public void reverseX() { speedX = -speedX; }
    public void reverseY() { speedY = -speedY; }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, radius * 2, radius * 2);
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getRadius() { return radius; }
}
