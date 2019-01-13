package org.hiro.things;


import org.hiro.Const;
import org.hiro.Util;

import java.util.Optional;

public class Gold extends ThingImp {

    int gold;

    public  Gold(int seed){
        this.gold = Util.rnd(50 + 10 * seed) + 2;
        this.set_o_flags(Const.ISMANY);
        this._o_group = 1;
    }

    public int getGold(){
        return this.gold;
    }
    public void addGold(int money) {
        this.gold += money;
    }

    @Override
    public void add_o_flags(int flag) {

    }

    @Override
    public void delete_o_flags(int flag) {

    }

    @Override
    public void set_o_flags(int flag) {

    }

    @Override
    public boolean contains_o_flags(int flag) {
        return false;
    }

    @Override
    public int foodValue() {
        return 0;
    }

    @Override
    public Optional<Thing> eat() {
        return Optional.of(null);
    }

    @Override
    public ObjectType getDisplay() {
        return ObjectType.GOLD;
    }
}
