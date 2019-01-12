package org.hiro.character;

import org.hiro.map.AbstractCoordinate;
import org.hiro.things.Thing;
import org.hiro.things.Weapon;

import java.util.List;
import java.util.Optional;

public interface Player {

    /**
     * 名前を返す
     *
     * @return 名前
     */
    String getName();

    /**
     * フロアー内の現在地を返す
     *
     * @return 現在地
     */
    AbstractCoordinate getPostion();

    void setPostion(AbstractCoordinate postion);

    /**
     * ダンジョン内の現在の階層数を返す
     *
     * @return
     */
    int getLevel();
    void setLevel(int level);

    /**
     * 現在のダンジョンを返す。
     * @return
     */
    String getDungeon();
    void setDungeon(String name);

    int getStrength();

    int getMaxHp();

    int getFoodLeft();

    void addState(StateEnum s);

    void removeState(StateEnum s);

    void changeFloor();

    Optional<Thing> eat(Thing thing);

    /** 装備周り */
    List<Weapon> getWeapons();

    boolean putOnWeapon(Weapon w);

}
