// data/repositories/PlayerRepository.java
package data.repositories;

import data.Storage;

import java.util.Map;

public class PlayerRepository {
    private final Storage storage;
    private static final String PLAYER_KEY = "player";

    public PlayerRepository(Storage storage) {
        this.storage = storage;
    }

    public void savePlayer(String name, int currentLevel) {
        Map<String, Object> data = storage.load(PLAYER_KEY);
        data.put("name", name);
        data.put("currentLevel", currentLevel);
        storage.save(PLAYER_KEY, data);
    }

    public String getPlayerName() {
        Map<String, Object> data = storage.load(PLAYER_KEY);
        return (String) data.getOrDefault("name", "Guest");
    }

    public int getCurrentLevel() {
        Map<String, Object> data = storage.load(PLAYER_KEY);
        return (int) data.getOrDefault("currentLevel", 1);
    }

    public void resetPlayer() {
        storage.delete(PLAYER_KEY);
    }

    public int getHighestLevelUnlocked() {
        try {
            Map<String, Object> data = storage.load("player");
            Object val = data.get("highestLevelUnlocked");
            if (val instanceof Number) {
                return ((Number) val).intValue();
            }
            // fallback nếu chưa có thì coi như level 1 mở sẵn
            return 1;
        } catch (Exception e) {
            return 1;
        }
    }

    public void setHighestLevelUnlocked(int level) {
        try {
            Map<String, Object> data = storage.load("player");
            data.put("highestLevelUnlocked", Math.max(1, level));
            storage.save("player", data);
        } catch (Exception e) {
            System.err.println("Không lưu được highestLevelUnlocked: " + e.getMessage());
        }
    }

}