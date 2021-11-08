package ru.rseu.gorkin.view.gameprocess;

import ru.rseu.gorkin.model.Coordinate;
import ru.rseu.gorkin.model.game.Game;

import java.awt.*;
import java.util.function.Supplier;

public class GameInfoView extends AbstractDrawableGameView {
    private final static double X_COORDINATE_SCORE = 0.7;
    private final static double Y_COORDINATE_FROM_TOP_MAP_SCORE = 7;

    private final static double X_COORDINATE_GAME_OVER_MESSAGE = 0.7;
    private final static double Y_COORDINATE_GAME_OVER_MESSAGE = 10;

    private final static double X_COORDINATE_GAME_OVER_BUTTON_MAIN_MENU = 0.7;
    private final static double Y_COORDINATE_GAME_OVER_BUTTON_MAIN_MENU = 15;


    private Game game;
    private Coordinate<Integer> startTextScoreCoordinate;
    private Coordinate<Integer> startTextGameOverCoordinate;
    private Coordinate<Integer> startButtonGoToMainMenuCoordinate;
    private GameManager gameManager;


    public GameInfoView(int mapSize, Game game, GameManager gameManager) {
        super(mapSize);
        this.game = game;
        this.gameManager = gameManager;
        int xScore = getPrintableX(getMapSize() + X_COORDINATE_SCORE);
        int yScore = getPrintableY(getMapSize() - Y_COORDINATE_FROM_TOP_MAP_SCORE);
        startTextScoreCoordinate = new Coordinate<>(xScore, yScore);
        int xEndGame = getPrintableX(getMapSize() + X_COORDINATE_GAME_OVER_MESSAGE);
        int yEndGame = getPrintableY(getMapSize() - Y_COORDINATE_GAME_OVER_MESSAGE);
        startTextGameOverCoordinate = new Coordinate<>(xEndGame, yEndGame);
        int xGoToMainMenu = getPrintableX(getMapSize() + X_COORDINATE_GAME_OVER_BUTTON_MAIN_MENU);
        int yGoToMainMenu = getPrintableY(getMapSize() - Y_COORDINATE_GAME_OVER_BUTTON_MAIN_MENU);
        startButtonGoToMainMenuCoordinate = new Coordinate<>(xGoToMainMenu, yGoToMainMenu);
    }

    @Override
    public void draw(Graphics g) {
        g.setFont(new Font(
                Font.SANS_SERIF,
                Font.BOLD,
                20));
        g.setColor(Color.BLACK);
        g.drawString(
                "Score: \n" +
                        String.valueOf(
                                game.getScore()),
                startTextScoreCoordinate.getX(),
                startTextScoreCoordinate.getY());
        if (game.isEnded()) {
            g.drawString(
                    "Игра закончена!",
                    startTextGameOverCoordinate.getX(),
                    startTextGameOverCoordinate.getY());
            g.drawString(
                    "Нажмите ENTER",
                    startButtonGoToMainMenuCoordinate.getX(),
                    startButtonGoToMainMenuCoordinate.getY());
        }
    }
}
