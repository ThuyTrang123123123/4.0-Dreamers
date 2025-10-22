package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.geometry.Rectangle2D;

public class Paddle {
    private double x, y;           // Tọa độ tâm
    private double width, height;
    private double speed = 400;
    private boolean moveLeft, moveRight;

    // ===== Constructor =====
    public Paddle(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // ===== Getters & Setters =====
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public double getSpeed() { return speed; }
    public boolean isMoveLeft() { return moveLeft; }
    public boolean isMoveRight() { return moveRight; }

    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setWidth(double width) { this.width = width; }
    public void setHeight(double height) { this.height = height; }
    public void setSpeed(double speed) { this.speed = speed; }
    public void setMoveLeft(boolean moveLeft) { this.moveLeft = moveLeft; }
    public void setMoveRight(boolean moveRight) { this.moveRight = moveRight; }

    // ===== Update & Render =====
    public void update(double deltaTime) {
        if (moveLeft) x -= speed * deltaTime;
        if (moveRight) x += speed * deltaTime;

        // Giới hạn biên trái/phải theo tâm
        double halfWidth = width / 2;
        if (x - halfWidth < 0) x = halfWidth;
        if (x + halfWidth > 800) x = 800 - halfWidth;
    }

    public void render(GraphicsContext gc) {
        gc.setFill(Color.CYAN);
        gc.fillRect(x - width / 2, y - height / 2, width, height);
    }

    // ===== Input Handling =====
    public void onKeyPressed(KeyCode code) {
        if (code == KeyCode.LEFT) moveLeft = true;
        if (code == KeyCode.RIGHT) moveRight = true;
    }

    public void onKeyReleased(KeyCode code) {
        if (code == KeyCode.LEFT) moveLeft = false;
        if (code == KeyCode.RIGHT) moveRight = false;
    }

    // ===== Collision =====
    public Rectangle2D getBounds() {
        return new Rectangle2D(x - width / 2, y - height / 2, width, height);
    }
}
