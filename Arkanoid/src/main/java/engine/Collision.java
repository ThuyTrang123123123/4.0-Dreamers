package engine;

import core.World;
import entities.Ball;
import entities.Bullet;
import entities.Paddle;
import entities.bricks.Brick;
import entities.bricks.ExplodingBrick;
import entities.bricks.HardBrick;
import entities.bricks.UnbreakableBrick;
import entities.powerups.*;
import javafx.scene.image.Image;

import java.util.List;

/**
 *
 */

public class Collision {


    public static boolean isBallTouchingWall(Ball ball, double screenWidth, double screenHeight) {
        return ball.getLeft() <= 0 ||
                ball.getRight() >= screenWidth ||
                ball.getTop() <= 0 ||
                ball.getBottom() >= screenHeight;
    }

    public static boolean isBallTouchingPaddle(Ball ball, Paddle paddle) {
        return ball.getBounds().intersects(paddle.getBounds());
    }

    public static boolean isBallTouchingBrick(Ball ball, Brick brick) {
        return !brick.isDestroyed() && ball.getBounds().intersects(brick.getBounds());
    }

    public static boolean isPowerUpTouchingPaddle(PowerUp pu, Paddle paddle) {
        return pu.isActive() && pu.getBounds().intersects(paddle.getBounds());
    }

    public static boolean isBulletTouchingBrick(Bullet bullet, Brick brick) {
        return bullet.isActive()
                && !brick.isDestroyed()
                && bullet.getBounds().intersects(brick.getBounds());
    }


    public static void handleBallWallCollision(Ball ball, double screenWidth, double screenHeight) {
        if (ball.getLeft() <= 0) {
            ball.setX(ball.getRadius());
            ball.reverseX();
        }

        if (ball.getRight() >= screenWidth) {
            ball.setX(screenWidth - ball.getRadius());
            ball.reverseX();
        }

        if (ball.getTop() <= 0) {
            ball.setY(ball.getRadius());
            ball.reverseY();
        }

        if (ball.getBottom() >= screenHeight) {
            ball.setLost(true);
        }
    }

    public static void handleBallPaddleCollision(Ball ball, Paddle paddle) {
        ball.setY(paddle.getY() - paddle.getHeight() / 2 - ball.getRadius());
        ball.reverseY();
        double hitPos = (ball.getX() + ball.getDiameter() / 2) - (paddle.getX() + paddle.getWidth() / 2);
        ball.setVelocityX(hitPos * 2);
    }

    public static void handleBallBrickCollision(Ball ball, List<Brick> bricks, World world) {
        for (Brick brick : bricks) {
            if (isBallTouchingBrick(ball, brick)) {
                // Nếu là ExplodingBrick → gọi hit + explodeNearby
                if (brick instanceof ExplodingBrick exploding) {
                    exploding.hit();
                    int count = exploding.exployNearbyAndCount(bricks);
                    world.getScoring().addScore(count);
                    world.getScoring().incrementBricksDestroyed(count);
                } else if (brick instanceof UnbreakableBrick unbreakable) {
                    //Không cộng điểm, ko cộng gạch phá
                } else if (brick instanceof HardBrick hard) {
                    hard.hit();
                    if (hard.isDestroyed()) {
                        world.getScoring().addScore(1);
                        world.getScoring().incrementBricksDestroyed(1);
                    }
                } else {
                    brick.hit();// gạch thường
                    world.getScoring().addScore(1);
                    world.getScoring().incrementBricksDestroyed(1);
                }
                systems.AudioSystem.getInstance().playBrickHit();
                new Physics().reflectBall(ball, brick);
                if (brick.isDestroyed()) {
                    double chance = Math.random();
                    if (chance < 0.25) {
                        Class<? extends PowerUp> type;
                        double r = Math.random();
                        if (r < 1.0 / 9) {
                            type = BonusCoin.class;
                        } else if (r < 2.0 / 9) {
                            type = ExtraLife.class;
                        } else if (r < 3.0 / 9) {
                            type = EnlargePaddle.class;
                        } else if (r < 4.0 / 9) {
                            type = DoubleBall.class;
                        } else if (r < 5.0 / 9) {
                            type = ShrinkPaddle.class;
                        } else if (r < 6.0 / 9) {
                            type = SlowBall.class;
                        } else if (r < 7.0 / 9) {
                            type = SpeedBall.class;
                        } else if (r < 8.0 / 9) {
                            type = EnLargeBall.class;
                        } else {
                            type = ShootPaddle.class;
                        }
                        PowerUp pu = world.getPowerUpPool().acquire(type, brick.getX(), brick.getY());
                        world.getPowerUps().add(pu);
                    }
                }
                break;
            }
        }
    }

    public static void handlePowerUpCollision(PowerUp pu, Paddle paddle, World world) {
        pu.collect(world);
    }

    public static void handleBulletBrickCollision(List<Bullet> bullets, List<Brick> bricks, World world) {
        for (Bullet bullet : bullets) {
            if (!bullet.isActive()) continue;

            for (Brick brick : bricks) {
                if (isBulletTouchingBrick(bullet, brick)) {
                    // Nếu là ExplodingBrick → gọi hit + explodeNearby
                    if (brick instanceof ExplodingBrick exploding) {
                        exploding.hit();
                        int count = exploding.exployNearbyAndCount(bricks);
                        world.getScoring().addScore(count);
                        world.getScoring().incrementBricksDestroyed(count);
                    } else if (brick instanceof UnbreakableBrick unbreakable) {
                        //Không cộng điểm, ko cộng gạch phá
                    }
                    else if (brick instanceof HardBrick hard) {
                        hard.hit();
                        if (hard.isDestroyed()) {
                            world.getScoring().addScore(1);
                            world.getScoring().incrementBricksDestroyed(1);
                        }
                    }
                    else {
                        brick.hit();// gạch thường or gạch hard
                        world.getScoring().addScore(1);
                        world.getScoring().incrementBricksDestroyed(1);
                    }
                    bullet.deactivate();
                    break;
                }
            }
        }
    }
}