package ru.rseu.gorkin.view.gameprocess;

import ru.rseu.gorkin.controller.GameProcessController;
import ru.rseu.gorkin.listeners.AddGameObjectsListener;
import ru.rseu.gorkin.listeners.RemoveGameObjectsListener;
import ru.rseu.gorkin.model.game.Game;
import ru.rseu.gorkin.model.game.objects.GameObject;
import ru.rseu.gorkin.model.game.objects.moving.BotTank;
import ru.rseu.gorkin.model.game.objects.moving.Bullet;
import ru.rseu.gorkin.model.game.objects.moving.Directions;
import ru.rseu.gorkin.view.View;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

public class GameView extends View implements AddGameObjectsListener, RemoveGameObjectsListener {
    private GameProcessController gameProcessController;
    private List<AbstractDrawableGameView> subViews;
    private List<BoxView> boxViews;
    private List<BulletView> bulletViews;
    private PlayerTankView playerTankView;
    private GameMapView gameMapView;
    private List<WallView> wallViews;
    private List<BotTankView> botTankViews;
    private HealthView healthView;
    private GameInfoView gameInfoView;
    private GameManager gameManager;


    public GameView(JFrame window, GameProcessController gameProcessController, GameManager gameManager) {
        this.gameProcessController = gameProcessController;
        this.gameManager = gameManager;

        boxViews = new CopyOnWriteArrayList<>();
        bulletViews = new CopyOnWriteArrayList<>();
        wallViews = new CopyOnWriteArrayList<>();
        botTankViews = new CopyOnWriteArrayList<>();

        gameProcessController.subscribeOnMapDataChange(this);
        gameProcessController.subscribeOnNewObjectOnMapCreated(this);
        gameProcessController.subscribeOnMapDataDeleted(this);

        Game game = gameProcessController.getGame();
        setWindow(window);
        int mapSize = game.getGameMap().getSize();

        gameMapView = new GameMapView(mapSize);
        game.getWalls().forEach(wall -> wallViews.add(new WallView(mapSize, wall)));
        game.getBoxes().forEach(box -> boxViews.add(new BoxView(mapSize, box)));
        game.getBotTanks().forEach(tank -> botTankViews.add(new BotTankView(mapSize, tank)));
        playerTankView = new PlayerTankView(mapSize, game.getPlayerTank());
        healthView = new HealthView(mapSize, game.getPlayerTank());
        gameInfoView = new GameInfoView(mapSize, game, gameManager);
    }

    @Override
    public void dataChanged() {
        render();
    }

    @Override
    public void start() {
        JPanel panel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                GameView.this.paint(g);
            }
        };
        setViewPanel(panel);
        getWindow().add(getViewPanel());
        setOnKeyPressedListener();
        panel.setFocusable(true);
        getWindow().revalidate();
        getWindow().transferFocus();
        gameProcessController.start();
    }

    @Override
    public void render() {
        getViewPanel().repaint();
    }


    @Override
    public void objectAdded(GameObject gameObject) {
        switch (gameObject.getType()) {
            case BULLET:
                bulletViews.add(new BulletView(
                        gameProcessController.getGame().getGameMap().getSize(),
                        (Bullet) gameObject));
                break;
            case BOT_TANK:
                botTankViews.add(new BotTankView(
                        gameProcessController.getGame().getGameMap().getSize(),
                        (BotTank) gameObject
                ));
        }
    }

    private void setOnKeyPressedListener() {
        JPanel panel = getViewPanel();
        panel.addKeyListener(new KeyAdapter() {
            int countPressedKeysDown = 0;
            int countPressedKeysUp = 0;
            int countPressedKeysLeft = 0;
            int countPressedKeysRight = 0;

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case (KeyEvent.VK_DOWN): {
                        setDefaultCounts();
                        countPressedKeysDown++;
                        runPlayerTankAndChangeDirection(Directions.DOWN);
                        break;
                    }
                    case (KeyEvent.VK_UP): {
                        setDefaultCounts();
                        countPressedKeysUp++;
                        runPlayerTankAndChangeDirection(Directions.UP);
                        break;
                    }
                    case (KeyEvent.VK_LEFT): {
                        setDefaultCounts();
                        countPressedKeysLeft++;
                        runPlayerTankAndChangeDirection(Directions.LEFT);
                        break;
                    }
                    case (KeyEvent.VK_RIGHT): {
                        setDefaultCounts();
                        countPressedKeysRight++;
                        runPlayerTankAndChangeDirection(Directions.RIGHT);
                        break;
                    }
                }
            }

            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case (KeyEvent.VK_DOWN): {
                        countPressedKeysDown--;
                        stopPlayerTankAndChangeDirection(Directions.DOWN);
                        break;
                    }
                    case (KeyEvent.VK_UP): {
                        countPressedKeysUp--;
                        stopPlayerTankAndChangeDirection(Directions.UP);

                        break;
                    }
                    case (KeyEvent.VK_LEFT): {
                        countPressedKeysLeft--;
                        stopPlayerTankAndChangeDirection(Directions.LEFT);
                        break;
                    }
                    case (KeyEvent.VK_RIGHT): {
                        countPressedKeysRight--;
                        stopPlayerTankAndChangeDirection(Directions.RIGHT);
                        break;
                    }
                    case (KeyEvent.VK_SPACE): {
                        gameProcessController.makePlayerShot();
                        break;
                    }
                    case (KeyEvent.VK_ESCAPE): {
                        gameManager.pause();
                        break;
                    }
                    case (KeyEvent.VK_ENTER): {
                        if (gameProcessController.getGame().isEnded()) {
                            gameManager.end();
                        }
                        break;
                    }
                }
            }

            private void setDefaultCounts() {
                countPressedKeysDown = 0;
                countPressedKeysLeft = 0;
                countPressedKeysUp = 0;
                countPressedKeysRight = 0;
            }

            private boolean isNowActiveKeys() {
                return (countPressedKeysUp > 0 ||
                        countPressedKeysLeft > 0 ||
                        countPressedKeysDown > 0 ||
                        countPressedKeysRight > 0);
            }

            private void stopPlayerTankAndChangeDirection(Directions direction) {
                if (!isNowActiveKeys()) {
                    gameProcessController.changePlayerTankDirection(direction);
                    gameProcessController.stopPlayerTank();
                }
            }

            private void runPlayerTankAndChangeDirection(Directions direction) {
                gameProcessController.changePlayerTankDirection(direction);
                gameProcessController.setDefaultSpeedForPlayerTank();
            }
        });
    }

    @Override
    public void objectRemoved(GameObject gameObject) {
        switch (gameObject.getType()) {
            case BOX:
                boxViews.removeIf(v -> v.getBox().equals(gameObject));
            case BULLET:
                bulletViews.removeIf(v -> v.getBullet().equals(gameObject));
            case BOT_TANK:
                botTankViews.removeIf(v -> v.getTank().equals(gameObject));
        }
    }

    private void paint(Graphics g) {
        validate();
        gameMapView.draw(g);
        wallViews.forEach(v -> v.draw(g));
        boxViews.forEach(v -> v.draw(g));
        botTankViews.forEach(v -> v.draw(g));
        playerTankView.draw(g);
        bulletViews.forEach(v -> v.draw(g));
        healthView.draw(g);
        gameInfoView.draw(g);
    }

    private void validate() {
        boxViews.removeIf(view -> view.getBox().getHealthPointsCount() < 0);
        bulletViews.removeIf(view -> !view.getBullet().isAlive());
        botTankViews.removeIf(view -> !view.getTank().isAlive());
    }

}
