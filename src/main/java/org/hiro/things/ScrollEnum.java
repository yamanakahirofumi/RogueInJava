package org.hiro.things;

import java.util.Arrays;

/*
 * Scroll types
 */
public enum ScrollEnum {

	/**
	 * Confuse
     *
     * 旧S_CONFUSE
	 */
	Confuse(0),
    /**
     * MagicMap
     *
     * 旧S_MAP
     */
	MapScroll(1),
    /**
     * Hold Monster
     *
     * 旧S_HOLD
     */
	HoldMonster(2),
    /**
     * Sleep
     *
     * 旧S_SLEEP
     */
	Sleep(3),
    /**
     *　Enchant Armor
     *
     * 旧S_ARMOR
     */
    EnchantArmor(4),
    /**
     * Identify Potion
     *
     * 旧S_ID_POTION
     */
    IdentifyPotion(5),
    /**
     * Identify Scroll
     *
     * 旧S_ID_SCROLL
     */
    IdentifyScroll(6),
    /**
     * Identify Weapon
     *
     * 旧S_ID_WEAPON
     */
    IdentifyWeapon(7),
    /**
     * Identify Armor
     *
     * 旧S_ID_ARMOR
     */
    IdentifyArmor(8),
    /**
     * Identify Ring or Stick
     *
     * 旧S_ID_R_OR_S
     */
    IdentifyRingOrStick(9),
    /**
     * Scare
     *
     * 旧S_SCARE
     */
	Scare(10),
    /**
     * Food Detection
     *
     * 旧S_FDET
     */
	FoodDetection(11),
    /**
     * Teleportation
     *
     * 旧S_TELEP
     */
    Teleportation(12),
    /**
     * Enchant Weapon
     *
     * 旧S_ENCH
     */
    EnchantWeapon(13),
    /**
     * Create Monster
     *
     * 旧S_CREATE
     */
    CreateMonster(14),
    /**
     * Remove Curse
     *
     * 旧S_REMOVE
     */
    RemoveCurse(15),
    /**
     * Wake up Monster
     *
     * 旧S_AGGR
     */
	WakeUpMonster(16),
    /**
     * Protect Armor
     *
     * 旧S_PROTECT
     */
    ProtectArmor(17);
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
