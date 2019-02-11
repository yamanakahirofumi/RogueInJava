package org.hiro.character;

import org.hiro.map.AbstractCoordinate;
import org.hiro.things.Armor;
import org.hiro.things.Ring;
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
    AbstractCoordinate getPosition();

    void setPosition(AbstractCoordinate position);

    /**
     * ダンジョン内の現在の階層数を返す
     *
     * @return
     */
    int getLevel();
    void setLevel(int level);
    void upstairs();
    void downstairs();

    /**
     * 現在のダンジョンを返す。
     * @return
     */
    String getDungeon();
    void setDungeon(String name);

    // ステータス周り
    int getCurrentStrength();
    void setStrength(int strength);
    int getMaxStrength();

    int getMaxHp();

    int getHp();
    void addHp(int plus);
    void deleteHp(int damage);

    int getStomachSize();

    int getFoodLeft();

    void addExperience(long exp);

    void addState(StateEnum s);

    void removeState(StateEnum s);

    boolean containsState(StateEnum s);

    void changeFloor();

    Optional<Thing> eat(Thing thing);

    /** 装備周り */
    boolean isEquipped(Thing thing);
    List<Weapon> getWeapons();
    boolean isEquippedWeapons(Thing thing);
    boolean putOnWeapon(Weapon w);
    boolean removeWeapon(Thing thing);
    boolean isEquippedArmor();
    boolean isEquippedArmor(Thing  thing);
    Armor getArmor();
    boolean putOnArmor(Armor armor);
    boolean removeArmor();
    boolean isEquippedRing(Ring ring);
    List<Ring> getRings();
    boolean putOnRing(Ring ring);

    int getBaggageSize();
    List<Thing> getBaggage();
    boolean addContent(Thing t);
    char getPositionOfContent(Thing t);
    boolean isContent(Thing t);

}
