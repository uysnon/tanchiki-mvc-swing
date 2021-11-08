package ru.rseu.gorkin.model.game.objects;

import ru.rseu.gorkin.listeners.RemoveGameObjectsListener;
import ru.rseu.gorkin.model.Coordinate;
import ru.rseu.gorkin.listeners.StateChangeListener;
import ru.rseu.gorkin.model.ModelElement;
import ru.rseu.gorkin.multithreading.Monitors;

import java.util.*;

public class GameObject extends ModelElement {
    public static final double BASE_SIZE = 1;

    private UUID id;
    private double size;
    private GameObjectTypes type;
    private Coordinate<Double> coordinate;
    private transient Set<RemoveGameObjectsListener> removedObjectListeners;

    public GameObject() {
        id = UUID.randomUUID();
        removedObjectListeners = new HashSet<>();
        setSize(BASE_SIZE);
    }

    public GameObjectTypes getType() {
        return type;
    }

    protected void setType(GameObjectTypes type) {
        this.type = type;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public Coordinate<Double> getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate<Double> coordinate) {
        this.coordinate = coordinate;
    }

    public Set<RemoveGameObjectsListener> getRemovedObjectListeners() {
        return removedObjectListeners;
    }

    public void setRemovedObjectListeners(Set<RemoveGameObjectsListener> removedObjectListeners) {
        this.removedObjectListeners = removedObjectListeners;
    }

    public UUID getId() {
        return id;
    }


    public void addRemovedObjectListener(RemoveGameObjectsListener listener) {
        if (removedObjectListeners == null) {
            removedObjectListeners = new HashSet<>();
        }
        removedObjectListeners.add(listener);
    }

    public void removeGameObject() {
        synchronized (Monitors.GAME_PROCESS_MONITOR) {
            notifyListenersObjectRemoved();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameObject that = (GameObject) o;
        return Double.compare(that.size, size) == 0 && Objects.equals(id, that.id) && type == that.type && Objects.equals(coordinate, that.coordinate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, size, type, coordinate);
    }

    @Override
    public String toString() {
        return "GameObject{" +
                "id=" + id +
                ", size=" + size +
                ", type=" + type +
                ", coordinate=" + coordinate +
                '}';
    }

    protected void notifyListenersStateChanged() {
        for (StateChangeListener stateChangeListener : getListeners()) {
            stateChangeListener.dataChanged();
        }
    }

    protected void notifyListenersObjectRemoved() {
        for (RemoveGameObjectsListener listener : getRemovedObjectListeners()) {
            listener.objectRemoved(this);
        }
    }
}
