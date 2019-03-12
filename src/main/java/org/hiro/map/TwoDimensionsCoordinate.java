package org.hiro.map;

import org.hiro.Util;

import java.util.LinkedList;
import java.util.List;

/*
 * Coordinate data type
 * 座標
 */
public class TwoDimensionsCoordinate implements AbstractCoordinate {
    private int x;
    private int y;

    public TwoDimensionsCoordinate() {
        this.x = 0;
        this.y = 0;
    }

    TwoDimensionsCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public AbstractCoordinate random(AbstractCoordinate position, AbstractCoordinate size) {
        if (!(position instanceof TwoDimensionsCoordinate)) {
            throw new IllegalArgumentException("");
        }
        if (!(size instanceof TwoDimensionsCoordinate)) {
            throw new IllegalArgumentException("");
        }
        int _x = ((TwoDimensionsCoordinate) position).x + Util.rnd(((TwoDimensionsCoordinate) size).x - 2) + 1;
        int _y = ((TwoDimensionsCoordinate) position).x + Util.rnd(((TwoDimensionsCoordinate) size).y - 2) + 1;
        return new TwoDimensionsCoordinate(_x, _y);
    }


    @Override
    public AbstractCoordinate add(AbstractCoordinate coordinate) {
        if (coordinate instanceof TwoDimensionsCoordinate){
            throw new RuntimeException("型制限");
        }
        TwoDimensionsCoordinate c = (TwoDimensionsCoordinate) coordinate;
        return new TwoDimensionsCoordinate(this.x + c.x, this.y + c.y);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TwoDimensionsCoordinate) {
            TwoDimensionsCoordinate c = (TwoDimensionsCoordinate) obj;
            return this.x == c.x && this.y == c.y;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.y * 31 + this.x;
    }

    public void setDimensions(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public List<AbstractCoordinate> near() {
        List<AbstractCoordinate> result = new LinkedList<>();
        result.add(new TwoDimensionsCoordinate(this.x - 1, this.y - 1));
        result.add(new TwoDimensionsCoordinate(this.x - 1, this.y));
        result.add(new TwoDimensionsCoordinate(this.x - 1, this.y + 1));
        result.add(new TwoDimensionsCoordinate(this.x, this.y - 1));
        result.add(new TwoDimensionsCoordinate(this.x, this.y + 1));
        result.add(new TwoDimensionsCoordinate(this.x + 1, this.y - 1));
        result.add(new TwoDimensionsCoordinate(this.x + 1, this.y));
        result.add(new TwoDimensionsCoordinate(this.x + 1, this.y + 1));
        return result;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public int getZ() {
        return 0;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public void setZ(int z) {

    }
}
