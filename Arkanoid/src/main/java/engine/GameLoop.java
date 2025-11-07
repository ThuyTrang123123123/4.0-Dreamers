// engin/GameLoop.java
package engine;
import javafx.animation.AnimationTimer;
import core.Game;
import javafx.application.Platform;

public class GameLoop implements Runnable {
    private volatile boolean running;
    private final int FPS = 60;
    private final long FRAME_TIME = 1000 / FPS; // ~16ms

    private final core.Game game;

    public GameLoop(core.Game game) {
        this.game = game;
    }

    public boolean isRunning() {
        return running;
    }

    public void start() {
        running = true;
        new Thread(this).start();
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        long lastTime = System.currentTimeMillis();

        while (running) {
            long startTime = System.currentTimeMillis();
            double deltaTime = (startTime - lastTime) / 1000.0;

            // Bước 1: Cập nhật logic game
            game.update(deltaTime);

            // Bước 2: Vẽ game
            Platform.runLater(() -> game.render());


            // Bước 3: Ngủ để duy trì FPS
            long elapsedTime = System.currentTimeMillis() - startTime;
            long sleepTime = FRAME_TIME - elapsedTime;
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            lastTime = startTime;
        }
    }
}