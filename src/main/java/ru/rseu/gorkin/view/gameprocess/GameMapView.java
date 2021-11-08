package ru.rseu.gorkin.view.gameprocess;
import java.awt.*;

public class GameMapView extends AbstractDrawableGameView {

    public GameMapView(int mapSize) {
        super(mapSize);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        for(int x = 0; x <= getMapSize(); x++){
            g.drawLine(
                    getPrintableX(x),
                    getPrintableY(0),
                    getPrintableX(x),
                    getPrintableY(getMapSize())
            );
        }
        for(int y = 0; y <= getMapSize(); y++){
            g.drawLine(
                    getPrintableX(0),
                    getPrintableY(y),
                    getPrintableX(getMapSize()),
                    getPrintableY(y)
            );
        }
    }
}
