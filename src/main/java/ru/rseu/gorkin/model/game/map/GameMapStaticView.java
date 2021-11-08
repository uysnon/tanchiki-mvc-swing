package ru.rseu.gorkin.model.game.map;

import ru.rseu.gorkin.model.Coordinate;
import ru.rseu.gorkin.model.game.objects.GameObjectTypes;

import java.util.Map;
import java.util.Objects;

public class GameMapStaticView {
    private int size;
    private Map<Coordinate<Integer>, GameObjectTypes> gameMap;

    public GameMapStaticView() {
    }

    public GameMapStaticView(int size, Map<Coordinate<Integer>, GameObjectTypes> gameMap) {
        this.size = size;
        this.gameMap = gameMap;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Map<Coordinate<Integer>, GameObjectTypes> getGameMap() {
        return gameMap;
    }

    public void setGameMap(Map<Coordinate<Integer>, GameObjectTypes> gameMap) {
        this.gameMap = gameMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameMapStaticView that = (GameMapStaticView) o;
        return size == that.size && Objects.equals(gameMap, that.gameMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, gameMap);
    }

    @Override
    public String toString() {
        return "GameMapStaticView{" +
                "size=" + size +
                ", gameMap=" + gameMap +
                '}';
    }
}
