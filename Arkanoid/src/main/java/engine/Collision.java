package engine;

import entities.Ball;
import entities.Paddle;
import entities.bricks.Brick;

import java.util.List;

public class Collision {

    // Va chạm biên màn hình
    public static void checkWallCollision(Ball ball, double screenWidth, double screenHeight) {
        if (ball.getLeft() <= 0 || ball.getRight() >= screenWidth) {
            ball.reverseX();
        }
        if (ball.getTop() <= 0) {
            ball.reverseY();
        }
        if (ball.getBottom() >= screenHeight) {
            ball.setLost(true);
        }
    }

    // Va chạm với paddle
    public static void checkPaddleCollision(Ball ball, Paddle paddle) {
        if (ball.getBounds().intersects(paddle.getBounds())) {
            ball.reverseY();
            double hitPos = (ball.getX() + ball.getDiameter()/2) - (paddle.getX() + paddle.getWidth()/2);
            ball.setVelocityX(hitPos * 2); // thay đổi hướng tùy theo vị trí va chạm
        }
    }

    // Va chạm với gạch
    public static void checkBrickCollision(Ball ball, List<Brick> bricks) {
        for (Brick brick : bricks) {
            if (!brick.isDestroyed() && ball.getBounds().intersects(brick.getBounds())) {
                brick.hit();
                reflectBall(ball, brick);
                break; // chỉ xử lý 1 gạch mỗi frame
            }
        }
    }

    // Xác định hướng phản xạ bóng sau va chạm với gạch
    private static void reflectBall(Ball ball, Brick brick) {
        double overlapLeft = ball.getRight() - brick.getX();
        double overlapRight = brick.getX() + brick.getWidth() - ball.getLeft();
        double overlapTop = ball.getBottom() - brick.getY();
        double overlapBottom = brick.getY() + brick.getHeight() - ball.getTop();

        boolean fromLeft = overlapLeft < overlapRight && overlapLeft < overlapTop && overlapLeft < overlapBottom;
        boolean fromRight = overlapRight < overlapLeft && overlapRight < overlapTop && overlapRight < overlapBottom;
        boolean fromTop = overlapTop < overlapBottom && overlapTop < overlapLeft && overlapTop < overlapRight;
        boolean fromBottom = overlapBottom < overlapTop && overlapBottom < overlapLeft && overlapBottom < overlapRight;

        if (fromLeft || fromRight) {
            ball.reverseX();
        } else if (fromTop || fromBottom) {
            ball.reverseY();
        }
    }
}