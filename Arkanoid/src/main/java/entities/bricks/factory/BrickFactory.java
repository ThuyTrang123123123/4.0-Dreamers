package entities.bricks.factory;

import entities.bricks.*;

public class BrickFactory {
    public Brick create(char type, double x, double y, double width, double height) {
        return switch(type) {
            case 'N' -> new NormalBrick(x, y, width, height);
            case 'H' -> new HardBrick(x, y, width, height);
            case 'E' -> new ExplodingBrick(x, y, width, height);
            default -> null;
        };
    }
}