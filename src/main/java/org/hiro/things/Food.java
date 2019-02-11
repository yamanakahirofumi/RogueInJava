package org.hiro.things;

import org.hiro.Global;
import org.hiro.Util;

import java.util.Optional;

public class Food extends ThingImp {

//	private ObjectType _o_type;
//	private int _o_which;

	Food(){
		Global.no_food = 0;
		if (Util.rnd(10) != 0) {
			this._o_which = 0;
		} else {
			this._o_which = 1;
		}

	}

	@Override
	public void add_o_flags(int flag) {

	}

	@Override
	public void delete_o_flags(int flag) {

	}

	@Override
	public void set_o_flags(int flag) {

	}

	@Override
	public boolean contains_o_flags(int flag) {
		return false;
	}

	@Override
	public int foodValue() {
		return 0;
	}

	@Override
	public Optional<Thing> eat() {
		return Optional.empty();
	}

	@Override
	public int getWorth(){
		return 2 * this.getCount();
	}

	@Override
	public ObjectType getDisplay() {
		return ObjectType.FOOD;
	}
}
