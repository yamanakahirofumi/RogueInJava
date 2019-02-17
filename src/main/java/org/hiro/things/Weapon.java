package org.hiro.things;

import org.hiro.Const;
import org.hiro.Global;
import org.hiro.InitWeapon;
import org.hiro.ThingMethod;
import org.hiro.Util;

import java.util.Arrays;

public class Weapon extends ThingImp {
    private String power; // = new char[8];        /* Damage if used like sword */
    public String _o_hurldmg; // = new char[8];        /* Damage if thrown */
    int hitPlus;

    public void addHit(){
        this.hitPlus++;
    }

    public int getHit(){
        return this.hitPlus;
    }

    Weapon() {
        super();
        this._o_which = ThingMethod.pick_one(Arrays.asList(Global.weap_info));
        initWeapon();
        int r = Util.rnd(100);
        if (r < 10) {
            this.add_o_flags(Const.ISCURSED);
            this.hitPlus -= Util.rnd(3) + 1;
        } else if (r < 15) {
            this.hitPlus += Util.rnd(3) + 1;
        }
    }

    public Weapon(WeaponEnum weaponEnum, int hplus){
        super();
        this._o_which =weaponEnum.getValue();
        initWeapon();
        this.hitPlus = hplus;
    }

    @Override
    public boolean isMagic(){
        return (this.hitPlus != 0 || this._o_dplus != 0);
    }

    @Override
    public int getWorth(){
        int worth = Global.weap_info[this._o_which].getWorth();
        worth *= 3 * (this.hitPlus + this._o_dplus) + this.getCount();
        this.add_o_flags(Const.ISKNOW);

        return worth;
    }

    /*
     * initWeapon:
     *	Set up the initial goodies for a Weapon
     */
    private void initWeapon() {

        InitWeapon iwp = Global.init_dam.get(this._o_which);
        this.power = iwp.iw_dam;
        this._o_hurldmg = iwp.iw_hrl;
        this._o_launch = iwp.iw_launch;
        this.set_o_flags(iwp.iw_flags);
        this.hitPlus = 0;
        this._o_dplus = 0;
        if (this._o_which == WeaponEnum.DAGGER.getValue()) {
            this._o_count = Util.rnd(4) + 2;
            this._o_group = Global.group++;
        } else if (this.contains_o_flags(Const.ISMANY)) {
            this._o_count = Util.rnd(8) + 8;
            this._o_group = Global.group++;
        } else {
            this._o_count = 1;
            this._o_group = 0;
        }
    }

    @Override
    public ObjectType getDisplay() {
        return ObjectType.WEAPON;
    }

    public String getDamage(){
        return this.power;
    }
}
