package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.geometry.Rectangle2D;

public class Paddle {

    private double x, y, width, height;
    private double speed = 500;
    private boolean moveLeft, moveRight;

    public Paddle(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // Cập nhật vị trí
    public void update(double deltaTime, double screenWidth) {
        if (moveLeft) x -= speed * deltaTime;
        if (moveRight) x += speed * deltaTime;

        // Không cho paddle ra ngoài màn hình
        if (x < 0) x = 0;
        if (x + width > screenWidth) x = screenWidth - width;
    }

    // Vẽ paddle
    public void render(GraphicsContext gc) {
        gc.setFill(Color.GREEN);
        gc.fillRect(x, y, width, height);
    }

    // Xử lý phím
    public void onKeyPressed(KeyCode code) {
        if (code == KeyCode.LEFT) moveLeft = true;
        if (code == KeyCode.RIGHT) moveRight = true;
    }

    public void onKeyReleased(KeyCode code) {
        if (code == KeyCode.LEFT) moveLeft = false;
        if (code == KeyCode.RIGHT) moveRight = false;
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(x, y, width, height);
    }
}


