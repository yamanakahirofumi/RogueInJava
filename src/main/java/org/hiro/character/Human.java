package org.hiro.character;

import org.hiro.map.AbstractCoord;
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
    private String name;
    private int strength;
    long exp;                /* Experience */
    int lvl;                /* level of mastery */
    int arm;                /* ArmorEnum class */
    int hp;            /* Hit points */
    String dmg;            /* String describing damage done */
    private int maxhp;            /* Max hit points */
    private int foodLeft;
    private int stomachSize;
    HashSet<StateEnum> state;
    /** 位置情報 */
    private AbstractCoord postion;
    private int level;
    private String dungeon;
    /** 装備周り */
    private Weapon weapon;

    public Human(String  name){
        this.name = name;
        this.stomachSize = 2000;
        this.state = new HashSet<>();
        this.weapon = null;
    }

    @Override
    public String getName(){
        return this.name;
    }

    @Override
    public void changeFloor(){
        this.state.remove(StateEnum.ISHELD);

    }

    @Override
    public AbstractCoord getPostion() {
        return this.postion;
    }

    @Override
    public void setPostion(AbstractCoord postion) {
        this.postion = postion;
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
    public String getDungeon() {
        return this.dungeon;
    }

    @Override
    public void setDungeon(String name) {
        this.dungeon = name;
    }

    @Override
    public Optional<Thing> eat(Thing t) {
        if(this.foodLeft + t.foodValue() > this.stomachSize){
            this.foodLeft = this.stomachSize;
        }else{
            this.foodLeft += t.foodValue();
        }
        return t.eat();
    }

    @Override
    public int getStrength() {
        return this.strength;
    }

    @Override
    public int getMaxHp() {
        return this.maxhp;
    }

    @Override
    public int getFoodLeft() {
        return 0;
    }

    @Override
    public void addState(StateEnum s){
        this.state.add(s);
    }

    @Override
    public void removeState(StateEnum s){
        this.state.remove(s);
    }

    @Override
    public List<Weapon> getWeapons(){
        if(this.weapon == null) {
            return new ArrayList<>();
        }else{
            return List.of(this.weapon);
        }
    }

    @Override
    public boolean putOnWeapon(Weapon w){
        this.weapon = w;
        return true;
    }

}
