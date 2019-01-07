package org.hiro.things;

public enum WeaponEnum {

    /*
     * Weapon types
     */
    MACE(0),
    SWORD(1),
    BOW(2),
    ARROW(3),
    DAGGER(4),
    TWOSWORD(5),
    DART(6),
    SHIRAKEN(7),
    SPEAR(8),
    FLAME(9);    /* fake entry for dragon breath (ick) */
    // MAXWEAPONS(9),    /* this should equal FLAME */

    private int value;
    WeaponEnum(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static int getMaxValue(){
        return WeaponEnum.values().length -1;
    }
}
