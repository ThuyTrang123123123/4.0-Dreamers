package engine;

import entities.Ball;
import entities.Paddle;
import entities.bricks.Brick;

import java.util.List;

public class Collision {

    // Va chạm biên màn hình
    public static void checkWallCollision(Ball ball, double screenWidth, double screenHeight) {
        // Trái
        if (ball.getLeft() <= 0) {
            ball.setX(ball.getRadius()); // đẩy ra khỏi tường
            ball.reverseX();
        }

        // Phải
        if (ball.getRight() >= screenWidth) {
            ball.setX(screenWidth - ball.getRadius()); // đẩy ra khỏi tường
            ball.reverseX();
        }

        // Trên
        if (ball.getTop() <= 0) {
            ball.setY(ball.getRadius());
            ball.reverseY();
        }

        // Dưới
        if (ball.getBottom() >= screenHeight) {
            ball.setLost(true);
        }
    }


    // Va chạm với paddle
    public static void checkPaddleCollision(Ball ball, Paddle paddle) {
        if (ball.getBounds().intersects(paddle.getBounds())) {
            ball.setY(paddle.getY()-paddle.getHeight()/2 - ball.getRadius());
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

    private static void reflectBall(Ball ball, Brick brick) {
        double ballLeft = ball.getLeft();
        double ballRight = ball.getRight();
        double ballTop = ball.getTop();
        double ballBottom = ball.getBottom();

        double brickLeft = brick.getX() - brick.getWidth() / 2;
        double brickRight = brick.getX() + brick.getWidth() / 2;
        double brickTop = brick.getY() - brick.getHeight() / 2;
        double brickBottom = brick.getY() + brick.getHeight() / 2;

        // Tính phần giao nhau
        double overlapLeft = ballRight - brickLeft;
        double overlapRight = brickRight - ballLeft;
        double overlapTop = ballBottom - brickTop;
        double overlapBottom = brickBottom - ballTop;

        double minOverlapX = Math.min(overlapLeft, overlapRight);
        double minOverlapY = Math.min(overlapTop, overlapBottom);

        // Nếu va chạm theo phương ngang
        if (minOverlapX < minOverlapY) {
            ball.reverseX();

            if (overlapLeft < overlapRight) {
                // Va chạm từ bên trái viên gạch
                ball.setX(brickLeft - ball.getRadius());
            } else {
                // Va chạm từ bên phải viên gạch
                ball.setX(brickRight + ball.getRadius());
            }
        } else {
            ball.reverseY();

            if (overlapTop < overlapBottom) {
                // Va chạm từ trên xuống
                ball.setY(brickTop - ball.getRadius());
            } else {
                // Va chạm từ dưới lên
                ball.setY(brickBottom + ball.getRadius());
            }
        }
    }

}