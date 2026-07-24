package com.exp2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class App {

    private static final SnakeGame game = new SnakeGame();

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Static Files
        server.createContext("/", exchange -> serveFile(exchange, "index.html", "text/html"));
        server.createContext("/style.css", exchange -> serveFile(exchange, "style.css", "text/css"));
        server.createContext("/script.js", exchange -> serveFile(exchange, "script.js", "application/javascript"));

        // Java Game API Endpoints
        server.createContext("/api/state", App::handleGetState);
        server.createContext("/api/action", App::handleAction);

        // Start Java Game Engine Timer (Ticks every 100ms)
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                game.tick();
            }
        }, 0, 100);

        server.start();

        System.out.println("Java Game Server running at http://localhost:8080");
    }

    private static void handleGetState(HttpExchange exchange) throws IOException {
        String jsonResponse = game.toJson();
        sendResponse(exchange, jsonResponse, "application/json");
    }

    private static void handleAction(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            String action = new String(exchange.getRequestBody().readAllBytes()).trim();
            if (action.equalsIgnoreCase("PAUSE")) {
                game.togglePause();
            } else if (action.equalsIgnoreCase("RESTART")) {
                game.resetGame();
            } else {
                game.setDirection(action);
            }
        }
        sendResponse(exchange, "{\"status\":\"ok\"}", "application/json");
    }

    private static void sendResponse(HttpExchange exchange, String response, String contentType) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", contentType);
        byte[] bytes = response.getBytes();
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static void serveFile(HttpExchange exchange, String fileName, String contentType) throws IOException {
        InputStream input = App.class.getClassLoader().getResourceAsStream(fileName);

        if (input == null) {
            String msg = "404 File Not Found";
            exchange.sendResponseHeaders(404, msg.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(msg.getBytes());
            }
            return;
        }

        byte[] data = input.readAllBytes();
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(200, data.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(data);
        }
    }
}