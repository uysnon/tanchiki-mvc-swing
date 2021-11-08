package ru.rseu.gorkin.model.game.objects.moving;

import ru.rseu.gorkin.model.Coordinate;

import java.util.Objects;

public class Tank extends MovingGameObject {
    private static final double SIZE = 0.7;
    private int healthPointsCount;
    private boolean isAvailableToMove;

    public Tank() {
        setDirection(Directions.getRandomDirection());
        setSize(SIZE);
    }

    public int getHealthPointsCount() {
        return healthPointsCount;
    }

    public void setHealthPointsCount(int healthPointsCount) {
        this.healthPointsCount = healthPointsCount;
    }

    public boolean isAvailableToMove() {
        return isAvailableToMove;
    }

    public void setAvailableToMove(boolean availableToMove) {
        isAvailableToMove = availableToMove;
    }

    public Coordinate<Double> calculateGunTailCoordinate() {
        return getDirection().increment(getCoordinate(), getSize() / 2);
    }

    @Override
    public void run() {
        while (isAlive()) {
            Coordinate<Double> newCoordinate = getNextCoordinate(RENDER_TIME_MS);
            ActionAvailableCheckResults checkResult = getMovingOperator().isActionAvailable(newCoordinate, this);
            if (checkResult == ActionAvailableCheckResults.CELL_FREE
                    || checkResult == ActionAvailableCheckResults.BULLET_COLLISION
            ) {
                setCoordinate(newCoordinate);
                if (checkResult == ActionAvailableCheckResults.BULLET_COLLISION) {
                    reduceHealthPoints();
                }
                notifyListenersStateChanged();
            }
            try {
                Thread.sleep(RENDER_TIME_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Tank tank = (Tank) o;
        return healthPointsCount == tank.healthPointsCount && isAvailableToMove == tank.isAvailableToMove;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), healthPointsCount, isAvailableToMove);
    }

    @Override
    public String toString() {
        return "Tank{" +
                "healthPointsCount=" + healthPointsCount +
                ", isAvailableToMove=" + isAvailableToMove +
                '}';
    }

     public void reduceHealthPoints() {
        this.healthPointsCount--;
        if (healthPointsCount <= 0) {
            removeGameObject();
        }
        notifyListenersStateChanged();
    }

}
