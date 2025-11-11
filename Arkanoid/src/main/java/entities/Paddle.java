package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.geometry.Rectangle2D;
import ui.theme.Colors;

public class Paddle {
    private double x, y;
    private double width, height;
    private double speed = 400;
    private boolean moveLeft, moveRight;
    private boolean shooting = false;
    private long lastShotTime = 0;
    private final long shootDelay = 400; // 0.4s giữa 2 viên
    private static Color defaultColor = Colors.SECONDARY;
    private Color color = defaultColor;


    public Paddle(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = defaultColor;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public double getSpeed() { return speed; }
    public boolean isMoveLeft() { return moveLeft; }
    public boolean isMoveRight() { return moveRight; }

    public boolean isShooting() { return shooting; }
    public void setShooting(boolean shooting) { this.shooting = shooting; }

    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setWidth(double width) { this.width = width; }
    public void setHeight(double height) { this.height = height; }
    public void setSpeed(double speed) { this.speed = speed; }
    public void setMoveLeft(boolean moveLeft) { this.moveLeft = moveLeft; }
    public void setMoveRight(boolean moveRight) { this.moveRight = moveRight; }

    public void update(double deltaTime) {
        if (moveLeft) x -= speed * deltaTime;
        if (moveRight) x += speed * deltaTime;

        double halfWidth = width / 2;
        if (x - halfWidth < 0) x = halfWidth;
        if (x + halfWidth > 800) x = 800 - halfWidth;
    }

    public void render(GraphicsContext gc) {
        gc.setFill(color != null ? color : defaultColor);
        gc.fillRect(x - width / 2, y - height / 2, width, height);
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
        return new Rectangle2D(x - width / 2, y - height / 2, width, height);
    }

    public Bullet tryShoot() {
        long now = System.currentTimeMillis();
        if (shooting && now - lastShotTime >= shootDelay) {
            lastShotTime = now;

            double bulletWidth = 4;
            double bulletHeight = 12;

            double bulletX = x - bulletWidth / 2;
            double bulletY = y - height / 2 - bulletHeight;

            return new Bullet(bulletX, bulletY);
        }
        return null;
    }

    public static void setDefaultColor(Color c) {
        if (c != null) defaultColor = c;
    }

    public static Color getDefaultColor() {
        return defaultColor;
    }

    public void setColor(Color c) {
        if (c != null) this.color = c;
    }

    public Color getColor() { return color; }
}