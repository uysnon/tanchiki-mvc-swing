package ru.rseu.gorkin.model;

import java.io.Serializable;
import java.util.Objects;

public class Coordinate<T extends Number> implements Serializable {
    private static final long serialVersionUID = 1L;

    private T x;
    private T y;

    public Coordinate() {
    }

    public Coordinate(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public T getX() {
        return x;
    }

    public T getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate<?> that = (Coordinate<?>) o;
        return Objects.equals(x, that.x) && Objects.equals(y, that.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
