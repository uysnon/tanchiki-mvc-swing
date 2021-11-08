package ru.rseu.gorkin.model.game.objects.immovable;

import ru.rseu.gorkin.model.game.objects.GameObject;
import ru.rseu.gorkin.model.game.objects.GameObjectTypes;

public class Wall extends GameObject {
    public static final double SIZE = 1;

    public Wall() {
        setSize(SIZE);
        setType(GameObjectTypes.WALL);
    }


}
