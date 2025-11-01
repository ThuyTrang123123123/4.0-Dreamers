package engine;

import core.World;
import entities.Ball;
import entities.Paddle;
import entities.bricks.Brick;

import java.util.List;

/**
 * Collision - Xử lý va chạm trong game
 */
public class Collision {

    /**
     * Va chạm với biên màn hình
     */
    public static void checkWallCollision(Ball ball, double screenWidth, double screenHeight) {
        // Trái
        if (ball.getLeft() <= 0) {
            ball.setX(ball.getRadius());
            ball.reverseX();
        }

        // Phải
        if (ball.getRight() >= screenWidth) {
            ball.setX(screenWidth - ball.getRadius());
            ball.reverseX();
        }

        // Trên
        if (ball.getTop() <= 0) {
            ball.setY(ball.getRadius());
            ball.reverseY();
        }

        // Dưới (bóng rơi)
        if (ball.getBottom() >= screenHeight) {
            ball.setLost(true);
        }
    }

    /**
     * Va chạm với paddle
     */
    public static void checkPaddleCollision(Ball ball, Paddle paddle) {
        if (ball.getBounds().intersects(paddle.getBounds())) {
            ball.setY(paddle.getY() - ball.getDiameter());
            ball.reverseY();

            // Thay đổi hướng bóng tùy vị trí va chạm
            double hitPos = (ball.getX() + ball.getDiameter()/2) - (paddle.getX() + paddle.getWidth()/2);
            ball.setVelocityX(hitPos * 2);
        }
    }

    /**
     * Va chạm với gạch
     * ⭐ QUAN TRỌNG: Truyền thêm World để cập nhật scoring và achievements
     */
    public static void checkBrickCollision(Ball ball, List<Brick> bricks, World world) {
        for (Brick brick : bricks) {
            if (!brick.isDestroyed() && ball.getBounds().intersects(brick.getBounds())) {
                brick.hit();

                systems.AudioSystem.getInstance().playBrickHit();

                world.getScoring().addScore(1);
                world.getScoring().incrementBricksDestroyed();

                world.getAchievements().checkAchievements(
                        world.getScoring(),
                        world.getLevel().getCurrentLevel()
                );

                reflectBall(ball, brick);

                break;
            }
        }
    }

    /**
     * Xác định hướng phản xạ bóng sau va chạm với gạch
     */
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