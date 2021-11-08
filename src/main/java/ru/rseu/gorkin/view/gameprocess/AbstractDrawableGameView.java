package ru.rseu.gorkin.view.gameprocess;

public abstract class AbstractDrawableGameView implements OnMapDrawable {
    protected static final int CELL_SIZE = 36;
    protected static final int START_MAP_X = 10;
    protected static final int START_MAP_Y = 10;
    protected static final double HALF = 0.5;

    private int mapSize;

    public AbstractDrawableGameView(int mapSize) {
        this.mapSize = mapSize;
    }

    public int getMapSize() {
        return mapSize;
    }

    public void setMapSize(int mapSize) {
        this.mapSize = mapSize;
    }

    protected int getPrintableX(double x) {
        return (int) Math.round(CELL_SIZE * x) + START_MAP_X;
    }

    protected int getPrintableY(double y) {
        return -(int) Math.round(CELL_SIZE * y) + START_MAP_Y + mapSize * CELL_SIZE;
    }

    protected int getYForImageFromCenterY(double y, double imageSize) {
        return getPrintableY(y + imageSize * HALF);
    }

    protected int getXForImageFromCenterX(double x, double imageSize) {
        return getPrintableX(x - imageSize * HALF);
    }

    /**
     * size in virtual points, for example 2.5 (cell: x=2, y=2)
     *
     * @param size size of object
     * @return size in pixels
     */
    protected int getScaledSizeOfImage(double size) {
        return (int) Math.round(size * CELL_SIZE);
    }

}
