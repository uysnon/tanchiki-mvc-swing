package ru.rseu.gorkin.model.game.objects.immovable;

import ru.rseu.gorkin.model.game.objects.GameObject;
import ru.rseu.gorkin.model.game.objects.GameObjectTypes;

public class BlankField extends GameObject {
    public BlankField() {
        setType(GameObjectTypes.BLANK_FIELD);
    }
}
