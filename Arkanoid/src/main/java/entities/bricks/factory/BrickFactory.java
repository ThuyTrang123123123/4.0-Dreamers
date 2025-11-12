package entities.bricks.factory;

import entities.bricks.*;

/**
 * Lớp BrickFactory (Factory)
 * Chịu trách nhiệm tạo ra các đối tượng Brick cụ thể (Normal, Hard, Exploding, Unbreakable)
 * dựa trên một tham số đầu vào (type).
 */

public class BrickFactory {
    public Brick create(char type, double x, double y, double width, double height) {
        return switch (type) {
            case 'N' -> new NormalBrick(x, y, width, height);
            case 'H' -> new HardBrick(x, y, width, height);
            case 'E' -> new ExplodingBrick(x, y, width, height);
            case 'U' -> new UnbreakableBrick(x, y, width, height);
            default -> null;
        };
    }
}