// src/main/java/net/MockServer.java
package net;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.Executors;

public class MockServer {
    private static final int PORT = 9091;
    private static final List<Map<String, Object>> scores = new ArrayList<>();
    private static HttpServer server;
    private final ObjectMapper mapper = new ObjectMapper();

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.setExecutor(Executors.newFixedThreadPool(4));

        // Endpoint submit score
        server.createContext("/leaderboard/submit", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if ("POST".equals(exchange.getRequestMethod())) {
                    InputStreamReader isr = new InputStreamReader(exchange.getRequestBody());
                    Map<String, Object> data = mapper.readValue(isr, HashMap.class);
                    scores.add(data);

                    // Sort by score desc
                    scores.sort((a, b) -> ((Integer) b.get("score")).compareTo((Integer) a.get("score")));

                    exchange.sendResponseHeaders(200, -1);
                } else {
                    exchange.sendResponseHeaders(405, -1); // Method not allowed
                }
                exchange.close();
            }
        });

        // Endpoint get top
        server.createContext("/leaderboard/top", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String query = exchange.getRequestURI().getQuery();
                int limit = query != null && query.contains("limit=") ? Integer.parseInt(query.split("=")[1]) : 10;

                List<Map<String, Object>> top = scores.subList(0, Math.min(limit, scores.size()));

                String json = mapper.writeValueAsString(top);
                exchange.sendResponseHeaders(200, json.length());
                OutputStream os = exchange.getResponseBody();
                os.write(json.getBytes());
                os.close();
            }
        });

        server.start();
        System.out.println("MockServer chạy tại http://localhost:" + PORT);
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