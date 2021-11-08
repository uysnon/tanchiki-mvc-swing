package ru.rseu.gorkin.model.game.map;

import ru.rseu.gorkin.model.Coordinate;
import ru.rseu.gorkin.model.game.map.collision.CollisionChecker;
import ru.rseu.gorkin.model.game.objects.GameObject;
import ru.rseu.gorkin.model.game.objects.GameObjectTypes;
import ru.rseu.gorkin.model.game.objects.immovable.Box;
import ru.rseu.gorkin.model.game.objects.moving.*;
import ru.rseu.gorkin.multithreading.Monitors;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameMap implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int SCORE_POINTS_OF_BOT_TANK_BULLET_COLLISION = 5;
    public static final int SCORE_POINTS_OF_BOX_BULLET_COLLISION = 1;

    private Map<Coordinate<Integer>, List<GameObject>> gameMatrix;
    private int size;
    private CollisionChecker collisionChecker;
    private transient ScoreActionsManager scoreActionsManager;

    public GameMap(int size) {
        this.size = size;
        gameMatrix = new HashMap<>();
        collisionChecker = new CollisionChecker();
    }

    public Map<Coordinate<Integer>, List<GameObject>> getGameMatrix() {
        return gameMatrix;
    }

    public void setGameMatrix(Map<Coordinate<Integer>, List<GameObject>> gameMatrix) {
        this.gameMatrix = gameMatrix;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ScoreActionsManager getScoreActionsManager() {
        return scoreActionsManager;
    }

    public void setScoreActionsManager(ScoreActionsManager scoreActionsManager) {
        this.scoreActionsManager = scoreActionsManager;
    }

    public void addObject(GameObject gameObject) {
        List<Coordinate<Integer>> gameObjectsCells = getCellFromDoubleCoordinate(gameObject.getCoordinate(), gameObject.getSize());
        for (Coordinate<Integer> cell : gameObjectsCells) {
            addToCell(cell, gameObject);
        }
    }

    public ActionAvailableCheckResults tryToMove(Coordinate<Double> desiredCoordinate, GameObject gameObject) {
        synchronized (Monitors.GAME_PROCESS_MONITOR) {
            ActionAvailableCheckResults result = null;
            switch (gameObject.getType()) {
                case PLAYER_TANK:
                    result = tryToMovePlayerTank(desiredCoordinate, gameObject);
                    break;
                case BULLET:
                    result = tryToMoveBullet(desiredCoordinate, (Bullet) gameObject);
                    break;
                case BOT_TANK:
                    result = tryToMoveBotTank(desiredCoordinate, (BotTank) gameObject);
                    break;
            }
            return result;
        }
    }

    public Coordinate<Integer> findFreeCell() {
        synchronized (Monitors.GAME_PROCESS_MONITOR) {
            List<Coordinate<Integer>> cellsInRandomOrder = new ArrayList<>(gameMatrix.keySet());
            Collections.shuffle(cellsInRandomOrder);
            for (Coordinate<Integer> cell : cellsInRandomOrder) {
                List<GameObject> gameObjects = gameMatrix.computeIfAbsent(cell, c -> new ArrayList<>());
                int countObjectsAtCell = (int) gameObjects.stream().filter(go -> go.getType() != GameObjectTypes.BLANK_FIELD).count();
                if (countObjectsAtCell == 0) {
                    return cell;
                }
            }
            return null;
        }
    }

    public void addToCell(Coordinate<Integer> coordinate, GameObject gameObject) {
        List<GameObject> gameObjects = gameMatrix.computeIfAbsent(coordinate, k -> new ArrayList<>());
        gameObjects.add(gameObject);
    }

    public void deleteFromCell(Coordinate<Integer> coordinate, GameObject gameObject) {
        List<GameObject> gameObjects = gameMatrix.get(coordinate);
        gameObjects.remove(gameObject);
    }

    public void deleteObject(GameObject gameObject) {
        for (Coordinate<Integer> coordinate : gameMatrix.keySet()) {
            gameMatrix.get(coordinate).removeIf(innerGameObject -> {
                        boolean result = innerGameObject.getId() == gameObject.getId();
                        if (innerGameObject instanceof MovingGameObject) {
                            if (!((MovingGameObject) innerGameObject).isAlive()) {
                                result = true;
                            }
                        }
                        return result;
                    }
            );
        }

//        List<Coordinate<Integer>> cells = getCellFromDoubleCoordinate(gameObject.getCoordinate(), gameObject.getSize());
//        for (Coordinate<Integer> cell : cells) {
//            deleteFromCell(cell, gameObject);
//        }

    }

    private ActionAvailableCheckResults tryToMovePlayerTank(Coordinate<Double> desiredCoordinate, GameObject gameObject) {
        List<Coordinate<Integer>> spaceRequirementCells
                = getCellFromDoubleCoordinate(desiredCoordinate, gameObject.getSize());
        ActionAvailableCheckResults result = ActionAvailableCheckResults.CELL_FREE;
        GameObject objectToRemove = null;
        outerLoop:
        for (Coordinate<Integer> cell : spaceRequirementCells) {
            for (GameObject standingOnCellObject : gameMatrix.computeIfAbsent(cell, t -> new ArrayList<>())) {
                if (standingOnCellObject.getType() == GameObjectTypes.BOX ||
                        standingOnCellObject.getType() == GameObjectTypes.BOT_TANK ||
                        standingOnCellObject.getType() == GameObjectTypes.WALL) {
                    if (collisionChecker.isCollisionHappened(gameObject, desiredCoordinate, standingOnCellObject)) {
                        result = ActionAvailableCheckResults.CELL_OCCUPIED;
                        break outerLoop;
                    }
                }
                if (standingOnCellObject.getType() == GameObjectTypes.BULLET) {
                    if ((!((Bullet) standingOnCellObject).isPlayer()) &&
                            (collisionChecker.isCollisionHappened(gameObject, desiredCoordinate, standingOnCellObject))) {
                        standingOnCellObject.removeGameObject();
                        setOldCellBlankAndReplaceObjectToNewCells(gameObject, spaceRequirementCells);
                        result = ActionAvailableCheckResults.BULLET_COLLISION;
                        objectToRemove = standingOnCellObject;
                        break outerLoop;
                    }
                }
            }
        }
        if (result == ActionAvailableCheckResults.CELL_FREE) {
            setOldCellBlankAndReplaceObjectToNewCells(gameObject, spaceRequirementCells);
        }
        if (objectToRemove != null) {
            objectToRemove.removeGameObject();
        }
        return result;
    }

    private ActionAvailableCheckResults tryToMoveBotTank(Coordinate<Double> desiredCoordinate, BotTank botTank) {
        List<Coordinate<Integer>> spaceRequirementCells
                = getCellFromDoubleCoordinate(desiredCoordinate, botTank.getSize());
        ActionAvailableCheckResults result = ActionAvailableCheckResults.CELL_FREE;
        GameObject objectToRemove = null;
        outerLoop:
        for (Coordinate<Integer> cell : spaceRequirementCells) {
            for (GameObject standingOnCellObject : gameMatrix.computeIfAbsent(cell, t -> new ArrayList<>())) {
                if (collisionChecker.isCollisionHappened(botTank, desiredCoordinate, standingOnCellObject)) {
                    switch (standingOnCellObject.getType()) {
                        case BOX:
                        case WALL:
                        case PLAYER_TANK:
                            result = ActionAvailableCheckResults.CELL_OCCUPIED;
                            break outerLoop;
                        case BULLET:
                            if (((Bullet) standingOnCellObject).isPlayer()) {
                                objectToRemove = standingOnCellObject;
                                scoreActionsManager.addPoints(SCORE_POINTS_OF_BOT_TANK_BULLET_COLLISION);
                                result = ActionAvailableCheckResults.BULLET_COLLISION;
                                break outerLoop;
                            }
                            break;
                    }
                }
            }
        }
        if (result == ActionAvailableCheckResults.CELL_FREE) {
            setOldCellBlankAndReplaceObjectToNewCells(botTank, spaceRequirementCells);
        }
        if (objectToRemove != null) {
            objectToRemove.removeGameObject();
        }
        return result;
    }


    private ActionAvailableCheckResults tryToMoveBullet(Coordinate<Double> desiredCoordinate, Bullet bullet) {
        List<Coordinate<Integer>> spaceRequirementCells
                = getCellFromDoubleCoordinate(desiredCoordinate, bullet.getSize());
        for (Coordinate<Integer> cell : spaceRequirementCells) {
            for (GameObject standingOnCellObject : gameMatrix.computeIfAbsent(cell, t -> new ArrayList<>())) {
                if (collisionChecker.isCollisionHappened(bullet, desiredCoordinate, standingOnCellObject)) {
                    switch (standingOnCellObject.getType()) {
                        case BOX:
                            ((Box) standingOnCellObject).reduceHealthPoints();
                            if (bullet.isPlayer()) {
                                scoreActionsManager.addPoints(SCORE_POINTS_OF_BOX_BULLET_COLLISION);
                            }
                            return ActionAvailableCheckResults.BULLET_COLLISION;
                        case BOT_TANK:
                            if (bullet.isPlayer()) {
                                ((BotTank) standingOnCellObject).reduceHealthPoints();
                                scoreActionsManager.addPoints(SCORE_POINTS_OF_BOT_TANK_BULLET_COLLISION);
                                return ActionAvailableCheckResults.BULLET_COLLISION;
                            }
                            break;
                        case PLAYER_TANK:
                            if (!bullet.isPlayer()) {
                                ((PlayerTank) standingOnCellObject).reduceHealthPoints();
                                return ActionAvailableCheckResults.BULLET_COLLISION;
                            }
                            break;

                        case WALL:
                            return ActionAvailableCheckResults.CELL_OCCUPIED;
                    }
                }
            }
        }
        setOldCellBlankAndReplaceObjectToNewCells(bullet, spaceRequirementCells);
        return ActionAvailableCheckResults.CELL_FREE;
    }

    private List<Coordinate<Integer>> getCellFromDoubleCoordinate(Coordinate<Double> coordinate, double size) {
        double halfSize = size * 0.5;
        Coordinate<Integer> coordinateLeftBottom = new Coordinate<>(
                (int) Math.round(coordinate.getX() - halfSize),
                (int) Math.round(coordinate.getY() - halfSize)
        );
        Coordinate<Integer> coordinateLeftTop = new Coordinate<>(
                (int) Math.round(coordinate.getX() - halfSize),
                (int) Math.round(coordinate.getY() + halfSize)
        );
        Coordinate<Integer> coordinateRightBottom = new Coordinate<>(
                (int) Math.round(coordinate.getX() + halfSize),
                (int) Math.round(coordinate.getY() - halfSize)
        );
        Coordinate<Integer> coordinateRightTop = new Coordinate<>(
                (int) Math.round(coordinate.getX() + halfSize),
                (int) Math.round(coordinate.getY() + halfSize)
        );
        return Stream.of(
                coordinateLeftBottom,
                coordinateLeftTop,
                coordinateRightBottom,
                coordinateRightTop)
                .distinct()
                .filter(c -> c.getX() >= 0)
                .filter(c -> c.getY() >= 0)
                .collect(Collectors.toList());
    }

    private void setOldCellBlankAndReplaceObjectToNewCells(GameObject
                                                                   gameObject, List<Coordinate<Integer>> newCells) {
        for (Coordinate<Integer> cell : getCellFromDoubleCoordinate(gameObject.getCoordinate(), gameObject.getSize())) {
            deleteFromCell(cell, gameObject);
        }

        for (Coordinate<Integer> cell : newCells) {
            addToCell(cell, gameObject);
        }
    }
}
