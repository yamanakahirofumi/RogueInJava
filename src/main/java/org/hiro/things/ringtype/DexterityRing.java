package org.hiro.things.ringtype;

import org.hiro.Const;
import org.hiro.Global;
import org.hiro.Obj_info;
import org.hiro.Util;
import org.hiro.things.Ring;
import org.hiro.things.RingEnum;

public class DexterityRing extends Ring {
    private int dexterity;

    public DexterityRing(){
        super();
        this._o_which = RingEnum.Dexterity.getValue();
        this.dexterity = Util.rnd(3);
        if (this.dexterity == 0) {
            this.dexterity = -1;
            this.add_o_flags(Const.ISCURSED);
        }

    }

    @Override
    public int getWorth() {
        Obj_info op = Global.ring_info[this._o_which];
        int worth = op.getWorth();
        if (this.dexterity > 0) {
            worth += this.dexterity * 100;
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

    public int getDexterity() {
        return dexterity;
    }
}
