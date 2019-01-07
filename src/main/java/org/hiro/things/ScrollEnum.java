package org.hiro.things;

import java.util.Arrays;

/*
 * Scroll types
 */
public enum ScrollEnum {

	S_CONFUSE(0),
	S_MAP(1),
	S_HOLD(2),
	S_SLEEP(3),
	S_ARMOR(4),
	S_ID_POTION(5),
	S_ID_SCROLL(6),
	S_ID_WEAPON(7),
	S_ID_ARMOR(8),
	S_ID_R_OR_S(9),
	S_SCARE(10),
	S_FDET(11),
	S_TELEP(12),
	S_ENCH(13),
	S_CREATE(14),
	S_REMOVE(15),
	S_AGGR(16),
	S_PROTECT(17);
//	MAXSCROLLS(18);

	private int value;

	ScrollEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static int getMaxValue(){
		return ScrollEnum.values().length;
	}

	static public ScrollEnum get(int i){
		return Arrays.asList(ScrollEnum.values()).stream().filter(o -> o.getValue() == i).findFirst().get();
	}

}
