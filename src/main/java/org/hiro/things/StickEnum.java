package org.hiro.things;

import java.util.Arrays;

/**
 * Rod/Wand/Staff types
 */
public enum StickEnum {

    /**
     * Light Up
     *
     * 旧WS_LIGHT
     */
    LightUp(0),
    /**
     * Invisible Seeing
     *
     * 旧WS_INVIS
     */
    InvisibleSeeing(1),
    /**
     * ElectricBolt
     *
     * 旧WS_ELECT
     */
    ElectricBolt(2),
    /**
     * FireBolt
     *
     * 旧WS_FIRE
     */
    FireBolt(3),
    /**
     * IceBolt
     *
     * 旧WS_COLD
     */
    IceBolt(4),
    /**
     * ChangeMonster
     *
     * 旧WS_POLYMORPH
     */
    ChangeMoster(5),
    /**
     * MagicMissile
     *
     * 旧WS_MISSILE
     */
    MagicMissile(6),
    /**
     * Haste Monster
     *
     * 旧WS_HASTE_M
     */
    HasteMonster(7),
    /**
     * Slow Monster
     *
     * 旧WS_SLOW_M
     */
    SlowMonster(8),
    /**
     * Drain
     *
     * 旧WS_DRAIN
     */
    Drain(9),
    /**
     * No Effect
     *
     * 旧WS_NOP
     */
    NoEffect(10),
    /**
     * Teleport Away
     *
     * 旧WS_TELAWAY
     */
    TeleportAway(11),
    /**
     * Teleport Me
     *
     * 旧WS_TELTO
     */
    TeleportMe(12),
    /**
     *　Invalidate effect
     *
     * 旧WS_CANCEL
     */
    InvalidateEffect(13);
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
        return Arrays.stream(StickEnum.values()).filter(o -> o.getValue() == i).findFirst().get();
    }

}
