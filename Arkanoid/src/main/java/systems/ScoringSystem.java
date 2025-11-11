// ScoringSystem.java
package systems;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ScoringSystem {
    private final IntegerProperty score;
    private final IntegerProperty lives;
    private final IntegerProperty bricksDestroyed;
    private static final int MAX_LIVES = 6;

    public ScoringSystem() {
        this.score = new SimpleIntegerProperty(0);
        this.lives = new SimpleIntegerProperty(2);
        this.bricksDestroyed = new SimpleIntegerProperty(0);
    }

    public IntegerProperty scoreProperty() { return score; }
    public IntegerProperty livesProperty() { return lives; }
    public IntegerProperty bricksDestroyedProperty() { return bricksDestroyed; }

    public int getScore() { return score.get(); }
    public int getLives() { return lives.get(); }
    public int getBricksDestroyed() { return bricksDestroyed.get(); }
    public int getMaxLives() { return MAX_LIVES; }

    public void addScore(int points) {
        score.set(score.get() + points);
    }

    public void loseLife() {
        int current = lives.get();
        if (current > 0) {
            lives.set(current - 1);
        }
    }

    public void addLife() {
        int current = lives.get();
        if (current < MAX_LIVES) {
            lives.set(current + 1);
            System.out.println("+1 Mạng! Hiện tại: " + (current + 1) + "/" + MAX_LIVES);
        } else {
            System.out.println("Đã đạt tối đa " + MAX_LIVES + " mạng!");
        }
    }

    public void addLives(int amount) {
        int current = lives.get();
        int newLives = Math.min(current + amount, MAX_LIVES);

        if (newLives > current) {
            lives.set(newLives);
            System.out.println((newLives - current) + " Mạng! Hiện tại: " + newLives + "/" + MAX_LIVES);
        } else {
            System.out.println("Đã đạt tối đa " + MAX_LIVES + " mạng!");
        }
    }

    public void incrementBricksDestroyed(int amount) {
        int currentBricks = bricksDestroyed.get();

        for (int i = 1; i <= amount; i++) {
            if ((currentBricks + i) % 10 == 0) {
                addLife();
            }
        }
        bricksDestroyed.set(currentBricks + amount);
    }

    public boolean canAddLife() {
        return lives.get() < MAX_LIVES;
    }

    public boolean isMaxLives() {
        return lives.get() >= MAX_LIVES;
    }

    public boolean isGameOver() {
        return lives.get() <= 0;
    }

    public void reset() {
        Platform.runLater(() -> {
            score.set(0);
            lives.set(2);
            bricksDestroyed.set(0);
        });
    }
}