package org.hiro.things;

import org.hiro.Const;
import org.hiro.Global;
import org.hiro.Obj_info;
import org.hiro.StickMethod;
import org.hiro.ThingMethod;

import java.util.Arrays;

public class Stick extends ThingImp {
    Stick(){
        this._o_which = ThingMethod.pick_one(Arrays.asList(Global.ws_info), StickEnum.getMaxValue());
        StickMethod.fix_stick(this);
//				boolean MASTER = false;
//				if (MASTER) {
//					break;
//					default:
//					debug("Picked a bad kind of object");
//					wait_for(stdscr, ' ');
//				}

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
