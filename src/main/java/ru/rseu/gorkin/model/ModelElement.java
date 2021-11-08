package ru.rseu.gorkin.model;

import ru.rseu.gorkin.listeners.StateChangeListener;

import java.io.Serializable;
import java.util.*;

public class ModelElement implements Serializable {
    private static final long serialVersionUID = 1L;
    private transient Set<StateChangeListener> stateChangeListeners;

    public ModelElement() {
        stateChangeListeners = new HashSet<>();
    }

    public Set<StateChangeListener> getListeners() {
        return stateChangeListeners;
    }

    public void setListeners(Set<StateChangeListener> stateChangeListeners) {
        this.stateChangeListeners = stateChangeListeners;
    }

    public void addStateChangeListener(StateChangeListener stateChangeListener) {
        if (stateChangeListeners == null){
            stateChangeListeners = new HashSet<>();
        }
        stateChangeListeners.add(stateChangeListener);
    }

    public void notifyStateChanged() {
        for (StateChangeListener stateChangeListener : stateChangeListeners) {
            stateChangeListener.dataChanged();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelElement that = (ModelElement) o;
        return Objects.equals(stateChangeListeners, that.stateChangeListeners);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stateChangeListeners);
    }

    @Override
    public String toString() {
        return "ModelElement{" +
                "listeners=" + stateChangeListeners +
                '}';
    }
}
