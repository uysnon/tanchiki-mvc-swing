package ru.rseu.gorkin.model.game.objects.moving;

import ru.rseu.gorkin.model.Coordinate;
import ru.rseu.gorkin.model.game.objects.GameObjectTypes;

import java.util.Objects;

public class Bullet extends MovingGameObject {
    private static final double SIZE = 0.4;
    private static final double SPEED = 0.015;

    private boolean isPlayer;

    public Bullet() {
        setSpeed(SPEED);
        setSize(SIZE);
        setType(GameObjectTypes.BULLET);
    }

    public boolean isPlayer() {
        return isPlayer;
    }

    public void setPlayer(boolean player) {
        isPlayer = player;
    }

    @Override
    public void run() {
        while (isAlive()) {
            try {
                Thread.sleep(RENDER_TIME_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Coordinate<Double> newCoordinate = getNextCoordinate(RENDER_TIME_MS);
            ActionAvailableCheckResults checkResult = getMovingOperator().isActionAvailable(newCoordinate, this);
            if (checkResult == ActionAvailableCheckResults.CELL_FREE) {
                setCoordinate(newCoordinate);
                notifyListenersStateChanged();
            } else {
                removeGameObject();
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Bullet bullet = (Bullet) o;
        return isPlayer == bullet.isPlayer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isPlayer);
    }

    @Override
    public String toString() {
        return "Bullet{" +
                "isPlayer=" + isPlayer +
                '}';
    }
}

