package ru.rseu.gorkin.model.game.objects.immovable;

import ru.rseu.gorkin.model.game.objects.GameObject;
import ru.rseu.gorkin.model.game.objects.GameObjectTypes;

import java.util.Objects;

public class Box extends GameObject {
    public static final double SIZE = 1;
    public static final int HEALTH_POINTS = 2;

    private int healthPointsCount;

    public Box() {
        setSize(SIZE);
        setType(GameObjectTypes.BOX);
        this.healthPointsCount = HEALTH_POINTS;
    }

    public int getHealthPointsCount() {
        return healthPointsCount;
    }

    public void setHealthPointsCount(int healthPointsCount) {
        this.healthPointsCount = healthPointsCount;
    }

    public void reduceHealthPoints() {
        healthPointsCount--;
        if (healthPointsCount <= 0){
            removeGameObject();
        }
        notifyListenersStateChanged();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Box box = (Box) o;
        return healthPointsCount == box.healthPointsCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), healthPointsCount);
    }

    @Override
    public String toString() {
        return "Box{" +
                "healthPointsCount=" + healthPointsCount +
                '}';
    }
}
