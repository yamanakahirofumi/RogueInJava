package org.hiro.character;

import org.hiro.Misc;
import org.hiro.Util;
import org.hiro.map.AbstractCoordinate;
import org.hiro.things.Armor;
import org.hiro.things.Food;
import org.hiro.things.Ring;
import org.hiro.things.Thing;
import org.hiro.things.Weapon;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * 腕が2本
 */
public class Human implements Player {
    public static Player instance = new Human("name");

    private String name;
    private int strength;
    private int currentStrength;
    long exp;                /* Experience */
    int lvl;                /* level of mastery */
    int arm;                /* ArmorEnum class */
    private int hp;            /* Hit points */
    String dmg;            /* String describing damage done */
    private int maxhp;            /* Max hit points */
    private int foodLeft;
    private int stomachSize;
    private int hugerTime;
    private int hungryState;
    private HashSet<StateEnum> state;
    /**
     * 位置情報
     */
    private AbstractCoordinate position;
    private int level;
    private String dungeon;
    /**
     * 装備周り
     */
    private Weapon weapon;
    private Armor armor;
    private Ring ring;

    public Human(String name) {
        this.name = name;
        this.stomachSize = 2000;
        this.state = new HashSet<>();
        this.weapon = null;
        this.hugerTime = 1300;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void changeFloor() {
        this.state.remove(StateEnum.ISHELD);

    }

    @Override
    public AbstractCoordinate getPosition() {
        return this.position;
    }

    @Override
    public void setPosition(AbstractCoordinate position) {
        this.position = position;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public void upstairs() {
        this.level++;
    }

    @Override
    public void downstairs() {
        this.level--;
    }

    @Override
    public String getDungeon() {
        return this.dungeon;
    }

    @Override
    public void setDungeon(String name) {
        this.dungeon = name;
    }

    @Override
    public Optional<Thing> eat(Thing t) {
        if(!(t instanceof Food)){
            return Optional.of(t);
        }

        if (this.foodLeft < 0) {
            this.foodLeft = 0;
        }

        if ((this.foodLeft += this.hugerTime - 200 + Util.rnd(400)) > this.stomachSize) {
            this.foodLeft = this.stomachSize;
        }
        this.hungryState = 0;
        // 装備している武器を食べる場合
        this.removeWeapon(t);
        if (Util.rnd(100) > 70) {
            this.addExperience(1);
        }

        // Pack.leave_pack(t, false, false);
        return t.eat();
    }

    @Override
    public int getCurrentStrength() {
        if (this.currentStrength < 3) {
            return 3;
        } else if (this.currentStrength > 31) {
            return 31;
        }

        return this.currentStrength;
    }

    @Override
    public int getMaxHp() {
        return this.maxhp;
    }

    @Override
    public int getHp() {
        return this.hp;
    }

    @Override
    public void deleteHp(int damage) {
        this.hp -= damage;
    }

    @Override
    public void addHp(int plus) {
        this.hp = this.hp + plus;
    }

    @Override
    public void addExperience(long exp) {
        this.exp += exp;
        Misc.check_level();
    }

    @Override
    public int getFoodLeft() {
        return 0;
    }

    @Override
    public int getStomachSize(){
        return this.stomachSize;
    }

    @Override
    public void addState(StateEnum s) {
        this.state.add(s);
    }

    @Override
    public void removeState(StateEnum s) {
        this.state.remove(s);
    }

    @Override
    public boolean containsState(StateEnum s) {
        return this.state.contains(s);
    }

    @Override
    public boolean isEquipped(Thing thing) {
        return this.weapon == thing || this.armor == thing || this.ring == thing;
    }

    @Override
    public List<Weapon> getWeapons() {
        if (this.weapon == null) {
            return new ArrayList<>();
        } else {
            return List.of(this.weapon);
        }
    }

    @Override
    public boolean isEquippedWeapons(Thing t) {
        return this.weapon == t;
    }

    @Override
    public boolean putOnWeapon(Weapon w) {
        this.weapon = w;
        return true;
    }

    @Override
    public boolean removeWeapon(Thing thing) {
        if (!isEquippedWeapons(thing)) {
            return false;
        }
        this.weapon = null;
        return true;
    }

}
