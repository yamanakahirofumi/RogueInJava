package org.hiro.things;

import java.util.Arrays;

/*
 * Ring types
 */
public enum RingEnum {

    /**
     * Protection
     *
     * R_PROTECT
     */
    Protection(0),
    /**
     * AddStrendth
     *
     */
    AddStrength(1),
    /**
     * SustainStrength
     *
     * 体力維持
     * 旧R_SUSTSTR
     */
    SustainStrength(2),
    /**
     * Searching
     *
     * 旧R_SEARCH
     */
    Searching(3),
    /**
     * see invisible
     *
     * 旧R_SEEINVIS
     */
    SeeInvisible(4),
    /**
     * adornment
     *
     * 旧R_NOP
     */
    Adornment(5),
    /**
     * aggravate monster
     *
     * 旧R_AGGR
     */
    AggravateMonster(6),
    /**
     * dexterity
     *
     * 旧R_ADDHIT
     */
    Dexterity(7),
    /**
     * addDamage
     *
     * 旧R_ADDDAM
     */
    AddDamage(8),
    /**
     *　regeneration
     *
     * 旧R_REGEN
     */
    Regeneration(9),
    /**
     * slow digestion
     *
     * 旧R_DIGEST
     */
    SlowDigestion(10),
    /**
     * teleportation
     *
     * 旧R_TELEPORT
     */
    Teleportation(11),
    /**
     * stealth
     *
     * 旧R_STEALTH
     */
    Stealth(12),
    /**
     * maintain armor
     *
     * 旧R_SUSTARM
     */
    MaintainArmor(13);
//    MAXRINGS(14);

    private int value;

    RingEnum(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static int getMaxValue(){
        return RingEnum.values().length;
    }

    static public RingEnum get(int i){
        return Arrays.stream(RingEnum.values()).filter(o -> o.getValue() == i).findFirst().get();
    }
}
