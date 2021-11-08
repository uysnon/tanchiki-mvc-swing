package ru.rseu.gorkin.model.game;

import ru.rseu.gorkin.listeners.StateChangeListener;
import ru.rseu.gorkin.listeners.RemoveGameObjectsListener;
import ru.rseu.gorkin.model.game.map.GameMap;
import ru.rseu.gorkin.model.game.map.ScoreActionsManager;
import ru.rseu.gorkin.model.game.objects.GameObject;
import ru.rseu.gorkin.model.game.objects.immovable.Box;
import ru.rseu.gorkin.model.game.objects.immovable.Wall;
import ru.rseu.gorkin.model.game.objects.moving.BotTank;
import ru.rseu.gorkin.model.game.objects.moving.Bullet;
import ru.rseu.gorkin.model.game.objects.moving.PlayerTank;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Game implements
        StateChangeListener,
        RemoveGameObjectsListener,
        ScoreActionsManager,
        Serializable {

    private static final long serialVersionUID = 1L;

    private GameMap gameMap;
    private int score;
    private List<BotTank> botTanks;
    private PlayerTank playerTank;
    private List<Box> boxes;
    private List<Bullet> bullets;
    private List<Wall> walls;
    private transient Set<StateChangeListener> stateChangeListeners;
    private transient Set<RemoveGameObjectsListener> removeGameObjectsListeners;


    public Game(int size) {
        gameMap = new GameMap(size);
        gameMap.setScoreActionsManager(this);
        stateChangeListeners = new HashSet<>();
        removeGameObjectsListeners = new HashSet<>();
        score = 0;
        botTanks = new ArrayList<>();
        boxes = new ArrayList<>();
        bullets = new ArrayList<>();
        walls = new ArrayList<>();
    }

    public void start() {
        Thread playerTankThread = new Thread(playerTank);
        List<Thread> botTanksThreads = botTanks.stream()
                .map(Thread::new)
                .collect(Collectors.toList());
        List<Thread> bulletThreads = bullets.stream()
                .map(Thread::new)
                .collect(Collectors.toList());
        playerTankThread.start();
        bulletThreads.forEach(Thread::start);
        botTanksThreads.forEach(Thread::start);
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<BotTank> getBotTanks() {
        return botTanks;
    }

    public void setBotTanks(List<BotTank> botTanks) {
        this.botTanks = botTanks;
    }

    public PlayerTank getPlayerTank() {
        return playerTank;
    }

    public void setPlayerTank(PlayerTank playerTank) {
        playerTank.addStateChangeListener(this);
        playerTank.addRemovedObjectListener(this);
        this.playerTank = playerTank;
        getGameMap().addObject(playerTank);
    }

    public List<Box> getBoxes() {
        return boxes;
    }

    public void setBoxes(List<Box> boxes) {
        this.boxes = boxes;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(List<Bullet> bullets) {
        this.bullets = bullets;
    }

    public List<Wall> getWalls() {
        return walls;
    }

    public void setWalls(List<Wall> walls) {
        this.walls = walls;
    }

    public void addBotTank(BotTank botTank) {
        botTanks.add(botTank);
        getGameMap().addObject(botTank);
    }

    public void addBox(Box box) {
        boxes.add(box);
        getGameMap().addObject(box);
    }

    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
        getGameMap().addObject(bullet);
    }

    public void addWall(Wall wall) {
        walls.add(wall);
        getGameMap().addObject(wall);
    }

    public Set<StateChangeListener> getListeners() {
        return stateChangeListeners;
    }

    public void setListeners(Set<StateChangeListener> stateChangeListeners) {
        this.stateChangeListeners = stateChangeListeners;
    }

    public void addListener(StateChangeListener stateChangeListener) {
        if (stateChangeListeners == null) {
            stateChangeListeners = new HashSet<>();
        }
        stateChangeListeners.add(stateChangeListener);
    }

    public void addRemoveObjectListener(RemoveGameObjectsListener listener) {
        if (removeGameObjectsListeners == null){
            removeGameObjectsListeners = new HashSet<>();
        }
        removeGameObjectsListeners.add(listener);
    }

    public void startBullet(Bullet bullet) {
        addBullet(bullet);
        Thread bulletThread = new Thread(bullet);
        bulletThread.start();
    }

    public void startBotTank(BotTank botTank) {
        addBotTank(botTank);
        Thread bulletThread = new Thread(botTank);
        bulletThread.start();
    }

    public void pause() {
        playerTank.setAlive(false);
        botTanks.forEach(tank -> tank.setAlive(false));
        bullets.forEach(bullet -> bullet.setAlive(false));
    }

    @Override
    public void dataChanged() {
        stateChangeListeners.forEach(StateChangeListener::dataChanged);
    }


    @Override
    public void objectRemoved(GameObject gameObject) {
        removeObject(gameObject);
        gameMap.deleteObject(gameObject);
        removeGameObjectsListeners.forEach(listener -> listener.objectRemoved(gameObject));
    }

    public void removeObject(GameObject gameObject) {
        switch (gameObject.getType()) {
            case BOT_TANK: {
                botTanks.remove(gameObject);
                break;
            }
            case BOX: {
                boxes.remove(gameObject);
                break;
            }
            case WALL: {
                walls.remove(gameObject);
                break;
            }
            case BULLET: {
                bullets.remove(gameObject);
                break;
            }
            case PLAYER_TANK: {
                bullets.forEach(
                        bullet -> bullet.setAlive(false)
                );
                botTanks.forEach(
                        botTank -> botTank.setAlive(false)
                );
            }
        }
    }

    @Override
    public void addPoints(int pointsCount) {
        score += pointsCount;
    }

    public boolean isEnded(){
        return !playerTank.isAlive();
    }
}
