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
        if (lastTime > 0) {
            double deltaTime = (now - lastTime) / 1_000_000_000.0;
            game.update(deltaTime);
            game.render();
        }
        lastTime = now;
    }
}