package ru.rseu.gorkin.model.game.objects.moving;

import ru.rseu.gorkin.model.Coordinate;
import ru.rseu.gorkin.model.game.objects.GameObject;

public interface MovableObjectsOperable {
    ActionAvailableCheckResults isActionAvailable(
            Coordinate<Double> coordinate,
            GameObject gameObject);

}
