package org.hiro.things;


import org.hiro.Util;

import java.util.Optional;

public class Gold implements Thing {

    int gold;

    public  Gold(int seed){
        this.gold = Util.rnd(50 + 10 * seed) + 2;
    }

    public int getGold(){
        return this.gold;
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
}
