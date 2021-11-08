package ru.rseu.gorkin.view.gameprocess;

import ru.rseu.gorkin.model.game.objects.immovable.Box;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class BoxView extends AbstractDrawableGameView {
    public static final String PATH_TO_IMAGE = "sprites/box.png";
    public static final String PATH_TO_IMAGE_DESTROYED = "sprites/box-destroyed.png";
    public Image spriteImage;
    public Image spriteImageDestroyed;
    private Box box;

    public BoxView(int mapSize, Box box) {
        super(mapSize);
        this.box = box;
        try {
            spriteImage = ImageIO.read(new File(getClass().getClassLoader().getResource(PATH_TO_IMAGE).getPath()));
            spriteImageDestroyed = ImageIO.read(new File(getClass().getClassLoader().getResource(PATH_TO_IMAGE_DESTROYED).getPath()));
            int imageSize = getScaledSizeOfImage(box.getSize());
            spriteImage = spriteImage.getScaledInstance(imageSize, imageSize, Image.SCALE_DEFAULT);
            spriteImageDestroyed = spriteImageDestroyed.getScaledInstance(imageSize, imageSize, Image.SCALE_DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }

    @Override
    public void draw(Graphics g) {
        Image image = null;
        if (box.getHealthPointsCount() > 1) {
            image = spriteImage;
        } else {
            image = spriteImageDestroyed;
        }
        g.drawImage(
                image,
                getXForImageFromCenterX(box.getCoordinate().getX(), box.getSize()),
                getYForImageFromCenterY(box.getCoordinate().getY(), box.getSize()), null);
    }
}
