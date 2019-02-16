package org.hiro.things;

import org.hiro.Const;
import org.hiro.Global;
import org.hiro.Obj_info;
import org.hiro.ThingMethod;
import org.hiro.Util;
import org.hiro.character.Player;
import org.hiro.things.ringtype.MaintainArmorRing;

import java.util.List;

public class Armor extends ThingImp {
    int defence;

    public Armor() {
        super();
        this._o_which = ThingMethod.pick_one(getTypes());
        this.defence = Global.a_class[this._o_which];
        int r = Util.rnd(100);
        if (r < 20) {
            this.add_o_flags(Const.ISCURSED);
            this.defence += Util.rnd(3) + 1;
        } else if (r < 28) {
            this.defence -= Util.rnd(3) + 1;
        }
    }

    public Armor(ArmorEnum armorEnum, int armor, int flg) {
        super();
        this._o_which = armorEnum.getValue();
        this.defence = armor;
        this.add_o_flags(flg);
    }


    @Override
    public boolean isMagic() {
        return (this.contains_o_flags(Const.ISPROT) || this.defence != Global.a_class[this._o_which]);
    }

    @Override
    public int getWorth() {
        int worth = Global.arm_info[this._o_which].getWorth();
        worth += (9 - this.defence) * 100;
        worth += (10 * (Global.a_class[this._o_which] - this.defence));
        this.add_o_flags(Const.ISKNOW);
        return worth;
    }

    /**
     * @return
     */
    public List<Obj_info> getTypes() {
        String filename = "armors.csv";
        return this.getTypes(filename);
    }

    @Override
    public ObjectType getDisplay() {
        return ObjectType.ARMOR;
    }

    public int getDefence() {
        return defence;
    }

    /*
     * rust_armor:
     *	Rust the given armor, if it is a legal kind to rust, and we
     *	aren't wearing a magic ring.
     *  錆びた鎧
     */
    public boolean rust(Player player) {
        if (this._o_which == ArmorEnum.LEATHER.getValue() || this.getDefence() >= 9) {
            return true;
        }
        if (this.contains_o_flags(Const.ISPROT) || MaintainArmorRing.isInclude(player.getRings())) {
            return true;
        } else {
            this.defence++;
            return false;
        }
    }

    public void enchant(){
        this.defence--;
        this.delete_o_flags(Const.ISCURSED);
    }

}
