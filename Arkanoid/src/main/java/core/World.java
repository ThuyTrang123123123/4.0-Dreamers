package core;

import entities.Ball;
import entities.Paddle;
import entities.bricks.Brick;
import javafx.scene.canvas.Canvas;

import java.util.ArrayList;
import java.util.List;

public class World {
    private Paddle paddle;
    private Ball ball;
    private List<Brick> bricks;

    public void init(Canvas canvas) {
        paddle = new Paddle(350, 550, 100, 15);
        ball = new Ball(395, 530, 10, 250);
        bricks = new ArrayList<>();
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                bricks.add(new Brick(60 + col * 70, 50 + row * 30, 60, 20));
            }
        }
    }

    public Paddle getPaddle() { return paddle; }
    public Ball getBall() { return ball; }
    public List<Brick> getBricks() { return bricks; }
}