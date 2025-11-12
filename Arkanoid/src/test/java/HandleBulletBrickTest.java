import core.World;
import engine.Collision;
import entities.Ball;
import entities.Bullet;
import entities.bricks.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HandleBulletBrickTest {
    @Test
    public void testBullet_NormalBrick() {
        Bullet bullet = new Bullet(105, 105);
        Brick brick = new NormalBrick(100, 100, 50, 20);
        List<Bullet> bullets = new ArrayList<>();
        bullets.add(bullet);
        List<Brick> bricks = new ArrayList<>();
        bricks.add(brick);
        World world = new World();

        Collision.handleBulletBrickCollision(bullets, bricks, world);

        assertEquals(1, world.getScoring().getScore(), "Lỗi: Không cộng điểm sau va chạm với gạch thường");
        assertEquals(1, world.getScoring().getBricksDestroyed(), "Lỗi Không tăng số gạch bị phá");
        assertFalse(bullet.isActive(), "Lỗi: Đạn không bị vô hiệu sau va chạm");
        assertTrue(brick.isDestroyed(), "Lỗi:Gạch thường không bị đánh dấu là phá");
    }

    @Test
    public void testBullet_UnbreakableBrick() {
        Bullet bullet = new Bullet(105, 105);
        UnbreakableBrick brick = new UnbreakableBrick(100, 100, 50, 20);
        List<Bullet> bullets = new ArrayList<>();
        bullets.add(bullet);
        List<Brick> bricks = new ArrayList<>();
        bricks.add(brick);
        World world = new World();

        Collision.handleBulletBrickCollision(bullets, bricks, world);

        assertEquals(0, world.getScoring().getScore(), "Lỗi cộng điểm với gạch không vỡ");
        assertEquals(0, world.getScoring().getBricksDestroyed(), "Lỗi nên tăng số gạch bị phá");
        assertFalse(brick.isDestroyed(), "Lỗi gạch không vỡ bị đánh dấu là hit");
        assertFalse(bullet.isActive(), "Đạn không bị vô hiệu sau va chạm với gạch không vỡ");
    }

    @Test
    public void testBullet_ExplodingBrick() {
        // Một viên đạn va chạm với gạch nổ, gạch gần bị phá, gạch xa không bị phá
        Bullet bullet = new Bullet(105, 105);
        ExplodingBrick explodingBrick = new ExplodingBrick(100, 100, 50, 20); // gạch nổ gốc
        Brick nearbyBrick = new NormalBrick(150, 100, 50, 20); // cách 50 → bị phá
        Brick farBrick = new NormalBrick(170, 100, 50, 20);    // cách 70 → không bị phá

        List<Bullet> bullets = new ArrayList<>();
        bullets.add(bullet);
        List<Brick> bricks = new ArrayList<>();
        bricks.add(explodingBrick);
        bricks.add(nearbyBrick);
        bricks.add(farBrick);
        World world = new World();

        Collision.handleBulletBrickCollision(bullets, bricks, world);

        assertFalse(bullet.isActive(), "Lỗi:Đạn không bị vô hiệu sau va chạm");
        assertTrue(explodingBrick.isDestroyed(), "Lỗi:Gạch nổ không bị phá");
        assertTrue(nearbyBrick.isDestroyed(), "Lỗi: Gạch gần không bị phá bởi nổ lan");
        assertFalse(farBrick.isDestroyed(), "Lỗi: Gạch xa bị phá sai");
        assertEquals(1, world.getScoring().getBricksDestroyed(), "Lỗi:Không cộng đúng số gạch bị phá");
        assertEquals(1, world.getScoring().getScore(), "Lỗi: Không cộng đúng điểm");
    }

    // gach hard- ban 3 lan
    @Test
    public void testBullet_HardBrick1() {
        Bullet bullet1 = new Bullet(105, 105);
        Bullet bullet2 = new Bullet(105, 105);
        Bullet bullet3 = new Bullet(105, 105);
        HardBrick brick = new HardBrick(100, 100, 50, 20);
        List<Bullet> bullets = new ArrayList<>();
        bullets.add(bullet1);
        bullets.add(bullet2);
        bullets.add(bullet3);
        List<Brick> bricks = new ArrayList<>();
        bricks.add(brick);
        World world = new World();
        for (Bullet bullet : bullets) {
            Collision.handleBulletBrickCollision(bullets, bricks, world);
            assertFalse(bullet.isActive(), "Đạn không bị vô hiệu sau va chạm với gạch");
        }
        assertEquals(1, world.getScoring().getScore(), "Lỗi ko cộng điểm ");
        assertEquals(1, world.getScoring().getBricksDestroyed(), "Lỗi không tăng số gạch bị phá");
        assertTrue(brick.isDestroyed(), "Lỗi gạch không vỡ ");
    }
    // Bắn 2 lần, gạch chưa vỡ
    @Test
    public void testBullet_HardBrick2() {
        Bullet bullet1 = new Bullet(105, 105);
        Bullet bullet2 = new Bullet(105, 105);
        HardBrick brick = new HardBrick(100, 100, 50, 20);
        List<Bullet> bullets = new ArrayList<>();
        bullets.add(bullet1);
        bullets.add(bullet2);
        List<Brick> bricks = new ArrayList<>();
        bricks.add(brick);
        World world = new World();
        for (Bullet bullet : bullets) {
            Collision.handleBulletBrickCollision(bullets, bricks, world);
            assertFalse(bullet.isActive(), "Đạn không bị vô hiệu sau va chạm với gạch");
        }
        assertEquals(0, world.getScoring().getScore(), "Lỗi cộng điểm ");
        assertEquals(0, world.getScoring().getBricksDestroyed(), "Lỗi tăng số gạch bị phá");
        assertFalse(brick.isDestroyed(), "Lỗi gạch vỡ ");
    }
    // Bắn 1 lần, gạch chưa vỡ
    @Test
    public void testBullet_HardBrick3() {
        Bullet bullet1 = new Bullet(105, 105);
        HardBrick brick = new HardBrick(100, 100, 50, 20);
        List<Bullet> bullets = new ArrayList<>();
        bullets.add(bullet1);
        List<Brick> bricks = new ArrayList<>();
        bricks.add(brick);
        World world = new World();
        for (Bullet bullet : bullets) {
            Collision.handleBulletBrickCollision(bullets, bricks, world);
            assertFalse(bullet.isActive(), "Đạn không bị vô hiệu sau va chạm với gạch");
        }
        assertEquals(0, world.getScoring().getScore(), "Lỗi cộng điểm ");
        assertEquals(0, world.getScoring().getBricksDestroyed(), "Lỗi tăng số gạch bị phá");
        assertFalse(brick.isDestroyed(), "Lỗi gạch vỡ ");
    }
}