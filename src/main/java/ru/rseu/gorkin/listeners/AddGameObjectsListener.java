package ru.rseu.gorkin.listeners;

import ru.rseu.gorkin.model.game.objects.GameObject;
import ru.rseu.gorkin.model.game.objects.GameObjectTypes;

public interface AddGameObjectsListener {
    void objectAdded(GameObject gameObject);
}
