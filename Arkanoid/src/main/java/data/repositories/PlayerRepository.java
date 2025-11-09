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
}