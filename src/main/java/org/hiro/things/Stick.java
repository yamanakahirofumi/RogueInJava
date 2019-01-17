package org.hiro.things;

import org.hiro.Const;
import org.hiro.Global;
import org.hiro.Obj_info;
import org.hiro.ThingMethod;
import org.hiro.Util;

import java.util.Arrays;

public class Stick extends ThingImp {
    private String power;
    private String hurlPower;

    Stick(){
        this._o_which = ThingMethod.pick_one(Arrays.asList(Global.ws_info), StickEnum.getMaxValue());
        fix_stick();
//				boolean MASTER = false;
//				if (MASTER) {
//					break;
//					default:
//					debug("Picked a bad kind of object");
//					wait_for(stdscr, ' ');
//				}

    }

    /*
     * fix_stick:
     *	Set up a new stick
     */
    private void fix_stick() {
        if ("staff".equals(Global.ws_type[this._o_which])) {
            this.power = "2x3";
        } else {
            this.power = "1x1";
        }
        this.hurlPower = "1x1";

        if (this._o_which == StickEnum.WS_LIGHT.getValue()) {
            this._o_arm = Util.rnd(10) + 10;
        } else {
            this._o_arm = Util.rnd(5) + 3;
        }
    }

    @Override
    public boolean isMagic(){
        return true;
    }

    @Override
    public int getWorth(){
        Obj_info op = Global.ws_info[this._o_which];
        int worth = op.getWorth();
        worth += 20 * this._o_arm;
        if (!this.contains_o_flags(Const.ISKNOW)) {
            worth /= 2;
        }
        this.add_o_flags(Const.ISKNOW);
        op.know();
        return worth;
    }

    @Override
    public ObjectType getDisplay() {
        return ObjectType.STICK;
    }
}
