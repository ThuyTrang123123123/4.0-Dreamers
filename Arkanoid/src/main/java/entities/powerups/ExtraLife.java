package entities.powerups;

import core.World;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

import javafx.geometry.Rectangle2D;

/**
 * PowerUp loại: Extra Life — tăng 1 mạng khi ăn.
 */
public class ExtraLife extends PowerUp {

    public ExtraLife(double x, double y) {
        super(x, y, 18, 18, Color.RED);
        image = new Image(getClass().getResource("/images/ExtraLife.png").toExternalForm());
    }
    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(x - width / 2, y - height / 2, width, height);
    }



    @Override
    public void render(GraphicsContext gc) {
        if (!isActive()) return;



        if (image.isError()) {
            System.out.println("Không thể tải ảnh: /images/ExtraLife.png");
            return;
        }

        // Vẽ ảnh từ tâm → dịch về góc trên trái
        double drawX = getX() - getWidth() / 2;
        double drawY = getY() - getHeight() / 2;

        gc.drawImage(image, drawX, drawY, getWidth(), getHeight());
    }



    @Override
    public void onCollected(World world) {
        world.getScoring().addLife();
        System.out.println(" da add life");
    }
}