package org.hiro.things;

import org.hiro.Global;
import org.hiro.ThingMethod;

import java.util.Arrays;

public class Scroll extends ThingImp {
	Scroll(){
		super();
		this._o_type = ObjectType.SCROLL;
		this._o_which = ThingMethod.pick_one(Arrays.asList(Global.scr_info), ScrollEnum.getMaxValue());
	}
}
