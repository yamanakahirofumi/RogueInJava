package org.hiro.things;

import org.hiro.Room;
import org.hiro.Stats;
import org.hiro.character.StateEnum;
import org.hiro.map.AbstractCoordinate;

import java.util.List;

public interface OriginalMonster {
    void addState(StateEnum se);

    void removeState(StateEnum se);

    boolean containsState(StateEnum se);

    void setState(int flags);

    int getType();

    void setType(int type);

    boolean isSlow();

    void slow();

    void changeSlow();

    Stats getStatus();

    void setStatus(Stats status);

    Room getRoom();

    void setRoom(Room room);

    AbstractCoordinate getPosition();

    void setPosition(AbstractCoordinate coordinate);

    AbstractCoordinate getRunPosition();

    void setRunPosition(AbstractCoordinate coordinate);

    int getFloorTile();

    void setFloorTile(int tile);

    int getDisplayTile();

    void setDisplayTile(int tile);

    void addItem(Thing th);

    void removeItem(Thing th);

    List<Thing> getBaggage();

    int getBaggageSize();

    void setBaggage(List<Thing> list);
}
