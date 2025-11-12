// data/SQLiteStorage.java
package data;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLiteStorage implements Storage {
    private static final String DB_URL = "jdbc:sqlite:src/main/resources/data/game.db";
    private Connection conn;
    private static final SQLiteStorage INSTANCE = new SQLiteStorage();
    public static SQLiteStorage getInstance() { return INSTANCE; }

    public SQLiteStorage() {
        try {
            conn = DriverManager.getConnection(DB_URL);
            createTableIfNotExists();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTableIfNotExists() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS storage (" +
                "key TEXT PRIMARY KEY, " +
                "value TEXT NOT NULL);";
        conn.createStatement().execute(sql);
    }

    @Override
    public void save(String key, Map<String, Object> data) {
        try {
            String json = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(data);
            PreparedStatement pstmt = conn.prepareStatement("REPLACE INTO storage (key, value) VALUES (?, ?)");
            pstmt.setString(1, key);
            pstmt.setString(2, json);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Object> load(String key) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT value FROM storage WHERE key = ?");
            pstmt.setString(1, key);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String json = rs.getString("value");
                return new com.fasterxml.jackson.databind.ObjectMapper().readValue(json, HashMap.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    @Override
    public void saveList(String key, List<Map<String, Object>> list) {
        try {
            String json = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(list);
            PreparedStatement pstmt = conn.prepareStatement("REPLACE INTO storage (key, value) VALUES (?, ?)");
            pstmt.setString(1, key);
            pstmt.setString(2, json);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Map<String, Object>> loadList(String key) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT value FROM storage WHERE key = ?");
            pstmt.setString(1, key);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String json = rs.getString("value");
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, HashMap.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public void delete(String key) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM storage WHERE key = ?");
            pstmt.setString(1, key);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void putRaw(String key, String value) {
        try (PreparedStatement pstmt =
                     conn.prepareStatement("REPLACE INTO storage (key, value) VALUES (?, ?)")) {
            pstmt.setString(1, key);
            pstmt.setString(2, value);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getRaw(String key) {
        try (PreparedStatement pstmt =
                     conn.prepareStatement("SELECT value FROM storage WHERE key = ?")) {
            pstmt.setString(1, key);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getString("value");
            }
        } catch (Exception e) {
        }
        return null;
    }
}