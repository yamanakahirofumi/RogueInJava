package org.hiro.things;

public class Amulet extends ThingImp {
    public Amulet(){
        super();
    }

    @Override
    public boolean isMagic(){
        return true;
    }

    @Override
    public int getWorth(){
        return 1000;
    }

    @Override
    public ObjectType getDisplay() {
        return ObjectType.AMULET;
    }

}
