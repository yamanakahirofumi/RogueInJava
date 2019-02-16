package org.hiro.things.ringtype;

import org.hiro.Const;
import org.hiro.Global;
import org.hiro.Obj_info;
import org.hiro.Util;
import org.hiro.things.Ring;
import org.hiro.things.RingEnum;

public class AddDamageRing extends Ring {
    private int damage;

    public AddDamageRing() {
        super();
        this._o_which = RingEnum.AddDamage.getValue();
        if ((this.damage = Util.rnd(3)) == 0) {
            this.damage = -1;
            this.add_o_flags(Const.ISCURSED);
        }
    }

    @Override
    public int getWorth() {
        Obj_info op = Global.ring_info[this._o_which];
        int worth = op.getWorth();
        if (this.damage > 0) {
            worth += this.damage * 100;
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

    public int getDamage(){
        return this.damage;
    }
}
