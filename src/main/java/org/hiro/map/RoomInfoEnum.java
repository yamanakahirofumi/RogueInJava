package org.hiro.map;

public enum RoomInfoEnum {
    /* room is dark */
    ISDARK(0000001),
    /* room is gone (a corridor) */ // 部屋を過ぎ去った (廊下)
    ISGONE(0000002),
    /* room is gone (a corridor) */
    ISMAZE(0000004);

    private int value;

    RoomInfoEnum(int value) {
        this.value = value;
    }

}
