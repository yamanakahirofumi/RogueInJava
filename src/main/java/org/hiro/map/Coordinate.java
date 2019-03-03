package org.hiro.map;

import java.util.LinkedList;
import java.util.List;

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

    public Coordinate(Coordinate c) {
        this.x = c.x;
        this.y = c.y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getZ() {
        return 0;
    }

    @Override
    public AbstractCoordinate random(AbstractCoordinate position, AbstractCoordinate size) {
        return null;
    }

    @Override
    public Coordinate add(AbstractCoordinate coordinate) {
        if (coordinate instanceof Coordinate) {
            throw new RuntimeException("型制限");
        }
        Coordinate c = (Coordinate) coordinate;
        return new Coordinate(this.x + c.getX(), this.y + c.getY());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Coordinate) {
            Coordinate c = (Coordinate) obj;
            return this.x == c.x && this.y == c.y;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.y * 31 + this.x;
    }

    @Override
    public List<AbstractCoordinate> near() {
        List<AbstractCoordinate> result = new LinkedList<>();
        result.add(new Coordinate(this.x - 1, this.y - 1));
        result.add(new Coordinate(this.x - 1, this.y));
        result.add(new Coordinate(this.x - 1, this.y + 1));
        result.add(new Coordinate(this.x, this.y - 1));
        result.add(new Coordinate(this.x, this.y + 1));
        result.add(new Coordinate(this.x + 1, this.y - 1));
        result.add(new Coordinate(this.x + 1, this.y));
        result.add(new Coordinate(this.x + 1, this.y + 1));
        return result;
    }
}
