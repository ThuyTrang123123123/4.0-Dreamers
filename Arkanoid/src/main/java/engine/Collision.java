package engine;

import core.World;
import entities.Ball;
import entities.Paddle;
import entities.bricks.Brick;
import engine.Physics;
import entities.bricks.ExplodingBrick;
import entities.powerups.BonusCoin;

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


    // Bóng Va chạm với paddle
    public static void checkPaddleCollision(Ball ball, Paddle paddle) {
        if (ball.getBounds().intersects(paddle.getBounds())) {
            ball.setY(paddle.getY()-paddle.getHeight()/2 - ball.getRadius());
            ball.reverseY();
            double hitPos = (ball.getX() + ball.getDiameter()/2) - (paddle.getX() + paddle.getWidth()/2);
            ball.setVelocityX(hitPos * 2); // thay đổi hướng tùy theo vị trí va chạm
        }
    }
    // Va chạm với gạch
    public static void checkBrickCollision(Ball ball, List<Brick> bricks, World world) {
        for (Brick brick : bricks) {
            if (!brick.isDestroyed() && ball.getBounds().intersects(brick.getBounds())) {
                brick.hit();
                // Cộng điểm: 1 gạch = 1 điểm
                world.getScoring().addScore(1);
                // Nếu là ExplodingBrick thì nổ lan
                if (brick instanceof ExplodingBrick explodingBrick) {
                    explodingBrick.explodeNearby(bricks);
                }
                world.getScoring().incrementBricksDestroyed();
                // Tăng số gạch đã phá (tự động thưởng mạng mỗi 3 gạch)
                world.getScoring().incrementBricksDestroyed();
                Physics physics=new Physics();
                physics.reflectBall(ball,brick);
                break; // chỉ xử lý 1 gạch mỗi frame
            }
        }
    }
    // Bomus Coin và paddle
    public static void checkPaddleCollision(BonusCoin bonusCoin, Paddle paddle,World world) {
        if (!bonusCoin.isCollected()&&bonusCoin.getBounds().intersects(paddle.getBounds())) {
            bonusCoin.setCollected(true);
            world.getScoring().addScore(bonusCoin.getValue());
        }
    }


}