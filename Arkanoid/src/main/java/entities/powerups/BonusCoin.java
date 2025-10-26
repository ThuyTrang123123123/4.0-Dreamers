package entities.powerups;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import core.World;
import javafx.scene.image.Image;


public class BonusCoin {
    private double x, y;
    private double radius;
    private double speed;
    private boolean collected = false;

    private double zigzagTimer = 0;
    private double zigzagInterval = 1; // đổi hướng mỗi ziczagInterval giây
    private int directionX = 1; // 1: phải, -1: trái

    private int value = 10; // điểm thưởng khi ăn coin

    // ===== Constructor =====
    public BonusCoin(double x, double y, double radius, double speed) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.speed = speed;
    }

    public double getX() {return x;}
    public void setX(double x) {this.x = x;}
    public double getY() {return y;}
    public void setY(double y) {this.y = y;}
    public void setCollected(boolean collected) {
        this.collected = collected;
    }
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    // ===== Update =====
    public void update(double deltaTime) {
        x += directionX * 2* speed * deltaTime;
        y += speed * deltaTime;

        zigzagTimer += deltaTime;
        if (zigzagTimer >= zigzagInterval) {
            directionX *= -1;
            zigzagTimer = 0;
        }
    }

    // ===== Render =====
    public void render(GraphicsContext gc) {
        if (!collected) {
            Image coinImage = new Image("file:/D:/IntelliJ/4.0-Dreamers/Arkanoid/src/main/resources/images/bonusCoin.png");
            gc.drawImage(coinImage, x - radius, y - radius, radius * 2, radius * 2);
        }
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x - radius, y - radius, radius * 2, radius * 2);
    }

    public boolean isCollected() {
        return collected;
    }
}