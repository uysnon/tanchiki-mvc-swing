package ru.rseu.gorkin.model.game.objects.moving;

import ru.rseu.gorkin.model.game.objects.GameObjectTypes;

public class PlayerTank extends Tank {
    public static final int HEALTH_POINTS = 3;

    public PlayerTank() {
        setType(GameObjectTypes.PLAYER_TANK);
        setHealthPointsCount(HEALTH_POINTS);
        stop();
    }

}

