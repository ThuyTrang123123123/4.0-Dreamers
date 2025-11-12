package net;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LeaderboardClient
 * Chịu trách nhiệm giao tiếp với API máy chủ Bảng xếp hạng.
 */
public class LeaderboardClient {
    private static final String API_URL = "http://localhost:9091/leaderboard"; // Thay bằng URL thật
    private final ObjectMapper mapper = new ObjectMapper();

    public boolean submitScore(String playerName, int score) {
        try {
            URL url = new URL(API_URL + "/submit");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            Map<String, Object> data = new HashMap<>();
            data.put("player", playerName);
            data.put("score", score);

            String json = mapper.writeValueAsString(data);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes());
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            return responseCode == 200;
        } catch (Exception e) {
            System.err.println("Lỗi gửi score: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lấy top N scores từ server
     * @param top Số lượng top (default 10)
     * @return List<Map> với player & score
     */
    public List<Map<String, Object>> getTopScores(int top) {
        try {
            URL url = new URL(API_URL + "/top?limit=" + top);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                // Kiểm tra Content-Length
                int contentLength = conn.getContentLength();
                if (contentLength == 0) {
                    return new ArrayList<>(); // Trả về list rỗng nếu server gửi body rỗng
                }

                return mapper.readValue(conn.getInputStream(),
                        mapper.getTypeFactory().constructCollectionType(List.class, HashMap.class));
            } else {
                System.err.println("Server trả về mã lỗi: " + responseCode);
                return new ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("Lỗi lấy leaderboard: " + e.getMessage());
            return new ArrayList<>(); // Luôn trả về list rỗng nếu lỗi
        }
    }
}