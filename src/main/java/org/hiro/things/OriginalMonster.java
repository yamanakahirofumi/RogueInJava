package org.hiro.things;

import org.hiro.Room;
import org.hiro.character.StateEnum;

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
    Room getRoom();
    void setRoom(Room room);

    void addItem(ThingImp th);
    void removeItem(ThingImp th);
    List<ThingImp> getBaggage();
    int getBaggageSize();
    void setBaggage(List<ThingImp> list);
}
