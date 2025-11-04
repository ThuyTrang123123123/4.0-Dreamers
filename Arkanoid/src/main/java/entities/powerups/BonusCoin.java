package entities.powerups;

import core.World;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class BonusCoin extends PowerUp {
    private double radius;
    private double speed;
    private boolean firstUpdate = true;

    private double zigzagTimer = 0;
    private double zigzagInterval = 1; // đổi hướng mỗi ziczagInterval giây
    private int directionX = 1; // 1: phải, -1: trái

    private int value = 10; // điểm thưởng khi ăn coin

    public BonusCoin(double x, double y, double radius, double speed) {
        super(x, y, radius * 2, radius * 2, null); // width & height từ bán kính
        this.radius = radius;
        this.speed = speed;
        image= new Image(getClass().getResourceAsStream("/images/bonusCoin.png"));
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
        this.active = false;
    }

    @Override
    public void update(double dt) {
        if (!active || collected) return;

        if (firstUpdate) {
            firstUpdate = false;
            return;
        }

        x += directionX * 2 * speed * dt;
        y += speed * dt;

        zigzagTimer += dt;
        if (zigzagTimer >= zigzagInterval) {
            directionX *= -1;
            zigzagTimer = 0;
        }

        if (y > core.Config.SCREEN_HEIGHT) {
            active = false;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!active || collected) return;
        gc.drawImage(image, x - radius, y - radius, radius * 2, radius * 2);
    }

    @Override
    public void onCollected(World world) {
        boolean hasFlyingBall = world.getBalls().stream().anyMatch(ball -> !ball.isStickToPaddle());
        if (hasFlyingBall) {
            world.getScoring().addScore(value);
        }


    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(x - radius, y - radius, radius * 2, radius * 2);
    }
}