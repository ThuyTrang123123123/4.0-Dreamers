package entities.powerups;

import core.World;
import javafx.scene.paint.Color;

/**
 * PowerUp loại: Extra Life — tăng 1 mạng khi ăn.
 */
public class ExtraLife extends PowerUp {

    public ExtraLife(double x, double y) {
        super(x, y, 18, 18, Color.RED);
    }

    @Override
    public void onCollected(World world) {
        world.getScoring().addLife();
    }
}
