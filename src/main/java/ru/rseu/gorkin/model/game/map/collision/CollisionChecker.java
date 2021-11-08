package ru.rseu.gorkin.model.game.map.collision;

import ru.rseu.gorkin.model.Coordinate;
import ru.rseu.gorkin.model.game.objects.GameObject;

import java.awt.*;
import java.io.Serializable;

public class CollisionChecker implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final double HALF = 0.5;
    private static final double MULTIPLE_COEFFICIENT_FOR_ACCURACY = 1_000;

    public boolean isCollisionHappened(GameObject gameObject1,
                                       GameObject gameObject2) {
        Rectangle rectangle1 = createRectangleOfGameObject(gameObject1);
        Rectangle rectangle2 = createRectangleOfGameObject(gameObject2);
        return rectangle1.intersects(rectangle2);
    }

    public boolean isCollisionHappened(GameObject gameObject1,
                                       Coordinate<Double> desiredCoordinateOfObject1,
                                       GameObject gameObject2) {
        Rectangle rectangle1 = createRectangleFromCoordinateAndSizeOfObject(desiredCoordinateOfObject1, gameObject1.getSize());
        Rectangle rectangle2 = createRectangleOfGameObject(gameObject2);
        return rectangle1.intersects(rectangle2);
    }


    private Rectangle createRectangleOfGameObject(GameObject gameObject) {
        Rectangle rectangle = new Rectangle();
        try {
            return createRectangleFromCoordinateAndSizeOfObject(gameObject.getCoordinate(), gameObject.getSize());
        } catch (NullPointerException e){
            System.out.println(e);
        }
        return rectangle;
    }

    private Rectangle createRectangleFromCoordinateAndSizeOfObject(Coordinate<Double> coordinate, double size) {
        double x1 = coordinate.getX() - HALF * size;
        double y1 = coordinate.getY() - HALF * size;
        return new Rectangle(
                (int) Math.round(x1 * MULTIPLE_COEFFICIENT_FOR_ACCURACY),
                (int) Math.round(y1 * MULTIPLE_COEFFICIENT_FOR_ACCURACY),
                (int) Math.round(size * MULTIPLE_COEFFICIENT_FOR_ACCURACY),
                (int) Math.round(size * MULTIPLE_COEFFICIENT_FOR_ACCURACY));
    }


}
