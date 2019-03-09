package org.hiro.things;

import java.util.Arrays;

public enum PotionEnum {

	Confuse(0),
	LSD(1),
	Poison(2),
	Strength(3),
	SeeInvisible(4),
	Healing(5),
	MonsterFind(6),
	TrapFind(7),
	P_RAISE(8),
	P_XHEAL(9),
	P_HASTE(10),
	P_RESTORE(11),
	Blind(12),
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
