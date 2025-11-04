package entities.powerups;

import core.World;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

import javafx.geometry.Rectangle2D;

/**
 * PowerUp: Extra Life — tăng 1 mạng khi ăn.
 */
public class ExtraLife extends PowerUp {

    public ExtraLife(double x, double y) {
        super(x, y, 18, 18, Color.RED);
        try {
            image = new Image(getClass().getResource("/images/ExtraLife.png").toExternalForm());
        } catch (Exception e) {
            System.err.println("Không thể tải ảnh ExtraLife: " + e.getMessage());
            image = null;
        }
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(x - width / 2, y - height / 2, width, height);
    }


    @Override
    public void render(GraphicsContext gc) {
        if (!isActive()) return;


        // Vẽ ảnh từ tâm → dịch về góc trên trái
        double drawX = getX() - getWidth() / 2;
        double drawY = getY() - getHeight() / 2;

        gc.drawImage(image, drawX, drawY, getWidth(), getHeight());
    }


    @Override
    public void onCollected(World world) {
        boolean hasFlyingBall = world.getBalls().stream().anyMatch(ball -> !ball.isStickToPaddle());
        if (!hasFlyingBall) return;

        world.getScoring().addLife();
        System.out.println(" da add life");

    }
}