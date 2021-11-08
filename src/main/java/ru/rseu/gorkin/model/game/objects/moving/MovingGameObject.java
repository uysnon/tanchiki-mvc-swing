package ru.rseu.gorkin.model.game.objects.moving;

import ru.rseu.gorkin.listeners.RemoveGameObjectsListener;
import ru.rseu.gorkin.model.Coordinate;
import ru.rseu.gorkin.model.game.objects.GameObject;

import java.util.List;
import java.util.Objects;

public class MovingGameObject extends GameObject implements Runnable, StateChangeable {
    public static final double BASE_SPEED = 0.007;
    private Directions direction;
    private transient boolean isAlive = true;
    private transient MovableObjectsOperable movingOperator;
    /**
     * fieldPoints per ms
     */
    private double speed;

    public MovingGameObject() {
        speed = BASE_SPEED;
        setAlive(true);
    }

    public Directions getDirection() {
        return direction;
    }

    public void setDirection(Directions direction) {
        this.direction = direction;
        System.out.println("direction is changed!!");
        notifyListenersStateChanged();
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public MovableObjectsOperable getMovingOperator() {
        return movingOperator;
    }

    public void setMovingOperator(MovableObjectsOperable movingOperator) {
        this.movingOperator = movingOperator;
    }

    @Override
    public void removeGameObject() {
        setAlive(false);
        super.removeGameObject();
        notifyListenersStateChanged();
    }

    public void stop() {
        speed = 0;
        notifyListenersStateChanged();
    }

    public void setDefaultSpeed() {
        speed = BASE_SPEED;
        notifyListenersStateChanged();
    }

    @Override
    public void run() {
        while (isAlive) {
            try {
                Thread.sleep(RENDER_TIME_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            changeLocationWithTime(RENDER_TIME_MS);
        }
    }

    protected Coordinate<Double> getNextCoordinate(int timeMs) {
        return direction.increment(getCoordinate(), speed * timeMs);
    }

    protected void changeLocationWithTime(int timeMs) {
        setCoordinate(getNextCoordinate(timeMs));
        notifyListenersStateChanged();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MovingGameObject that = (MovingGameObject) o;
        return isAlive == that.isAlive && Double.compare(that.speed, speed) == 0 && direction == that.direction && Objects.equals(movingOperator, that.movingOperator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), direction, isAlive, movingOperator, speed);
    }

    @Override
    public String toString() {
        return "MovingGameObject{" +
                "direction=" + direction +
                ", isAlive=" + isAlive +
                ", movingOperator=" + movingOperator +
                ", speed=" + speed +
                '}';
    }
}
