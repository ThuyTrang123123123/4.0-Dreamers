package systems;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * ScoringSystem.
 * Chịu trách nhiệm quản lý điểm số, mạng sống và số lượng gạch đã phá hủy.
 */
public class ScoringSystem {
    private final IntegerProperty score;
    private final IntegerProperty lives;
    private final IntegerProperty bricksDestroyed;
    private static final int MAX_LIVES = 6;

    public ScoringSystem() {
        this.score = new SimpleIntegerProperty(0);
        this.lives = new SimpleIntegerProperty(2);
        this.bricksDestroyed = new SimpleIntegerProperty(0);
        score.addListener((obs, oldV, newV) -> {
            int delta = newV.intValue() - oldV.intValue();
            if (delta > 0) {
                addToWallet(delta);
                setWalletSnapshot(newV.intValue());
                System.out.println("[WALLET] +"+delta+" (score "+oldV+"→"+newV+"), wallet="+getWallet());
            }
        });
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    public IntegerProperty livesProperty() {
        return lives;
    }

    public IntegerProperty bricksDestroyedProperty() {
        return bricksDestroyed;
    }

    public int getScore() {
        return score.get();
    }

    public int getLives() {
        return lives.get();
    }

    public int getBricksDestroyed() {
        return bricksDestroyed.get();
    }

    public int getMaxLives() {
        return MAX_LIVES;
    }

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

    public int getPoints() {
        return getScore();
    }

    public boolean spendPoints(int cost) {
        if (getScore() >= cost) {
            score.set(getScore() - cost);
            return true;
        } else {
            return false;
        }
    }

    private static final String KEY_WALLET = "wallet.";
    private static final String KEY_WALLET_SNAPSHOT = "wallet.snap";

    private data.Storage _store() {
        return data.JsonStorage.getInstance();
    }

    private String currentPlayerKey() {
        try {
            String username = data.AccountManager.getLoggedInUser();
            if (username == null || username.isEmpty()) {
                return "Guest";
            }
            return username;
        } catch (Exception e) {
            return "Guest";
        }
    }

    private String keyWallet() {
        return KEY_WALLET + currentPlayerKey();
    }

    private String keySnap() {
        return KEY_WALLET_SNAPSHOT + currentPlayerKey();
    }

    public int getWallet() {
        _store().flush();
        return _store().getInt(keyWallet(), 0);
    }

    public void addToWallet(int delta) {
        if (delta <= 0) return;
        int cur = getWallet();
        _store().putInt(keyWallet(), Math.max(0, cur + delta));
        _store().flush();
    }

    public boolean spendWallet(int cost) {
        if (cost <= 0) return true;
        int cur = getWallet();
        if (cur < cost) return false;
        _store().putInt(keyWallet(), cur - cost);
        _store().flush();
        return true;
    }

    public void setWalletSnapshot(int scoreNow) {
        _store().putInt(keySnap(), Math.max(0, scoreNow));
        _store().flush();
    }

    public int getWalletSnapshot() {
        try {
            return _store().getInt(keySnap(), 0);
        } catch (Exception e) {
            return 0;
        }
    }

    public void accumulateToWalletFromSnapshot() {
        int snap = getWalletSnapshot();
        int cur = getScore();
        int delta = cur - snap;
        if (delta > 0) addToWallet(delta);
        setWalletSnapshot(cur);
    }
}