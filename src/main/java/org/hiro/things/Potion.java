package org.hiro.things;

import org.hiro.Global;
import org.hiro.Obj_info;
import org.hiro.ThingMethod;

import java.util.List;

public class Potion extends ThingImp {

    Potion() {
        super();
        this._o_which = ThingMethod.pick_one(getTypes());
    }

    @Override
    public boolean isMagic(){
        return true;
    }

    @Override
    public int getWorth(){
        int worth = Global.pot_info[this._o_which].getWorth();
        worth *= this._o_count;
        Obj_info op = Global.pot_info[this._o_which];
        if (!op.isKnown()) {
            worth /= 2;
        }
        op.know();
        return worth;
    }

    /**
     * @return
     */
    public List<Obj_info> getTypes() {
        String filename = "potions.csv";
        return this.getTypes(filename);
    }

    @Override
    public ObjectType getDisplay() {
        return ObjectType.POTION;
    }
}
