package ru.rseu.gorkin.view.gameprocess;

import ru.rseu.gorkin.model.game.objects.moving.Directions;
import ru.rseu.gorkin.model.game.objects.moving.Tank;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BotTankView extends AbstractDrawableGameView {
    public static final String PATH_TO_IMAGE_RIGHT = "sprites/bot-right.png";
    public static final String PATH_TO_IMAGE_LEFT = "sprites/bot-left.png";
    public static final String PATH_TO_IMAGE_TOP = "sprites/bot-top.png";
    public static final String PATH_TO_IMAGE_BOTTOM = "sprites/bot-bottom.png";

    private Map<Directions, Image> spriteImages;
    private Tank tank;

    public BotTankView(int mapSize, Tank tank) {
        super(mapSize);
        this.tank = tank;
        try {
            int imageSize = getScaledSizeOfImage(tank.getSize());
            Image spriteImageRight = ImageIO
                    .read(new File(getClass().getClassLoader().getResource(PATH_TO_IMAGE_RIGHT).getPath()))
                    .getScaledInstance(imageSize, imageSize, Image.SCALE_DEFAULT);
            Image spriteImageLeft = ImageIO
                    .read(new File(getClass().getClassLoader().getResource(PATH_TO_IMAGE_LEFT).getPath()))
                    .getScaledInstance(imageSize, imageSize, Image.SCALE_DEFAULT);
            Image spriteImageTop = ImageIO
                    .read(new File(getClass().getClassLoader().getResource(PATH_TO_IMAGE_TOP).getPath()))
                    .getScaledInstance(imageSize, imageSize, Image.SCALE_DEFAULT);
            Image spriteImageBottom = ImageIO
                    .read(new File(getClass().getClassLoader().getResource(PATH_TO_IMAGE_BOTTOM).getPath()))
                    .getScaledInstance(imageSize, imageSize, Image.SCALE_DEFAULT);
            spriteImages = new HashMap<>();
            spriteImages.put(Directions.RIGHT, spriteImageRight);
            spriteImages.put(Directions.LEFT, spriteImageLeft);
            spriteImages.put(Directions.UP, spriteImageTop);
            spriteImages.put(Directions.DOWN, spriteImageBottom);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Tank getTank() {
        return tank;
    }

    public void setTank(Tank tank) {
        this.tank = tank;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(
                spriteImages.get(tank.getDirection()),
                getXForImageFromCenterX(tank.getCoordinate().getX(), tank.getSize()),
                getYForImageFromCenterY(tank.getCoordinate().getY(), tank.getSize()), null);
    }
}
