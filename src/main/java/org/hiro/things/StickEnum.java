package org.hiro.things;

import java.util.Arrays;

public enum StickEnum {

    /*
     * Rod/Wand/Staff types
     */
    WS_LIGHT(0),
    WS_INVIS(1),
    WS_ELECT(2),
    WS_FIRE(3),
    WS_COLD(4),
    WS_POLYMORPH(5),
    WS_MISSILE(6),
    WS_HASTE_M(7),
    WS_SLOW_M(8),
    WS_DRAIN(9),
    WS_NOP(10),
    WS_TELAWAY(11),
    WS_TELTO(12),
    WS_CANCEL(13);
    // MAXSTICKS(14;

    private int value;

    StickEnum(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static int getMaxValue(){
        return StickEnum.values().length;
    }

    static public StickEnum get(int i){
        return Arrays.asList(StickEnum.values()).stream().filter(o -> o.getValue() == i).findFirst().get();
    }

}
