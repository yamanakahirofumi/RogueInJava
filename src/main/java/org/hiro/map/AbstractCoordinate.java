package org.hiro.map;

import java.util.List;

/*
 * Coordinate data type
 * 座標
 */
public interface AbstractCoordinate {
    public AbstractCoordinate random(AbstractCoordinate position, AbstractCoordinate size);
    public AbstractCoordinate add(AbstractCoordinate co0rdinate);
    public List<AbstractCoordinate> near();
    int getX();
    int getY();
    int getZ();
    void setX(int x);
    void setY(int y);
    void setZ(int z);
}
