package org.hiro.things;

import org.hiro.Const;
import org.hiro.Global;
import org.hiro.ThingMethod;
import org.hiro.Util;

import java.util.Arrays;

public class Ring extends ThingImp {
    Ring() {
        super();
        this._o_type = ObjectType.RING;
        this._o_which = ThingMethod.pick_one(Arrays.asList(Global.ring_info), RingEnum.getMaxValue()); // TODO
        if (this._o_which == RingEnum.R_ADDSTR.getValue() || this._o_which == RingEnum.R_PROTECT.getValue()
                || this._o_which == RingEnum.R_ADDHIT.getValue() || this._o_which == RingEnum.R_ADDDAM.getValue()) {
            if ((this._o_arm = Util.rnd(3)) == 0) {
                this._o_arm = -1;
                this.add_o_flags(Const.ISCURSED);
            }
        } else if (this._o_which == RingEnum.R_AGGR.getValue() || this._o_which == RingEnum.R_TELEPORT.getValue()) {
            this.add_o_flags(Const.ISCURSED);
        }

    }
}
