package org.hiro;

import java.util.Arrays;

/**
 * Trap types
 */
public enum TrapEnum {
    T_DOOR(0),
    T_ARROW(1),
    T_SLEEP(2),
    T_BEAR(3),
    T_TELEP(4),
    T_DART(5),
    T_RUST(6),
    T_MYST(7);

    private int value;

    TrapEnum(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static int count(){
        return TrapEnum.values().length;
    }

    public static TrapEnum get(int i){
        return Arrays.asList(TrapEnum.values()).stream().filter(o -> o.getValue() == i).findFirst().orElse(null);
    }
}
