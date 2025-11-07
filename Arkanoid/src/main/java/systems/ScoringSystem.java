package systems;

public class ScoringSystem {
    private int score;
    private int lives;
    private int bricksDestroyed;

    public ScoringSystem() {
        this.score = 0;
        this.lives = 1;
        this.bricksDestroyed = 0;
    }

    // Getter methods
    public int getScore() { return score; }
    public int getLives() { return lives; }
    public int getBricksDestroyed() { return bricksDestroyed; }

    // Logic methods
    public void addScore(int points) {
        score += points;
    }

    public void loseLife() {
        if (lives > 0) {
            lives--;
        }
    }

    public void addLife() {
        lives++;
    }

    public void incrementBricksDestroyed(int amount) {
        for (int i = 1; i <= amount; i++) {
            if ((bricksDestroyed + i) % 3 == 0) {
                addLife();
            }
        }
        bricksDestroyed += amount;
    }

    public boolean isGameOver() {
        return lives <= 0;
    }

    public void reset() {
        score = 0;
        lives = 1;
        bricksDestroyed = 0;
    }
}