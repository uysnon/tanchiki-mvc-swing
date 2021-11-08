package ru.rseu.gorkin.controller;

import ru.rseu.gorkin.configuration.AppConfiguration;
import ru.rseu.gorkin.file.FileUtils;
import ru.rseu.gorkin.listeners.AddGameObjectsListener;
import ru.rseu.gorkin.listeners.RemoveGameObjectsListener;
import ru.rseu.gorkin.listeners.StateChangeListener;
import ru.rseu.gorkin.model.Coordinate;
import ru.rseu.gorkin.model.game.Game;
import ru.rseu.gorkin.model.game.objects.GameObject;
import ru.rseu.gorkin.model.game.objects.GameObjectTypes;
import ru.rseu.gorkin.model.game.objects.moving.*;
import ru.rseu.gorkin.multithreading.Monitors;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GameProcessController implements
        MovableObjectsOperable,
        RemoveGameObjectsListener,
        AddGameObjectsListener {
    private static final double GAP_TO_CENTER_CELL_FROM_CORNER = 0.5;
    private Game game;
    private List<AddGameObjectsListener> addGameObjectsListeners;

    public GameProcessController(Game game) {
        addGameObjectsListeners = new ArrayList<>();
        this.game = game;
        addListenersAndManagersToGame(game);
        setMovableObjectsAlive(game);
    }


    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }


    public void pauseGame() {
        synchronized (Monitors.GAME_PROCESS_MONITOR) {
            game.pause();
            try {
                String backupFilePath = AppConfiguration.INSTANCE.getPathToResourcesFolder() +
                        AppConfiguration.INSTANCE.getProperty("src.backupGameFile");
                if (FileUtils.isBackupFileExists()) {
                    Files.delete(Paths.get(backupFilePath));
                }
                FileOutputStream fileOutputStream
                        = null;
                fileOutputStream = new FileOutputStream(
                        backupFilePath
                );

                ObjectOutputStream objectOutputStream
                        = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(game);
                objectOutputStream.flush();
                objectOutputStream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void endGame() {
        synchronized (Monitors.GAME_PROCESS_MONITOR) {
            game.pause();
            try {
                String backupFilePath = AppConfiguration.INSTANCE.getPathToResourcesFolder() +
                        AppConfiguration.INSTANCE.getProperty("src.backupGameFile");
                if (FileUtils.isBackupFileExists()) {
                    Files.delete(Paths.get(backupFilePath));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void subscribeOnMapDataChange(StateChangeListener stateChangeListener) {
        game.addListener(stateChangeListener);
    }

    public void subscribeOnNewObjectOnMapCreated(AddGameObjectsListener listener) {
        addGameObjectsListeners.add(listener);
    }

    public void subscribeOnMapDataDeleted(RemoveGameObjectsListener listener) {
        game.addRemoveObjectListener(listener);
    }

    public void start() {
        game.start();
    }


    public void changePlayerTankDirection(Directions direction) {
        game.getPlayerTank().setDirection(direction);
    }

    public void setDefaultSpeedForPlayerTank() {
        game.getPlayerTank().setDefaultSpeed();
    }

    public void stopPlayerTank() {
        game.getPlayerTank().stop();
    }

    @Override
    public ActionAvailableCheckResults isActionAvailable(Coordinate<Double> coordinate, GameObject gameObject) {
        return game.getGameMap().tryToMove(coordinate, gameObject);
    }


    @Override
    public void objectRemoved(GameObject object) {
        switch (object.getType()) {
            case BOT_TANK:
                Coordinate<Integer> cell = game.getGameMap().findFreeCell();
                BotTank botTank = (BotTank) GameObjectTypes.BOT_TANK.createObject(new Coordinate<>(
                        cell.getX() + GAP_TO_CENTER_CELL_FROM_CORNER,
                        cell.getY() + GAP_TO_CENTER_CELL_FROM_CORNER
                ));
                botTank.setMovingOperator(this);
                botTank.addAddGameObjectListener(this);
                game.startBotTank(botTank);
                notifyAddGameObjectListeners(botTank);
                break;
        }
    }

    public void subscribeOnRemoveGameObjectsFromMap(RemoveGameObjectsListener listener) {

    }

    public void makePlayerShot() {
        synchronized (Monitors.GAME_PROCESS_MONITOR) {
            Bullet bullet = new Bullet();
            PlayerTank playerTank = game.getPlayerTank();
            bullet.setCoordinate(playerTank.calculateGunTailCoordinate());
            bullet.setPlayer(true);
            bullet.setDirection(playerTank.getDirection());
            bullet.setMovingOperator(this);
            bullet.addStateChangeListener(game);
            bullet.addRemovedObjectListener(game);
            game.startBullet(bullet);
            notifyAddGameObjectListeners(bullet);
        }
    }

    @Override
    public void objectAdded(GameObject gameObject) {
        switch (gameObject.getType()) {
            case BULLET:
                Bullet bullet = (Bullet) gameObject;
                bullet.setMovingOperator(this);
                bullet.addStateChangeListener(game);
                bullet.addRemovedObjectListener(game);
                game.startBullet(bullet);
                notifyAddGameObjectListeners(bullet);
                break;
        }
    }

    private void notifyAddGameObjectListeners(GameObject gameObject) {
        for (AddGameObjectsListener listener : addGameObjectsListeners) {
            listener.objectAdded(gameObject);
        }
    }

    private void addListenersAndManagersToGame(Game game) {
        game.getBotTanks().forEach(tank -> {
            tank.setMovingOperator(this);
            tank.addAddGameObjectListener(this);
            tank.addStateChangeListener(game);
            tank.addRemovedObjectListener(game);
        });
        game.getGameMap().setScoreActionsManager(game);
        game.getPlayerTank().setMovingOperator(this);
        game.getPlayerTank().addStateChangeListener(game);
        game.getPlayerTank().addRemovedObjectListener(game);
        game.getWalls().forEach(wall -> {
            wall.addStateChangeListener(game);
            wall.addRemovedObjectListener(game);
        });

        game.getBoxes().forEach(box -> {
            box.addStateChangeListener(game);
            box.addRemovedObjectListener(game);
        });

        game.getBullets().forEach(bullet -> {
            bullet.addStateChangeListener(game);
            bullet.setMovingOperator(this);
            bullet.addRemovedObjectListener(game);
        });
        game.addRemoveObjectListener(this);
    }

    private void setMovableObjectsAlive(Game game) {
        game.getBotTanks().forEach(tank -> {
            tank.setAlive(true);
        });
        game.getPlayerTank().setAlive(true);
        game.getBullets().forEach(bullet -> bullet.setAlive(true));
    }


}

