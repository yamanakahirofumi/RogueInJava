package org.hiro;

public class RoomGraphDescription {
    int[] conn = new int[Const.MAXROOMS];        /* possible to connect to room i? */
    boolean[] isconn = new boolean[Const.MAXROOMS];    /* connection been made to room i? */
    boolean ingraph;        /* this room in graph already? */
}
