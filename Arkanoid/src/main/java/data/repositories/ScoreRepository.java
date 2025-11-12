package data.repositories;

import data.Storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import data.AccountManager;

/**
 * Quản lý lưu trữ và truy xuất điểm số của người dùng.
 * Tự động phân biệt điểm của người dùng đã đăng nhập và khách (guest).
 */

public class ScoreRepository {
    private final Storage storage;
   // private static final String SCORES_KEY = "scores";

    /**
     * Lấy key (tên file) để lưu/tải điểm cá nhân, dựa trên người dùng đã đăng nhập.
     */
    private String getDynamicScoresKey() {
        String username = AccountManager.getLoggedInUser();
        if (username == null || username.isEmpty()) {
            return "scores_guest"; // Dùng file riêng cho khách (Guest)
        }
        String safeUsername = username.replaceAll("[^a-zA-Z0-9_.-]", "_");
        return "scores_" + safeUsername;
    }
    public ScoreRepository(Storage storage) {
        this.storage = storage;
    }

    public void saveScore(int level, int score) {
        List<Map<String, Object>> scores = storage.loadList(getDynamicScoresKey());
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
        storage.saveList(getDynamicScoresKey(), scores);
    }

    public int getHighScore(int level) {
        List<Map<String, Object>> scores = storage.loadList(getDynamicScoresKey());
        for (Map<String, Object> entry : scores) {
            if ((int) entry.get("level") == level) {
                return (int) entry.get("score");
            }
        }
        return 0;
    }

    public List<Map<String, Object>> getAllHighScores() {
        return storage.loadList(getDynamicScoresKey());
    }

    public void resetScores() {
        storage.delete(getDynamicScoresKey());
    }

    public int getBestScore(int level) {
        List<Map<String, Object>> scores = storage.loadList(getDynamicScoresKey());
        for (Map<String, Object> entry : scores) {
            if ((int) entry.get("level") == level) {
                return (int) entry.get("score");
            }
        }
        return 0;
    }

    // === Chỉ lưu điểm nếu cao hơn điểm cũ ===
    public void saveBestScoreIfHigher(int level, int newScore) {
        List<Map<String, Object>> scores = storage.loadList(getDynamicScoresKey());
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

        storage.saveList(getDynamicScoresKey(), scores);
    }

}