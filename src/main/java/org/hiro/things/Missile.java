package org.hiro.things;

import org.hiro.Const;

public class Missile extends Weapon {
    public Missile(){
        this._o_which = '*';
        this._o_hurldmg = "1x4";
        this._o_hplus = 100;
        this._o_dplus = 1;
        this.set_o_flags(Const.ISMISL);
    }
}
