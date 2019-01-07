package org.hiro.things;

import org.hiro.Const;
import org.hiro.Global;
import org.hiro.Obj_info;
import org.hiro.ThingMethod;
import org.hiro.Util;

import java.util.List;

public class Armor extends ThingImp {
	public Armor(){
		super();
		this._o_type = ObjectType.ARMOR;
		this._o_which = ThingMethod.pick_one(getTypes(), ArmorEnum.getMaxValue());
		this._o_arm = Global.a_class[this._o_which];
		int r = Util.rnd(100);
		if (r < 20) {
			this.add_o_flags(Const.ISCURSED);
			this._o_arm += Util.rnd(3) + 1;
		} else if (r < 28) {
			this._o_arm -= Util.rnd(3) + 1;
		}
	}

	/**
	 * @return
	 */
	public List<Obj_info> getTypes() {
		String filename = "armors.csv";
		return this.getTypes(filename);
	}

}
