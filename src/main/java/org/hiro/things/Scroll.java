package org.hiro.things;

import org.hiro.Global;
import org.hiro.Obj_info;
import org.hiro.ThingMethod;

import java.util.Arrays;

public class Scroll extends ThingImp {
    public Scroll() {
        super();
        this._o_which = ThingMethod.pick_one(Arrays.asList(Global.scr_info));
    }

    @Override
    public boolean isMagic() {
        return true;
    }

    @Override
    public int getWorth() {
        int worth = Global.scr_info[this._o_which].getWorth();
        worth *= this._o_count;
        Obj_info op = Global.scr_info[this._o_which];
        if (!op.isKnown()) {
            worth /= 2;
        }
        op.know();

        return worth;
    }

    @Override
    public ObjectType getDisplay() {
        return ObjectType.SCROLL;
    }

    public void read() {

    }
}
