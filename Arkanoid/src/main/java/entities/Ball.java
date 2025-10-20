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

    public Ball(double x, double y, double radius, double speed) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.velocityX = speed;
        this.velocityY = -speed;
    }

    // Cập nhật vị trí
    public void update(double deltaTime) {
        x += velocityX * deltaTime;
        y += velocityY * deltaTime;
    }

    // Vẽ bóng
    public void render(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.fillOval(x, y, radius * 2, radius * 2);
    }

    public double getX() { return x; }
    public double getY() { return y; }

    // Trả về đường kính
    public double getDiameter() { return radius * 2; }

    // Tọa độ các cạnh của quả bóng
    public double getLeft() { return x; }
    public double getRight() { return x + radius * 2; }
    public double getTop() { return y; }
    public double getBottom() { return y + radius * 2; }

    // Đảo hướng di chuyển
    public void reverseX() { velocityX = -velocityX; }
    public void reverseY() { velocityY = -velocityY; }

    // Đặt vận tốc theo trục X (chỉ hình thức)
    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    // Đánh dấu bóng đã rơi
    public void setLost(boolean lost) {
        this.lost = lost;
    }

    public boolean isLost() {
        return lost;
    }

    // Kiểm tra va chạm
    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, radius * 2, radius * 2);
    }
}
