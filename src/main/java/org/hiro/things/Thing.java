package org.hiro.things;

import org.hiro.map.AbstractCoordinate;

import java.util.Optional;

public interface Thing {
    void add_o_flags(int flag);

    void delete_o_flags(int flag);

    void set_o_flags(int flag);

    boolean contains_o_flags(int flag);

    AbstractCoordinate getOPos();

    void setOPos(AbstractCoordinate pos);

    int foodValue();

    Optional<Thing> eat();

    /**
     * isMagic
     *
     * 旧is_magic:
     *	Returns true if an object radiates magic
     */
    default boolean isMagic(){
        return false;
    }
    int getWorth();

    default ObjectType getDisplay(){
        return  ObjectType.Initial;
    }

    boolean isGroup();
    int getGroup();
    void addCount(int c);
    int getCount();
    void reduceCount();

    int getNumber();
}
