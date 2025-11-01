package engine;

import core.Config;
import entities.powerups.BonusCoin;
import entities.powerups.EnlargePaddle;
import entities.powerups.ExtraLife;
import entities.powerups.PowerUp;

import java.util.ArrayList;
import java.util.List;

public class PowerUpPool {
    private final List<PowerUp> available = new ArrayList<>();
    private final List<PowerUp> inUse = new ArrayList<>();

    public PowerUp acquire(Class<? extends PowerUp> type, double x, double y) {
        for (PowerUp pu : available) {
            if (pu.getClass() == type) {
                pu.reset(x, y);
                available.remove(pu);
                inUse.add(pu);
                return pu;
            }
        }

        PowerUp newPU = createNew(type, x, y);
        inUse.add(newPU);
        return newPU;
    }

    public void release(PowerUp pu) {
        pu.deactivate();
        inUse.remove(pu);
        available.add(pu);
    }

    private PowerUp createNew(Class<? extends PowerUp> type, double x, double y) {
        if (type == BonusCoin.class) {
            return new BonusCoin(x, y, Config.BONUSCOIN_RADIUS, Config.BONUSCOIN_SPEED);
        }
        if (type == ExtraLife.class) {
            return new ExtraLife(x, y);
        }
        if (type == EnlargePaddle.class) {
            return new EnlargePaddle(x, y);
        }
        throw new IllegalArgumentException("Unknown PowerUp type: " + type.getSimpleName());
    }
}