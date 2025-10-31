package entities;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball {
    private double x, y;
    private double radius;
    private double velocityX;     // vận tốc theo trục X
    private double velocityY;     // vận tốc theo trục Y
    private boolean lost = false; // đánh dấu bóng rơi ra khỏi màn chơi

    // ===== Constructor =====
    public Ball(double x, double y, double radius, double speed) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.velocityX = speed;
        this.velocityY = -speed;
    }

    // ===== Getters & Setters =====
    public double getX() { return x; }
    public double getY() { return y; }
    public double getRadius() { return radius; }
    public double getVelocityX() { return velocityX; }
    public double getVelocityY() { return velocityY; }
    public boolean isLost() { return lost; }

    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setRadius(double radius) { this.radius = radius; }
    public void setVelocityX(double velocityX) { this.velocityX = velocityX; }
    public void setVelocityY(double velocityY) { this.velocityY = velocityY; }
    public void setLost(boolean lost) { this.lost = lost; }

    // ===== Update & Render =====
    public void update(double deltaTime) {
        x += velocityX * deltaTime;
        y += velocityY * deltaTime;
    }

    public void render(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);
    }

    // ===== Collision & Bounds =====
    public Rectangle2D getBounds() {
        return new Rectangle2D(x - radius, y - radius, radius * 2, radius * 2);
    }

    public double getLeft()   { return x - radius; }
    public double getRight()  { return x + radius; }
    public double getTop()    { return y - radius; }
    public double getBottom() { return y + radius; }

    // ===== Utility =====
    //Diameter : duong kink
    public double getDiameter() { return radius * 2; }

    public void reverseX() { velocityX = -velocityX; }
    public void reverseY() { velocityY = -velocityY; }
}