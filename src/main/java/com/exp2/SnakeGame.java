package com.exp2;

import java.util.*;

public class SnakeGame {

    public static class Point {
        public int x, y;
        public Point(int x, int y) { this.x = x; this.y = y; }
    }

    private List<Point> snake = new ArrayList<>();
    private Point food = new Point(5, 5);
    private Point direction = new Point(1, 0); 
    private int score = 0;
    private int lives = 3;
    private int timerSeconds = 0; // ⏱️ Added Timer Variable
    private int tickCount = 0;    // Counter to track 1-second intervals
    private boolean isPaused = false;
    private boolean isGameOver = false;

    public SnakeGame() {
        resetGame();
    }

    public synchronized void resetGame() {
        snake.clear();
        snake.add(new Point(10, 10));
        snake.add(new Point(9, 10));
        snake.add(new Point(8, 10));
        direction = new Point(1, 0);
        score = 0;
        lives = 3;
        timerSeconds = 0;
        tickCount = 0;
        isPaused = false;
        isGameOver = false;
        spawnFood();
    }

    public synchronized void setDirection(String dir) {
        if (isPaused || isGameOver) return;
        switch (dir) {
            case "UP": if (direction.y == 0) direction = new Point(0, -1); break;
            case "DOWN": if (direction.y == 0) direction = new Point(0, 1); break;
            case "LEFT": if (direction.x == 0) direction = new Point(-1, 0); break;
            case "RIGHT": if (direction.x == 0) direction = new Point(1, 0); break;
        }
    }

    public synchronized void togglePause() {
        this.isPaused = !this.isPaused;
    }

    public synchronized void tick() {
        if (isPaused || isGameOver) return;

        // Since tick runs every 100ms (10 times a second):
        tickCount++;
        if (tickCount >= 10) {
            timerSeconds++;
            tickCount = 0;
        }

        Point head = new Point(snake.get(0).x + direction.x, snake.get(0).y + direction.y);

        // Wall Collision Check (20x20 Grid)
        if (head.x < 0 || head.x >= 20 || head.y < 0 || head.y >= 20) {
            handleLifeLoss();
            return;
        }

        // Self Collision Check
        for (Point segment : snake) {
            if (segment.x == head.x && segment.y == head.y) {
                handleLifeLoss();
                return;
            }
        }

        snake.add(0, head);

        // Food Eating Check
        if (head.x == food.x && head.y == food.y) {
            score += 10;
            spawnFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    private void handleLifeLoss() {
        lives--;
        if (lives <= 0) {
            isGameOver = true;
        } else {
            snake.clear();
            snake.add(new Point(10, 10));
            snake.add(new Point(9, 10));
            snake.add(new Point(8, 10));
            direction = new Point(1, 0);
        }
    }

    private void spawnFood() {
        Random rand = new Random();
        food = new Point(rand.nextInt(20), rand.nextInt(20));
    }

    // Export state including timer to JSON
    public synchronized String toJson() {
        StringBuilder json = new StringBuilder("{");
        json.append("\"score\":").append(score).append(",");
        json.append("\"lives\":").append(lives).append(",");
        json.append("\"timerSeconds\":").append(timerSeconds).append(","); // ⏱️ Send Timer
        json.append("\"isPaused\":").append(isPaused).append(",");
        json.append("\"isGameOver\":").append(isGameOver).append(",");
        
        json.append("\"food\":{\"x\":").append(food.x).append(",\"y\":").append(food.y).append("},");

        json.append("\"snake\":[");
        for (int i = 0; i < snake.size(); i++) {
            Point p = snake.get(i);
            json.append("{\"x\":").append(p.x).append(",\"y\":").append(p.y).append("}");
            if (i < snake.size() - 1) json.append(",");
        }
        json.append("]}");
        return json.toString();
    }
}