package org.hiro.things;

public enum ArmorEnum {
	/**
	 * 革の鎧
	 */
	LEATHER(0),
	/**
	 * リングメイル
	 */
	RING_MAIL(1),
	/**
	 * 鋲付き革の鎧
	 */
	STUDDED_LEATHER(2),
	/**
	 * 鱗の鎧
	 */
	SCALE_MAIL(3),
	/**
	 * チェインメイル
	 */
	CHAIN_MAIL(4),
	/**
	 *
	 */
	SPLINT_MAIL(5),
	/**
	 *
	 */
	BANDED_MAIL(6),
	/**
	 * プレートメイル
	 */
	PLATE_MAIL(7);
//	MAXARMORS(8);

	private int value;

	ArmorEnum(int value){
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	static public int getMaxValue(){
		return ArmorEnum.values().length;
	}
}
