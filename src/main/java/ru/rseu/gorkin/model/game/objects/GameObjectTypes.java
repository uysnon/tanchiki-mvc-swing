package ru.rseu.gorkin.model.game.objects;

import ru.rseu.gorkin.model.Coordinate;
import ru.rseu.gorkin.model.game.objects.immovable.BlankField;
import ru.rseu.gorkin.model.game.objects.immovable.Box;
import ru.rseu.gorkin.model.game.objects.immovable.Wall;
import ru.rseu.gorkin.model.game.objects.moving.BotTank;
import ru.rseu.gorkin.model.game.objects.moving.Bullet;
import ru.rseu.gorkin.model.game.objects.moving.PlayerTank;

public enum GameObjectTypes {
    BLANK_FIELD('-'){
        @Override
        GameObject createObject() {
            return new BlankField();
        }
    },
    WALL('|'){
        @Override
        GameObject createObject() {
            return new Wall();
        }
    },
    BOX ('#'){
        @Override
        GameObject createObject() {
            return new Box();
        }
    },
    PLAYER_TANK ('$') {
        @Override
        GameObject createObject() {
            return new PlayerTank();
        }
    },
    BOT_TANK ('!'){
        @Override
        GameObject createObject() {
            return new BotTank();
        }
    },
    BULLET {
        @Override
        GameObject createObject() {
            return new Bullet();
        }
    };

    private static final char BLANK_SIGN = '\0';

    private char sign;

    GameObjectTypes(char sign) {
        this.sign = sign;
    }

    GameObjectTypes() {
        this.sign = BLANK_SIGN;
    }

    public static GameObjectTypes parseFromCharacter(char objectSign){
        if (objectSign == BLANK_SIGN){
            return null;
        }
        for (GameObjectTypes type: values()){
            if (type.sign == objectSign){
                return type;
            }
        }
        return null;
    }

    public GameObject createObject(Coordinate<? extends Number> coordinate){
        GameObject gameObject = createObject();
        gameObject.setCoordinate(new Coordinate<>(coordinate.getX().doubleValue(), coordinate.getY().doubleValue()));
        return gameObject;
    }

    abstract GameObject createObject();
}
