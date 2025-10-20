package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.geometry.Rectangle2D;

public class Paddle {
    private double x, y, width, height, speed = 400;
    private boolean moveLeft, moveRight;

    public Paddle(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void update(double deltaTime) {
        if (moveLeft) x -= speed * deltaTime;
        if (moveRight) x += speed * deltaTime;

        if (x < 0) x = 0;
        if (x + width > 800) x = 800 - width;
    }

    public void render(GraphicsContext gc) {
        gc.setFill(Color.CYAN);
        gc.fillRect(x, y, width, height);
    }

    public void onKeyPressed(KeyCode code) {
        if (code == KeyCode.LEFT) moveLeft = true;
        if (code == KeyCode.RIGHT) moveRight = true;
    }

    public void onKeyReleased(KeyCode code) {
        if (code == KeyCode.LEFT) moveLeft = false;
        if (code == KeyCode.RIGHT) moveRight = false;
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
}