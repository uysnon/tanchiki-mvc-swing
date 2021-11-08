package ru.rseu.gorkin.listeners;

import ru.rseu.gorkin.model.game.objects.GameObject;

public interface RemoveGameObjectsListener {
    void objectRemoved(GameObject gameObject);
}
