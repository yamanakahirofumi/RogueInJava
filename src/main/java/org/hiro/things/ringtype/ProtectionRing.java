package org.hiro.things.ringtype;

import org.hiro.Const;
import org.hiro.Global;
import org.hiro.Obj_info;
import org.hiro.Util;
import org.hiro.things.Ring;
import org.hiro.things.RingEnum;

public class ProtectionRing extends Ring {
    private int defence;

    public ProtectionRing() {
        super();
        this._o_which = RingEnum.Protection.getValue();
        this.defence = Util.rnd(3);
        if (this.defence == 0) {
            this.defence = -1;
            this.add_o_flags(Const.ISCURSED);
        }

    }

    @Override
    public int getWorth() {
        Obj_info op = Global.ring_info[this._o_which];
        int worth = op.getWorth();
        if (this.defence > 0) {
            worth += this.defence * 100;
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

    public int getDefence() {
        return defence;
    }
}
