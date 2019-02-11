package org.hiro.map;

import org.hiro.Util;

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
}
