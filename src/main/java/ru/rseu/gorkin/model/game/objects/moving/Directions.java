package ru.rseu.gorkin.model.game.objects.moving;

import ru.rseu.gorkin.model.Coordinate;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public enum Directions {
    UP {
        @Override
        public Coordinate<Double> increment(Coordinate<Double> baseCoordinate, double value) {
            return new Coordinate<>(baseCoordinate.getX(), baseCoordinate.getY() + value);
        }
    }, DOWN {
        @Override
        public Coordinate<Double> increment(Coordinate<Double> baseCoordinate, double value) {
            return new Coordinate<>(baseCoordinate.getX(), baseCoordinate.getY() - value);
        }
    }, RIGHT {
        @Override
        public Coordinate<Double> increment(Coordinate<Double> baseCoordinate, double value) {
            return new Coordinate<>(baseCoordinate.getX() + value, baseCoordinate.getY());
        }
    }, LEFT {
        @Override
        public Coordinate<Double> increment(Coordinate<Double> baseCoordinate, double value) {
            return new Coordinate<>(baseCoordinate.getX() - value, baseCoordinate.getY());
        }
    };


    public static Directions getRandomDirection() {
        int randomNum = ThreadLocalRandom.current().nextInt(0, values().length);
        return Stream.of(values()).filter(element -> element.ordinal() == randomNum)
                .findFirst().get();

    }

    public abstract Coordinate<Double> increment(Coordinate<Double> baseCoordinate, double value);
}
