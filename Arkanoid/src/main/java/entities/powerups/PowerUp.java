package entities.powerups;

import core.Config;
import core.World;
import entities.Paddle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import javafx.geometry.Rectangle2D;

/**
 * PowerUp - Lớp cơ sở cho tất cả các vật phẩm rơi xuống (Bonus, Buff,...)
 * Có thể được mở rộng để tạo nhiều loại khác nhau như BonusCoin, ExtraLife,...
 */
public abstract class PowerUp {
    protected double x, y;           // Tọa độ
    protected double width, height;  // Kích thước
    protected double velocityY = 100; // Tốc độ rơi
    protected Color color = Color.AQUA;           // Màu hiển thị
    protected boolean collected = false; // Đã được ăn chưa
    protected boolean active = true;     // Còn tồn tại trên màn hình không
    private boolean firstUpdate = true;
    protected  Image image;




    public PowerUp(double x, double y, double width, double height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    /**
     * Cập nhật vị trí của power-up mỗi frame
     */
    public void update(double dt) {
        if (!active) return;


        if (firstUpdate) {
            firstUpdate = false;
            return;
        }
        y += velocityY * dt;
        if (y > Config.SCREEN_HEIGHT) {
            active = false; // rơi ra khỏi màn hình thì biến mất
        }
    }

    /**
     * Vẽ power-up
     */
    public void render(GraphicsContext gc) {
        if (!active) return;
        gc.setFill(color);
        gc.fillOval(x, y, width, height);
        System.out.println("da goi render cho PU");
    }

    /**
     * Kiểm tra va chạm với paddle
     */
    public abstract Rectangle2D getBounds();



    /**
     * Khi paddle ăn được power-up
     */
    public void collect(World world) {
        collected = true;
        active = false;
        onCollected(world);
    }

    /**
     * Hành vi riêng của từng loại power-up khi được ăn
     */
    public abstract void onCollected(World world);

    // === Getter / Setter ===
    public boolean isActive() { return active; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }

    public void setActive(boolean active) {
        this.active = active;
    }

    //Đặt lại x,y
    public void reset(double x, double y) {
        this.x = x;
        this.y = y;
        this.active = true;
    }
    //deactivate :tat
    public void deactivate() {
        this.active = false;
    }





}