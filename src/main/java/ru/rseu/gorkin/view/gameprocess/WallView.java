package ru.rseu.gorkin.view.gameprocess;

import ru.rseu.gorkin.model.game.objects.immovable.Wall;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class WallView extends AbstractDrawableGameView {
    public static final String PATH_TO_IMAGE = "sprites/wall.png";
    private Image spriteImage;
    private Wall wall;

    public WallView(int mapSize, Wall wall) {
        super(mapSize);
        this.wall = wall;
        try {
            spriteImage = ImageIO.read(new File(getClass().getClassLoader().getResource(PATH_TO_IMAGE).getPath()));
            int imageSize = getScaledSizeOfImage(wall.getSize());
            spriteImage = spriteImage.getScaledInstance(imageSize, imageSize, Image.SCALE_DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Wall getWall() {
        return wall;
    }

    public void setWall(Wall wall) {
        this.wall = wall;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(
                spriteImage,
                getXForImageFromCenterX(wall.getCoordinate().getX(), wall.getSize()),
                getYForImageFromCenterY(wall.getCoordinate().getY(), wall.getSize()), null);
    }
}
