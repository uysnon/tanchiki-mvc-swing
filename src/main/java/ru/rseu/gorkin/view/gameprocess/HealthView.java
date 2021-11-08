package ru.rseu.gorkin.view.gameprocess;

import ru.rseu.gorkin.model.Coordinate;
import ru.rseu.gorkin.model.game.objects.moving.PlayerTank;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class HealthView extends AbstractDrawableGameView {
    private final static int IMAGE_SIZE = 3;
    private final static int X_COORDINATE = 2;
    private final static int Y_COORDINATE_FROM_TOP_MAP = 2;

    public static final String PATH_TO_IMAGE_HEALTH_POINTS_1 = "sprites/heart-1.png";
    public static final String PATH_TO_IMAGE_HEALTH_POINTS_2 = "sprites/heart-2.png";
    public static final String PATH_TO_IMAGE_HEALTH_POINTS_3 = "sprites/heart-3.png";
    public static final String PATH_TO_IMAGE_HEALTH_POINTS_EMPTY = "sprites/heart-empty.png";

    private Image spriteImageHealthPoints1;
    private Image spriteImageHealthPoints2;
    private Image spriteImageHealthPoints3;
    private Image spriteImageHealthPointsEmpty;

    private Coordinate<Integer> drawCoordinate;

    public PlayerTank playerTank;

    public HealthView(int mapSize, PlayerTank playerTank) {
        super(mapSize);
        this.playerTank = playerTank;
        try {
            spriteImageHealthPoints1 = ImageIO.read(new File(getClass().getClassLoader().getResource(
                    PATH_TO_IMAGE_HEALTH_POINTS_1).getPath()));
            spriteImageHealthPoints2 = ImageIO.read(new File(getClass().getClassLoader().getResource(
                    PATH_TO_IMAGE_HEALTH_POINTS_2).getPath()));
            spriteImageHealthPoints3 = ImageIO.read(new File(getClass().getClassLoader().getResource(
                    PATH_TO_IMAGE_HEALTH_POINTS_3).getPath()));
            spriteImageHealthPointsEmpty = ImageIO.read(new File(getClass().getClassLoader().getResource(
                    PATH_TO_IMAGE_HEALTH_POINTS_EMPTY).getPath()));
            int imageSize = getScaledSizeOfImage(IMAGE_SIZE);
            spriteImageHealthPoints1 = spriteImageHealthPoints1.getScaledInstance(imageSize, imageSize, Image.SCALE_DEFAULT);
            spriteImageHealthPoints2 = spriteImageHealthPoints2.getScaledInstance(imageSize, imageSize, Image.SCALE_DEFAULT);
            spriteImageHealthPoints3 = spriteImageHealthPoints3.getScaledInstance(imageSize, imageSize, Image.SCALE_DEFAULT);
            spriteImageHealthPointsEmpty = spriteImageHealthPointsEmpty.getScaledInstance(imageSize, imageSize, Image.SCALE_DEFAULT);

            int x = getXForImageFromCenterX(getMapSize() + X_COORDINATE, IMAGE_SIZE);
            int y = getYForImageFromCenterY(getMapSize() - Y_COORDINATE_FROM_TOP_MAP, IMAGE_SIZE);
            drawCoordinate = new Coordinate<>(x, y);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics g) {
        Image image = null;
        if (playerTank.getHealthPointsCount() >= 3) {
            image = spriteImageHealthPoints3;
        } else if (playerTank.getHealthPointsCount() >= 2) {
            image = spriteImageHealthPoints2;
        } else if (playerTank.getHealthPointsCount() >= 1) {
            image = spriteImageHealthPoints1;
        } else {
            image = spriteImageHealthPointsEmpty;
        }

        g.drawImage(
                image,
                drawCoordinate.getX(),
                drawCoordinate.getY(),
                null);


    }
}
