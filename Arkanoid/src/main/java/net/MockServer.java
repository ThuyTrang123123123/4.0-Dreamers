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
import java.util.Collections; // <-- Đã thêm import
import java.util.concurrent.Executors;

public class MockServer {
    private static final int PORT = 9091;
    private static final String SCORES_KEY = "leaderboard";
    private static HttpServer server;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Storage storage = new JsonStorage();

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.setExecutor(Executors.newFixedThreadPool(4));

        // === SỬA LỖI KHỞI ĐỘNG (CHỐNG NULL) ===
        List<Map<String, Object>> loadedScores = storage.loadList(SCORES_KEY);

        // 1. Kiểm tra null nếu file không tồn tại
        if (loadedScores == null) {
            loadedScores = new ArrayList<>(); // Tạo list rỗng
        }

        // 2. Bọc trong danh sách thread-safe
        List<Map<String, Object>> scores = Collections.synchronizedList(loadedScores);
        // === KẾT THÚC SỬA LỖI KHỞI ĐỘNG ===


        // Endpoint: /leaderboard/submit (Đã sửa lỗi đa luồng VÀ ép kiểu)
        server.createContext("/leaderboard/submit", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if ("POST".equals(exchange.getRequestMethod())) {
                    try {
                        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody());
                        Map<String, Object> newData = mapper.readValue(isr, HashMap.class);

                        // === KHÓA scores ĐỂ ĐẢM BẢO AN TOÀN KHI GHI ===
                        synchronized (scores) {
                            String newPlayer = (String) newData.get("player");

                            // === SỬA LỖI ÉP KIỂU (NUMBER) ===
                            int newScore = ((Number) newData.get("score")).intValue();
                            boolean playerExists = false;

                            // 1. Tìm
                            for (Map<String, Object> existingEntry : scores) {
                                if (existingEntry.get("player").equals(newPlayer)) {
                                    playerExists = true;

                                    // === SỬA LỖI ÉP KIỂU (NUMBER) ===
                                    int oldScore = ((Number) existingEntry.get("score")).intValue();

                                    if (newScore > oldScore) {
                                        existingEntry.put("score", newScore);
                                    }
                                    break;
                                }
                            }

                            // 2. Thêm mới
                            if (!playerExists) {
                                scores.add(newData);
                            }

                            // 3. Sắp xếp (an toàn)
                            scores.sort((a, b) -> {
                                int scoreA = ((Number) a.get("score")).intValue();
                                int scoreB = ((Number) b.get("score")).intValue();
                                return Integer.compare(scoreB, scoreA); // Giảm dần
                            });

                            // 4. Lưu lại file
                            storage.saveList(SCORES_KEY, scores);
                        } // <-- KẾT THÚC KHỐI SYNCHRONIZED

                        exchange.sendResponseHeaders(200, -1);
                    } catch (Exception e) {
                        System.err.println("!!! Lỗi Server /submit: " + e.getMessage());
                        e.printStackTrace();
                        exchange.sendResponseHeaders(500, -1); // Gửi lỗi 500
                    }
                } else {
                    exchange.sendResponseHeaders(405, -1);
                }
                exchange.close();
            }
        });

        // Endpoint: /leaderboard/top (Đã sửa lỗi đa luồng)
        server.createContext("/leaderboard/top", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                try {
                    String query = exchange.getRequestURI().getQuery();
                    int limit = query != null && query.contains("limit=")
                            ? Integer.parseInt(query.split("=")[1])
                            : 10;

                    String json;
                    // === KHÓA scores ĐỂ ĐẢM BẢO AN TOÀN KHI ĐỌC ===
                    synchronized (scores) {
                        List<Map<String, Object>> top = new ArrayList<>(scores.subList(0, Math.min(limit, scores.size())));
                        json = mapper.writeValueAsString(top);
                    } // <-- KẾT THÚC KHỐI SYNCHRONIZED

                    exchange.sendResponseHeaders(200, json.length());
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(json.getBytes());
                    }
                } catch (Exception e) {
                    System.err.println("!!! Lỗi Server /top: " + e.getMessage());
                    e.printStackTrace();
                    exchange.sendResponseHeaders(500, -1); // Gửi lỗi 500
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