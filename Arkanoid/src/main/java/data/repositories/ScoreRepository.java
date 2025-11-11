package data.repositories;

import data.Storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreRepository {
    private final Storage storage;
    private static final String SCORES_KEY = "scores";

    public ScoreRepository(Storage storage) {
        this.storage = storage;
    }

    public void saveScore(int level, int score) {
        List<Map<String, Object>> scores = storage.loadList(SCORES_KEY);
        boolean updated = false;
        for (Map<String, Object> entry : scores) {
            if ((int) entry.get("level") == level && (int) entry.get("score") < score) {
                entry.put("score", score);
                updated = true;
                break;
            }
        }
        if (!updated) {
            Map<String, Object> newEntry = new HashMap<>();
            newEntry.put("level", level);
            newEntry.put("score", score);
            scores.add(newEntry);
        }
        storage.saveList(SCORES_KEY, scores);
    }

    public int getHighScore(int level) {
        List<Map<String, Object>> scores = storage.loadList(SCORES_KEY);
        for (Map<String, Object> entry : scores) {
            if ((int) entry.get("level") == level) {
                return (int) entry.get("score");
            }
        }
        return 0;
    }

    public List<Map<String, Object>> getAllHighScores() {
        return storage.loadList(SCORES_KEY);
    }

    public void resetScores() {
        storage.delete(SCORES_KEY);
    }

    public int getBestScore(int level) {
        List<Map<String, Object>> scores = storage.loadList(SCORES_KEY);
        for (Map<String, Object> entry : scores) {
            if ((int) entry.get("level") == level) {
                return (int) entry.get("score");
            }
        }
        return 0;
    }

    // === Chỉ lưu điểm nếu cao hơn điểm cũ ===
    public void saveBestScoreIfHigher(int level, int newScore) {
        List<Map<String, Object>> scores = storage.loadList(SCORES_KEY);
        boolean updated = false;

        for (Map<String, Object> entry : scores) {
            int entryLevel = (int) entry.get("level");
            int oldScore = (int) entry.get("score");

            if (entryLevel == level) {
                if (newScore > oldScore) {
                    entry.put("score", newScore);
                }
                updated = true;
                break;
            }
        }

        if (!updated) {
            Map<String, Object> newEntry = new HashMap<>();
            newEntry.put("level", level);
            newEntry.put("score", newScore);
            scores.add(newEntry);
        }

        storage.saveList(SCORES_KEY, scores);
    }

}