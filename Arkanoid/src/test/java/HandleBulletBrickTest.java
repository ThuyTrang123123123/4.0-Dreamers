import core.World;
import engine.Collision;
import entities.Ball;
import entities.Bullet;
import entities.bricks.Brick;
import entities.bricks.ExplodingBrick;
import entities.bricks.NormalBrick;
import entities.bricks.UnbreakableBrick;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HandleBulletBrickTest {
    @Test
    public void testBulletNormalBrick() {
        Bullet bullet = new Bullet(105, 105);
        Brick brick = new NormalBrick(100, 100, 50, 20);
        List<Bullet> bullets = List.of(bullet);
        List<Brick> bricks = List.of(brick);
        World world = new World();

        Collision.handleBulletBrickCollision(bullets, bricks, world);

        assertEquals(1, world.getScoring().getScore(), "Lỗi: Không cộng điểm sau va chạm với gạch thường");
        assertEquals(1, world.getScoring().getBricksDestroyed(), "Lỗi Không tăng số gạch bị phá");
        assertFalse(bullet.isActive(), "Lỗi: Đạn không bị vô hiệu sau va chạm");
        assertTrue(brick.isDestroyed(), "Lỗi:Gạch thường không bị đánh dấu là phá");
    }
    @Test
    public void testHandleCollisionWithUnbreakableBrick() {
        Bullet bullet = new Bullet(105, 105);
        UnbreakableBrick brick = new UnbreakableBrick(100, 100, 50, 20);
        List<Bullet> bullets = List.of(bullet);
        List<Brick> bricks = List.of(brick);
        World world = new World();

        Collision.handleBulletBrickCollision(bullets, bricks, world);

        assertEquals(0, world.getScoring().getScore(), "Lỗi cộng điểm với gạch không vỡ");
        assertEquals(0, world.getScoring().getBricksDestroyed(), "Lỗi nên tăng số gạch bị phá");
        assertFalse(brick.isDestroyed(), "Lỗi gạch không vỡ bị đánh dấu là hit");
        assertFalse(bullet.isActive(), "Đạn không bị vô hiệu sau va chạm với gạch không vỡ");
    }
    @Test
    public void testExplodingBrickOnlyDestroysNearbyWithBullet() {
        // Một viên đạn va chạm với gạch nổ, gạch gần bị phá, gạch xa không bị phá
        Bullet bullet = new Bullet(105, 105);
        ExplodingBrick explodingBrick = new ExplodingBrick(100, 100, 50, 20); // gạch nổ gốc
        Brick nearbyBrick = new NormalBrick(150, 100, 50, 20); // cách 50 → bị phá
        Brick farBrick = new NormalBrick(170, 100, 50, 20);    // cách 70 → không bị phá

        List<Bullet> bullets = List.of(bullet);
        List<Brick> bricks = List.of(explodingBrick, nearbyBrick, farBrick);
        World world = new World();

        Collision.handleBulletBrickCollision(bullets, bricks, world);

        assertFalse(bullet.isActive(), "Lỗi:Đạn không bị vô hiệu sau va chạm");
        assertTrue(explodingBrick.isDestroyed(), "Lỗi:Gạch nổ không bị phá");
        assertTrue(nearbyBrick.isDestroyed(), "Lỗi: Gạch gần không bị phá bởi nổ lan");
        assertFalse(farBrick.isDestroyed(), "Lỗi: Gạch xa bị phá sai");
        assertEquals(1, world.getScoring().getBricksDestroyed(), "Lỗi:Không cộng đúng số gạch bị phá");
        assertEquals(1, world.getScoring().getScore(), "Lỗi: Không cộng đúng điểm");
    }




}
