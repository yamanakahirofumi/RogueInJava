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
    public String getName();

    /**
     * フロアー内の現在地を返す
     *
     * @return 現在地
     */
    public AbstractCoordinate getPostion();

    public void setPostion(AbstractCoordinate postion);

    /**
     * ダンジョン内の現在の階層数を返す
     *
     * @return
     */
    public int getLevel();
    public void setLevel(int level);

    /**
     * 現在のダンジョンを返す。
     * @return
     */
    public String getDungeon();
    public void setDungeon(String name);

    public int getStrength();

    public int getMaxHp();

    public int getFoodLeft();

    public void addState(StateEnum s);

    public void removeState(StateEnum s);

    public void changeFloor();

    public Optional<Thing> eat(Thing thing);

    /** 装備周り */
    public List<Weapon> getWeapons();

    public boolean putOnWeapon(Weapon w);

}
