package org.hiro.things;

import org.hiro.Const;
import org.hiro.Global;
import org.hiro.ThingMethod;

import java.util.Arrays;

public class ThingFactory {
	public static Thing create() {
		switch (Global.no_food > 3 ? 2 : ThingMethod.pick_one(Arrays.asList(Global.things), Const.NUMTHINGS)) {
			case 0:
				return new Potion();
			case 1:
				return new Scroll();
			case 2:
				return new Food();
			case 3:
				return new Weapon();
			case 4:
				return new Armor();
			case 5:
				return new Ring();
			case 6:
				return new Stick();
		}
		throw new RuntimeException("Bugs!!");
	}
}
