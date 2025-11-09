package systems;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ScoringSystem {
    private final IntegerProperty score;
    private final IntegerProperty lives;
    private final IntegerProperty bricksDestroyed;

    public ScoringSystem() {
        this.score = new SimpleIntegerProperty(0);
        this.lives = new SimpleIntegerProperty(1);
        this.bricksDestroyed = new SimpleIntegerProperty(0);
    }

    // Getter methods for properties (for binding)
    public IntegerProperty scoreProperty() { return score; }
    public IntegerProperty livesProperty() { return lives; }
    public IntegerProperty bricksDestroyedProperty() { return bricksDestroyed; }

    // Getter methods for values
    public int getScore() { return score.get(); }
    public int getLives() { return lives.get(); }
    public int getBricksDestroyed() { return bricksDestroyed.get(); }

    // Logic methods
    public void addScore(int points) {
        Platform.runLater(() -> score.set(score.get() + points));
    }

    public void loseLife() {
        int current = lives.get();
        if (current > 0) {
            Platform.runLater(() -> lives.set(current - 1));
        }
    }

    public void addLife() {
        Platform.runLater(() -> lives.set(lives.get() + 1));
    }

    public void incrementBricksDestroyed(int amount) {
        int currentBricks = bricksDestroyed.get();
        for (int i = 1; i <= amount; i++) {
            if ((currentBricks + i) % 3 == 0) {
                addLife();
            }
        }
        Platform.runLater(() -> bricksDestroyed.set(currentBricks + amount));
    }

    public boolean isGameOver() {
        return lives.get() <= 0;
    }

    public void reset() {
        Platform.runLater(() -> {
            score.set(0);
            lives.set(1);
            bricksDestroyed.set(0);
        });
    }
}