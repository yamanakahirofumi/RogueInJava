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

	public Armor(ArmorEnum armorEnum, int armor, int flg){
		super();
		this._o_which = armorEnum.getValue();
		this._o_arm = armor;
		this.add_o_flags(flg);
	}


	@Override
	public boolean isMagic(){
		return (this.contains_o_flags(Const.ISPROT) || this._o_arm != Global.a_class[this._o_which]);
	}

	@Override
	public int getWorth(){
		int worth = Global.arm_info[this._o_which].getWorth();
		worth += (9 - this._o_arm) * 100;
		worth += (10 * (Global.a_class[this._o_which] - this._o_arm));
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
}
