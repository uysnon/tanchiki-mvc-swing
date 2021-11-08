package ru.rseu.gorkin.view.gameprocess;

import ru.rseu.gorkin.model.game.objects.moving.Bullet;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class BulletView extends AbstractDrawableGameView {
    public static final String PATH_TO_IMAGE = "sprites/bullet.png";
    public static final String PATH_TO_IMAGE_PLAYER = "sprites/bullet-player.png";

    private Image spriteImage;
    private Image spriteImagePlayer;
    private Bullet bullet;

    public BulletView(int mapSize, Bullet bullet){
        super(mapSize);
        this.bullet = bullet;
        try {
            spriteImage = ImageIO.read(new File(getClass().getClassLoader().getResource(PATH_TO_IMAGE).getPath()));
            spriteImagePlayer = ImageIO.read(new File(getClass().getClassLoader().getResource(PATH_TO_IMAGE_PLAYER).getPath()));
            int imageSize = getScaledSizeOfImage(bullet.getSize());
            spriteImage = spriteImage.getScaledInstance(imageSize, imageSize, Image.SCALE_DEFAULT);
            spriteImagePlayer = spriteImagePlayer.getScaledInstance(imageSize, imageSize, Image.SCALE_DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Bullet getBullet() {
        return bullet;
    }

    public void setBullet(Bullet bullet) {
        this.bullet = bullet;
    }

    @Override
    public void draw(Graphics g) {
        Image image = null;
        if (bullet.isPlayer()){
            image = spriteImagePlayer;
        } else {
            image = spriteImage;
        }
        g.drawImage(
                image,
                getXForImageFromCenterX(bullet.getCoordinate().getX(), bullet.getSize()),
                getYForImageFromCenterY(bullet.getCoordinate().getY(), bullet.getSize()), null);
    }


}
