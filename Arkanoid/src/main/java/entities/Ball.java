package entities;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
/**
 * Ball – Đối tượng chính trong trò chơi, di chuyển và phá vỡ các mục tiêu.
 * Có thể thay đổi tốc độ, kích thước và hướng tùy theo trạng thái game.
 */
public class Ball {
    private double x, y;
    private double radius;
    private double velocityX;
    private double velocityY;
    private boolean lost = false;
    private boolean stickToPaddle = true;
    private double stickOffsetX = 0;
    public Ball(double x, double y, double radius, double speed) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.velocityX = speed;
        this.velocityY = -speed;
    }
    public double getSpeedMultiplier() {return speedMultiplier;}
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

    public void update(double deltaTime) {
        if (stickToPaddle) {
            return;
        }

        x += velocityX * deltaTime;
        y += velocityY * deltaTime;
        normalizeSpeed();
    }

    private void normalizeSpeed() {
        double currentSpeed = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
        double targetSpeed = core.Config.BALL_SPEED* speedMultiplier;

        if (Math.abs(currentSpeed - targetSpeed) > targetSpeed * 0.1) {
            double ratio = targetSpeed / currentSpeed;
            velocityX *= ratio;
            velocityY *= ratio;
        }
    }

    public void updateStickPosition(double paddleX, double paddleY) {
        if (stickToPaddle) {
            this.x = paddleX + stickOffsetX;
            this.y = paddleY - radius - 5;  // Đặt bóng trên paddle (cách 5px)
        }
    }

    public void launch() {
        if (stickToPaddle) {
            stickToPaddle = false;
            double speed = core.Config.BALL_SPEED;
            double angle=-180;
            velocityX = speed * Math.sin(Math.toRadians(angle));
            velocityY = speed * Math.cos(Math.toRadians(angle));

            System.out.println("Bóng đã được bắn! Tốc độ: " + speed);
        }
    }
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

    public Rectangle2D getBounds() {
        return new Rectangle2D(x - radius, y - radius, radius * 2, radius * 2);
    }

    public double getLeft()   { return x - radius; }
    public double getRight()  { return x + radius; }
    public double getTop()    { return y - radius; }
    public double getBottom() { return y + radius; }

    public double getDiameter() { return radius * 2; }

    public void reverseX() { velocityX = -velocityX; }
    public void reverseY() { velocityY = -velocityY; }
}