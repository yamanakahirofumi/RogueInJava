package org.hiro.things;

import java.util.Arrays;

public enum PotionEnum {

	P_CONFUSE(0),
	P_LSD(1),
	P_POISON(2),
	P_STRENGTH(3),
	P_SEEINVIS(4),
	P_HEALING(5),
	P_MFIND(6),
	P_TFIND(7),
	P_RAISE(8),
	P_XHEAL(9),
	P_HASTE(10),
	P_RESTORE(11),
	P_BLIND(12),
	P_LEVIT(13);
	// public MAXPOTIONS(14);

	private int value;
	
	PotionEnum(int value){
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static int getMaxValue(){
		return PotionEnum.values().length;
	}

	static public PotionEnum get(int i){
		return Arrays.asList(PotionEnum.values()).stream().filter(o -> o.getValue() == i).findFirst().get();
	}

}
