package engine;

import entities.Ball;
import entities.bricks.Brick;

/**
 * Physics- logic bóng bật lại khi va chạm
 */

public class Physics {

    public static void reflectBall(Ball ball, Brick brick) {
        double ballLeft = ball.getLeft();
        double ballRight = ball.getRight();
        double ballTop = ball.getTop();
        double ballBottom = ball.getBottom();

        double brickLeft = brick.getX() - brick.getWidth() / 2;
        double brickRight = brick.getX() + brick.getWidth() / 2;
        double brickTop = brick.getY() - brick.getHeight() / 2;
        double brickBottom = brick.getY() + brick.getHeight() / 2;

        double overlapLeft = ballRight - brickLeft;
        double overlapRight = brickRight - ballLeft;
        double overlapTop = ballBottom - brickTop;
        double overlapBottom = brickBottom - ballTop;

        double minOverlapX = Math.min(overlapLeft, overlapRight);
        double minOverlapY = Math.min(overlapTop, overlapBottom);

        if (minOverlapX < minOverlapY) {
            ball.reverseX();

            if (overlapLeft < overlapRight) {
                ball.setX(brickLeft - ball.getRadius());
            } else {
                ball.setX(brickRight + ball.getRadius());
            }
        } else {
            ball.reverseY();

            if (overlapTop < overlapBottom) {
                ball.setY(brickTop - ball.getRadius());
            } else {
                ball.setY(brickBottom + ball.getRadius());
            }
        }
    }
}