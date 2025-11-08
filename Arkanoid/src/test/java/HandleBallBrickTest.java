import entities.Ball;
import entities.bricks.Brick;
import entities.bricks.ExplodingBrick;
import entities.bricks.NormalBrick;
import entities.bricks.UnbreakableBrick;
import engine.Collision;
import core.World;
import org.junit.jupiter.api.BeforeAll;
import systems.ScoringSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HandleBallBrickTest {
       @Test
    public void testHandleCollisionWithNormalBrick() {
        Ball ball = new Ball(105, 105, 10, 5);
        Brick brick = new NormalBrick(100, 100, 50, 20);
        List<Brick> bricks = List.of(brick);
        World world = new World();

        Collision.handleBallBrickCollision(ball, bricks, world);

        assertEquals(1, world.getScoring().getScore(), "Không cộng điểm sau va chạm với gạch thường");
        assertEquals(1, world.getScoring().getBricksDestroyed(), "Không tăng số gạch bị phá");
    }
    @Test
    public void testHandleCollisionWithUnbreakableBrick() {
        Ball ball = new Ball(105, 105, 10, 5);
        UnbreakableBrick brick = new UnbreakableBrick(100, 100, 50, 20);
        List<Brick> bricks = List.of(brick);
        World world = new World();
        Collision.handleBallBrickCollision(ball, bricks, world);

        assertEquals(0,world.getScoring().getScore(), "Lỗi cộng điểm với gạch không vỡ");
        assertEquals(0, world.getScoring().getBricksDestroyed(), "Lỗi nên tăng số gạch bị phá");
        assertFalse(brick.isDestroyed(), "Lỗi gạch không vỡ bị đánh dấu là hit");
    }
    @Test
    public void testExplodingBrickOnlyDestroysNearby() {
           //mot gach o gan no, o xa ko no
        Ball ball = new Ball(105, 105, 10, 5);
        ExplodingBrick explodingBrick = new ExplodingBrick(100, 100, 50, 20); // gốc
        Brick nearbyBrick = new NormalBrick(150, 100, 50, 20); // dx = 50 → bị phá
        Brick farBrick = new NormalBrick(170, 100, 50, 20);    // dx = 70 → không bị phá

        List<Brick> bricks = List.of(explodingBrick, nearbyBrick, farBrick);
        World world = new World();

        Collision.handleBallBrickCollision(ball, bricks, world);

        assertTrue(explodingBrick.isDestroyed(), "Gạch nổ không bị phá");
        assertTrue(nearbyBrick.isDestroyed(), "Gạch gần không bị phá bởi nổ lan");
        assertFalse(farBrick.isDestroyed(), "Gạch xa bị phá sai");
        assertEquals(1, world.getScoring().getBricksDestroyed(), "Không cộng đúng số gạch bị phá");
    }


}