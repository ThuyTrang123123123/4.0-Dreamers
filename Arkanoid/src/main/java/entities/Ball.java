package entities;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Ball {
    private double x, y;
    private double radius;
    private double velocityX;
    private double velocityY;
    private boolean lost = false;
    // Trạng thái dính với paddle
    private boolean stickToPaddle = true;  // Bắt đầu với trạng thái dính
    private double stickOffsetX = 0;        // Khoảng cách từ tâm paddle

    // Constructor
    public Ball(double x, double y, double radius, double speed) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.velocityX = speed;
        this.velocityY = -speed;
    }

    // Getters & Setters
    public double getX() { return x; }
    public double getY() { return y; }
    public double getRadius() { return radius; }
    public double getVelocityX() { return velocityX; }
    public double getVelocityY() { return velocityY; }
    private double speedMultiplier = 1.0;
    public boolean isLost() { return lost; }
    public boolean isStickToPaddle() { return stickToPaddle; }

    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setRadius(double radius) { this.radius = radius; }
    public void setVelocityX(double velocityX) { this.velocityX = velocityX; }
    public void setVelocityY(double velocityY) { this.velocityY = velocityY; }
    public void setSpeedMultiplier(double multiplier) {
        this.speedMultiplier = multiplier;
    }

    public void setLost(boolean lost) { this.lost = lost; }
    public void setStickToPaddle(boolean stick) { this.stickToPaddle = stick; }

    //  Update & Render
    public void update(double deltaTime) {
        // Nếu đang dính với paddle thì KHÔNG di chuyển
        if (stickToPaddle) {
            return;
        }

        // Di chuyển bình thường
        x += velocityX * deltaTime;
        y += velocityY * deltaTime;

        // Giữ tốc độ ổn định
        normalizeSpeed();
    }

    /**
     * Giữ tốc độ bóng ổn định
     * Đảm bảo tốc độ không tăng/giảm sau nhiều va chạm
     */

    private void normalizeSpeed() {
        double currentSpeed = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
        double targetSpeed = core.Config.BALL_SPEED* speedMultiplier;
        ;

        // Nếu tốc độ sai lệch > 10%, điều chỉnh lại
        if (Math.abs(currentSpeed - targetSpeed) > targetSpeed * 0.1) {
            double ratio = targetSpeed / currentSpeed;
            velocityX *= ratio;
            velocityY *= ratio;
        }
    }


    /**
     * Cập nhật vị trí bóng khi dính với paddle
     * Gọi method này từ Game.java khi bóng đang stick
     */
    public void updateStickPosition(double paddleX, double paddleY) {
        if (stickToPaddle) {
            this.x = paddleX + stickOffsetX;
            this.y = paddleY - radius - 5;  // Đặt bóng trên paddle (cách 5px)
        }
    }

    /**
     * Bắn bóng (gọi khi nhấn SPACE)
     */
    public void launch() {
        if (stickToPaddle) {
            stickToPaddle = false;

            // Luôn dùng tốc độ cố định từ Config
            double speed = core.Config.BALL_SPEED;  // Lấy từ Config

            // Bắn thẳng lên
            velocityX = 0;
            velocityY = -speed;

            System.out.println("Bóng đã được bắn! Tốc độ: " + speed);
        }
    }

    /**
     * Reset bóng về trạng thái stick (khi mất mạng hoặc chuyển level)
     * Bóng sẽ DÍNH trên paddle và KHÔNG tự động bay
     */
    public void resetToStick(double paddleX, double paddleY) {
        stickToPaddle = true;      // Bật chế độ dính
        stickOffsetX = 0;           // Dính ở giữa paddle
        this.x = paddleX;
        this.y = paddleY - radius - 5;
        this.lost = false;

        System.out.println("Bóng đã reset về trạng thái STICK - Nhấn SPACE để bắn!");
    }

    public void render(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);
    }


    // Collision & Bounds
    public Rectangle2D getBounds() {
        return new Rectangle2D(x - radius, y - radius, radius * 2, radius * 2);
    }

    public double getLeft()   { return x - radius; }
    public double getRight()  { return x + radius; }
    public double getTop()    { return y - radius; }
    public double getBottom() { return y + radius; }

    // Utility
    public double getDiameter() { return radius * 2; }

    public void reverseX() { velocityX = -velocityX; }
    public void reverseY() { velocityY = -velocityY; }
}