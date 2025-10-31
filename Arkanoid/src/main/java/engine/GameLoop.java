// engin/GameLoop.java
package engine;
import javafx.animation.AnimationTimer;
import core.Game;

public class GameLoop extends AnimationTimer {
    private Game game;
    private long lastTime = 0;

    public GameLoop(Game game) {
        this.game = game;
    }

    @Override
    public void handle(long now) {
        final double frameRate = 60.0;
        final double interval = 1_000_000_000.0 / frameRate; // nanoseconds per frame

        if (now - lastTime >= interval) {
            double deltaTime = (now - lastTime) / 1_000_000_000.0;
            game.update(deltaTime);
            game.render();
            lastTime = now;
        }
    }


}