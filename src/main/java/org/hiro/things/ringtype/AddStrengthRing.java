package org.hiro.things.ringtype;

import org.hiro.Const;
import org.hiro.Global;
import org.hiro.Obj_info;
import org.hiro.Util;
import org.hiro.things.Ring;
import org.hiro.things.RingEnum;

public class AddStrengthRing extends Ring {
    private int strength;

    public AddStrengthRing() {
        super();
        this._o_which = RingEnum.Protection.getValue();
        this.strength = Util.rnd(3);
        if (this.strength == 0) {
            this.strength = -1;
            this.add_o_flags(Const.ISCURSED);
        }

    }

    @Override
    public int getWorth() {
        Obj_info op = Global.ring_info[this._o_which];
        int worth = op.getWorth();
        if (this.strength > 0) {
            worth += this.strength * 100;
        } else {
            worth = 10;
        }
        if (!this.contains_o_flags(Const.ISKNOW)) {
            worth /= 2;
        }
        this.add_o_flags(Const.ISKNOW);
        op.know();
        return worth;
    }

    public int getStrength() {
        return strength;
    }
}
