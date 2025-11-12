package net;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.JsonStorage;
import data.Storage;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.Executors;

public class MockServer {
    private static final int PORT = 9091;
    private static final String SCORES_KEY = "leaderboard"; // khóa để lưu vào file
    private static HttpServer server;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Storage storage = new JsonStorage(); // 🔹 sử dụng lớp JsonStorage

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.setExecutor(Executors.newFixedThreadPool(4));

        // Tải dữ liệu từ file
        List<Map<String, Object>> scores = storage.loadList(SCORES_KEY);

        // Endpoint: /leaderboard/submit
        server.createContext("/leaderboard/submit", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if ("POST".equals(exchange.getRequestMethod())) {
                    InputStreamReader isr = new InputStreamReader(exchange.getRequestBody());
                    Map<String, Object> newData = mapper.readValue(isr, HashMap.class);

                    // === LOGIC MỚI ĐỂ ĐẢM BẢO UNIQUE PLAYER ===
                    String newPlayer = (String) newData.get("player");
                    int newScore = (Integer) newData.get("score");
                    boolean playerExists = false;

                    // 1. Tìm xem người chơi đã tồn tại chưa
                    for (Map<String, Object> existingEntry : scores) {
                        if (existingEntry.get("player").equals(newPlayer)) {
                            playerExists = true;
                            int oldScore = (Integer) existingEntry.get("score");

                            // 2. Nếu điểm mới cao hơn, cập nhật điểm cũ
                            if (newScore > oldScore) {
                                existingEntry.put("score", newScore);
                            }
                            break; // Đã tìm thấy, thoát vòng lặp
                        }
                    }

                    // 3. Nếu người chơi không tồn tại, thêm mới
                    if (!playerExists) {
                        scores.add(newData);
                    }
                    // === KẾT THÚC LOGIC MỚI ===

                    // Sắp xếp giảm dần theo điểm
                    scores.sort((a, b) -> ((Integer) b.get("score")).compareTo((Integer) a.get("score")));

                    // Lưu lại vào file
                    storage.saveList(SCORES_KEY, scores);

                    exchange.sendResponseHeaders(200, -1);
                } else {
                    exchange.sendResponseHeaders(405, -1);
                }
                exchange.close();
            }
        });

        // Endpoint: /leaderboard/top
        server.createContext("/leaderboard/top", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String query = exchange.getRequestURI().getQuery();
                int limit = query != null && query.contains("limit=")
                        ? Integer.parseInt(query.split("=")[1])
                        : 10;

                List<Map<String, Object>> top = scores.subList(0, Math.min(limit, scores.size()));
                String json = mapper.writeValueAsString(top);
                exchange.sendResponseHeaders(200, json.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(json.getBytes());
                }
            }
        });

        server.start();
        System.out.println("MockServer chạy tại http://localhost:" + PORT);
        System.out.println("Đã nạp " + scores.size() + " bản ghi leaderboard từ file.");
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            System.out.println("MockServer dừng");
        }
    }

    public static void main(String[] args) throws IOException {
        new MockServer().start();
    }
}
