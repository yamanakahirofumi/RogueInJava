package org.hiro.things;

import org.hiro.Const;
import org.hiro.Global;
import org.hiro.ThingMethod;
import org.hiro.Util;
import org.hiro.WeaponMethod;

import java.util.Arrays;

public class Weapon extends ThingImp {
    Weapon() {
        super();
        WeaponMethod.init_weapon(this, ThingMethod.pick_one(Arrays.asList(Global.weap_info), WeaponEnum.getMaxValue()));
        int r = Util.rnd(100);
        if (r < 10) {
            this.add_o_flags(Const.ISCURSED);
            this._o_hplus -= Util.rnd(3) + 1;
        } else if (r < 15) {
            this._o_hplus += Util.rnd(3) + 1;
        }
    }
}
