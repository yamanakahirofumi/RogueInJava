package org.hiro.map;

/*
 * Coordinate data type
 * 座標
 */
public class Coordinate implements AbstractCoordinate {
    public int x;
    public int y;

    public Coordinate() {
        super();
    }

    public Coordinate(int x, int y) {
        super();
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public AbstractCoordinate random(AbstractCoordinate position, AbstractCoordinate size) {
        return null;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Coordinate) {
            Coordinate c = (Coordinate) obj;
            return this.x == c.x && this.y == c.y;
        } else {
            return false;
        }
    }

}
