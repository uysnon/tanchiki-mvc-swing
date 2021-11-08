package ru.rseu.gorkin.model.game.objects.moving;

import ru.rseu.gorkin.listeners.AddGameObjectsListener;
import ru.rseu.gorkin.model.Coordinate;
import ru.rseu.gorkin.model.game.objects.GameObjectTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BotTank extends Tank {
    private final static int HEALTH_COUNT = 1;

    private int MIN_COUNT_MS_TO_CHANGE_DIRECTION = 500;
    private int MAX_COUNT_MS_TO_CHANGE_DIRECTION = 2_000;

    private int MIN_COUNT_MS_TO_MAKE_SHOT = 200;
    private int MAX_COUNT_MS_TO_MAKE_SHOT = 700;


    private transient List<AddGameObjectsListener> addGameObjectsListeners;

    private int countMsSinceLastDirectionChanged;
    private int countMsSinceLastShot;
    private int countMsToChangeDirection;
    private int countMsToShot;

    public BotTank() {
        setType(GameObjectTypes.BOT_TANK);
        setHealthPointsCount(HEALTH_COUNT);
        addGameObjectsListeners = new ArrayList<>();
        countMsSinceLastDirectionChanged = 0;
        countMsSinceLastShot = 0;
        countMsToShot = calculateCountMsToShot();
        countMsToChangeDirection = calculateCountMsToChangeDirection();
        setDefaultSpeed();
    }

    @Override
    public void run() {
        while (isAlive()) {
            Coordinate<Double> newCoordinate = getNextCoordinate(RENDER_TIME_MS);
            ActionAvailableCheckResults checkResult = getMovingOperator().isActionAvailable(newCoordinate, this);
            switch (checkResult) {
                case CELL_FREE:
                    setCoordinate(newCoordinate);
                    break;
                case BULLET_COLLISION:
                    setCoordinate(newCoordinate);
                    reduceHealthPoints();
                    break;
                case CELL_OCCUPIED:
                    countMsSinceLastDirectionChanged = 0;
                    countMsToChangeDirection = calculateCountMsToChangeDirection();
                    setDirection(Directions.getRandomDirection());
                default:
                    notifyListenersStateChanged();
            }

            try {
                Thread.sleep(RENDER_TIME_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countMsSinceLastShot += RENDER_TIME_MS;
            countMsSinceLastDirectionChanged += RENDER_TIME_MS;

            if (countMsSinceLastShot >= countMsToShot) {
                countMsSinceLastShot = 0;
                countMsToShot = calculateCountMsToShot();
                makeShot();
            }

            if (countMsSinceLastDirectionChanged >= countMsToChangeDirection) {
                countMsSinceLastDirectionChanged = 0;
                countMsToChangeDirection = calculateCountMsToChangeDirection();
                setDirection(Directions.getRandomDirection());
            }
        }
    }

    public void addAddGameObjectListener(AddGameObjectsListener listener) {
        if (addGameObjectsListeners == null) {
            addGameObjectsListeners = new ArrayList<>();
        }
        addGameObjectsListeners.add(listener);
    }

    public Bullet makeShot() {
        Bullet bullet = new Bullet();
        bullet.setCoordinate(calculateGunTailCoordinate());
        bullet.setPlayer(false);
        bullet.setDirection(getDirection());
        addGameObjectsListeners.forEach(l -> l.objectAdded(bullet));
        return bullet;
    }

    private int calculateCountMsToShot() {
        return ThreadLocalRandom.current().nextInt(
                MIN_COUNT_MS_TO_MAKE_SHOT,
                MAX_COUNT_MS_TO_MAKE_SHOT + 1);
    }

    private int calculateCountMsToChangeDirection() {
        return ThreadLocalRandom.current().nextInt(
                MIN_COUNT_MS_TO_CHANGE_DIRECTION,
                MAX_COUNT_MS_TO_CHANGE_DIRECTION + 1);
    }
}
