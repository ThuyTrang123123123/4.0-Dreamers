package engine;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class PlayExploding {

    private Sprite explosionSprite;
    private double x, y;
    private boolean finished = false;
    Image explosionSheet = new Image(getClass().getResource("/images/ESS.png").toExternalForm());

    public PlayExploding( int columns, int rows, double x, double y) {
        this.explosionSprite = new Sprite(explosionSheet, columns, rows);
        this.x = x;
        this.y = y;
    }

    public void start(GraphicsContext gc) {
        explosionSprite.reset();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (finished) {
                    stop();
                    return;
                }
                explosionSprite.update();
                explosionSprite.render(gc, x, y);
                if (explosionSprite.isLastFrame()) {
                    finished = true;
                }
            }
        };

        timer.start();
    }

    public boolean isFinished() {
        return finished;
    }
}