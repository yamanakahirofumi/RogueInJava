package org.hiro.things;

import java.util.Arrays;

/*
 * Ring types
 */
public enum RingEnum {

    R_PROTECT(0),
    R_ADDSTR(1),
    R_SUSTSTR(2),
    R_SEARCH(3),
    R_SEEINVIS(4),
    R_NOP(5),
    R_AGGR(6),
    R_ADDHIT(7),
    R_ADDDAM(8),
    R_REGEN(9),
    R_DIGEST(10),
    R_TELEPORT(11),
    R_STEALTH(12),
    R_SUSTARM(13);
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
