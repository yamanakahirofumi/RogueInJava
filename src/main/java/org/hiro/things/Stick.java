package org.hiro.things;

import org.hiro.Global;
import org.hiro.StickMethod;
import org.hiro.ThingMethod;

import java.util.Arrays;

public class Stick extends ThingImp {
    Stick(){
        this._o_type = ObjectType.STICK;
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
}
