package org.hiro.things;

import org.hiro.Const;

public class Magic extends Weapon {
    public Magic(){
        this._o_which = '*';
        this._o_hurldmg = "1x4";
        this.hitPlus = 100;
        this._o_dplus = 1;
        this.set_o_flags(Const.ISMISL);
    }
}
