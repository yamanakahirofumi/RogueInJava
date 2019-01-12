package org.hiro;

import org.hiro.map.Coordinate;
import org.hiro.map.RoomInfoEnum;
import org.hiro.output.Display;
import org.hiro.things.ObjectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 通路
 * Draw the connecting passages
 */
public class Passage {

    /*
     * putpass:
     *	add a passage character or secret passage here
     *  通路や秘密の通路の追加
     */
    static void putpass(Coordinate cp) {
        Place pp = Util.INDEX(cp.y, cp.x);
        pp.p_flags |= Const.F_PASS;
        if (Util.rnd(10) + 1 < Global.level && Util.rnd(40) == 0) {
            pp.p_flags &= ~Const.F_REAL;
        } else {
            pp.p_ch = ObjectType.PASSAGE;
        }
    }

    /*
     * do_passages:
     *	Draw all the passages on a level.
     */

    static void do_passages() {
        List<RoomGraphDescription> rdes = new ArrayList<>();
        {
            RoomGraphDescription r = new RoomGraphDescription();
            r.conn = new int[]{0, 1, 0, 1, 0, 0, 0, 0, 0};
            Arrays.fill(r.isconn, false);
            r.ingraph = false;
            rdes.add(r);
        }
        {
            RoomGraphDescription r = new RoomGraphDescription();
            r.conn = new int[]{1, 0, 1, 0, 1, 0, 0, 0, 0};
            Arrays.fill(r.isconn, false);
            r.ingraph = false;
            rdes.add(r);
        }
        {
            RoomGraphDescription r = new RoomGraphDescription();
            r.conn = new int[]{0, 1, 0, 0, 0, 1, 0, 0, 0};
            Arrays.fill(r.isconn, false);
            r.ingraph = false;
            rdes.add(r);
        }
        {
            RoomGraphDescription r = new RoomGraphDescription();
            r.conn = new int[]{1, 0, 0, 0, 1, 0, 1, 0, 0};
            Arrays.fill(r.isconn, false);
            r.ingraph = false;
            rdes.add(r);
        }
        {
            RoomGraphDescription r = new RoomGraphDescription();
            r.conn = new int[]{0, 1, 0, 1, 0, 1, 0, 1, 0};
            Arrays.fill(r.isconn, false);
            r.ingraph = false;
            rdes.add(r);
        }
        {
            RoomGraphDescription r = new RoomGraphDescription();
            r.conn = new int[]{0, 0, 1, 0, 1, 0, 0, 0, 1};
            Arrays.fill(r.isconn, false);
            r.ingraph = false;
            rdes.add(r);
        }
        {
            RoomGraphDescription r = new RoomGraphDescription();
            r.conn = new int[]{0, 0, 0, 1, 0, 0, 0, 1, 0};
            Arrays.fill(r.isconn, false);
            r.ingraph = false;
            rdes.add(r);
        }
        {
            RoomGraphDescription r = new RoomGraphDescription();
            r.conn = new int[]{0, 0, 0, 0, 1, 0, 1, 0, 1};
            Arrays.fill(r.isconn, false);
            r.ingraph = false;
            rdes.add(r);
        }
        {
            RoomGraphDescription r = new RoomGraphDescription();
            r.conn = new int[]{0, 0, 0, 0, 0, 1, 0, 1, 0};
            Arrays.fill(r.isconn, false);
            r.ingraph = false;
            rdes.add(r);
        }

        /*
         * starting with one room, connect it to a random adjacent room and
         * then pick a new room to start with.
         */
        int r1Number = Util.rnd(Const.MAXROOMS);
        RoomGraphDescription r1 = rdes.get(r1Number);
        r1.ingraph = true;

        RoomGraphDescription r2 = null;
        int roomcount = 1;
        do {
            /*
             * find a room to connect with
             */
            int r2Number = 0;
            int j = 0;
            for (int i = 0; i < Const.MAXROOMS; i++) {
                if (r1.conn[i] != 0 && !rdes.get(i).ingraph && Util.rnd(++j) == 0) {
                    r2 = rdes.get(i);
                    r2Number = i;
                }
            }
            /*
             * if no adjacent rooms are outside the graph, pick a new room
             * to look from
             */
            if (j == 0) {
                do {
                    r1 = rdes.get(Util.rnd(Const.MAXROOMS));
                } while (!r1.ingraph);
            } else {
                /*
                 * otherwise, connect new room to the graph, and draw a tunnel
                 * to it
                 */
                r2.ingraph = true;
                conn(r1Number, r2Number);
                r1.isconn[r2Number] = true;
                r2.isconn[r1Number] = true;
                roomcount++;
            }
        } while (roomcount < Const.MAXROOMS);

        /*
         * attempt to add passages to the graph a random number of times so
         * that there isn't always just one unique passage through it.
         */
        for (roomcount = Util.rnd(5); roomcount > 0; roomcount--) {
            int from = Util.rnd(Const.MAXROOMS);
            r1 = rdes.get(from);    /* a random room to look from */
            /*
             * find an adjacent room not already connected
             */
            int j = 0;
            int to = 0;
            for (int i = 0; i < Const.MAXROOMS; i++) {
                if (r1.conn[i] != 0 && !r1.isconn[i] && Util.rnd(++j) == 0) {
                    to = i;
                    r2 = rdes.get(i);
                }
            }
            /*
             * if there is one, connect it and look for the next added
             * passage
             */
            if (j != 0) {
                conn(from, to);
                r1.isconn[to] = true;
                r2.isconn[from] = true;
            }
        }
        passnum();
    }

    /*
     * conn:
     *	Draw a corridor from a room in a certain direction.
     */
    static void conn(int r1, int r2) {
        int rm;
        int direc;

        if (r1 < r2) {
            rm = r1;
            if (r1 + 1 == r2) {
                direc = 'r';
            } else {
                direc = 'd';
            }
        } else {
            rm = r2;
            if (r2 + 1 == r1) {
                direc = 'r';
            } else {
                direc = 'd';
            }
        }
        Room rpf = Global.rooms.get(rm);
        /*
         * Set up the movement variables, in two cases:
         * first drawing one down.
         */
        int rmt;
        int distance = 0;
        int turn_distance = 0;
        Room rpt = null;
        Coordinate startPosition = new Coordinate();
        Coordinate endPosiiton = new Coordinate();
        Coordinate direction = new Coordinate();
        Coordinate turn_delta = new Coordinate();
        if (direc == 'd') {
            rmt = rm + 3;                /* room # of dest */
            rpt = Global.rooms.get(rmt);            /* room pointer of dest */
            direction.x = 0;
            direction.y = 1;                /* direction of move */
            startPosition = new Coordinate(rpf.r_pos.x, rpf.r_pos.y);            /* start of move */
            endPosiiton = new Coordinate(rpt.r_pos.x, rpt.r_pos.y);            /* end of move */
            if (!rpf.containInfo(RoomInfoEnum.ISGONE)) {    /* if not gone pick door pos */
                do {
                    startPosition.x = rpf.r_pos.x + Util.rnd(rpf.r_max.x - 2) + 1;
                    startPosition.y = rpf.r_pos.y + rpf.r_max.y - 1;
                } while (rpf.containInfo(RoomInfoEnum.ISMAZE) && (Util.flat(startPosition.y, startPosition.x) & Const.F_PASS) == 0);
            }
            if (!rpt.containInfo(RoomInfoEnum.ISGONE)) {
                do {
                    endPosiiton.x = rpt.r_pos.x + Util.rnd(rpt.r_max.x - 2) + 1;
                } while (rpt.containInfo(RoomInfoEnum.ISMAZE) && (Util.flat(endPosiiton.y, endPosiiton.x) & Const.F_PASS) == 0);
            }
            distance = Math.abs(startPosition.y - endPosiiton.y) - 1;    /* distance to move */
            turn_delta.x = (startPosition.x < endPosiiton.x ? 1 : -1);
            turn_delta.y = 0; /* direction to turn */
            turn_distance = Math.abs(startPosition.x - endPosiiton.x);    /* how far to turn */
        } else if (direc == 'r')            /* setup for moving right */ {
            rmt = rm + 1;
            rpt = Global.rooms.get(rmt);
            direction.x = 1;
            direction.y = 0;
            startPosition = new Coordinate(rpf.r_pos.x, rpf.r_pos.y);
            endPosiiton = new Coordinate(rpt.r_pos.x, rpt.r_pos.y);
            if (!rpf.containInfo(RoomInfoEnum.ISGONE)) {
                do {
                    startPosition.x = rpf.r_pos.x + rpf.r_max.x - 1;
                    startPosition.y = rpf.r_pos.y + Util.rnd(rpf.r_max.y - 2) + 1;
                } while (rpf.containInfo(RoomInfoEnum.ISMAZE) && (Util.flat(startPosition.y, startPosition.x) & Const.F_PASS) == 0);
            }
            if (!rpt.containInfo(RoomInfoEnum.ISGONE)) {
                do {
                    endPosiiton.y = rpt.r_pos.y + Util.rnd(rpt.r_max.y - 2) + 1;
                } while (rpt.containInfo(RoomInfoEnum.ISMAZE) && (Util.flat(endPosiiton.y, endPosiiton.x) & Const.F_PASS) == 0);
            }
            distance = Math.abs(startPosition.x - endPosiiton.x) - 1;
            turn_delta.x = 0;
            turn_delta.y = (startPosition.y < endPosiiton.y ? 1 : -1);
            turn_distance = Math.abs(startPosition.y - endPosiiton.y);
        }
        boolean MASTER = true;
        if (MASTER) {
        } else {
//    debug("error in connection tables");
        }

        /* where turn starts */
        int turn_spot = Util.rnd(distance - 1) + 1;

        /*
         * Draw in the doors on either side of the passage or just put #'s
         * if the rooms are gone.
         */
        if (!rpf.containInfo(RoomInfoEnum.ISGONE)) {
            door(rpf, startPosition);
        } else {
            putpass(startPosition);
        }
        if (!rpt.containInfo(RoomInfoEnum.ISGONE)) {
            door(rpt, endPosiiton);
        } else {
            putpass(endPosiiton);
        }
        /*
         * Get ready to move...
         */
        Coordinate curr = new Coordinate(startPosition.x, startPosition.y);
        while (distance > 0) {
            /*
             * Move to new position
             */
            curr.x += direction.x;
            curr.y += direction.y;
            /*
             * Check if we are at the turn place, if so do the turn
             */
            if (distance == turn_spot)
                while (turn_distance-- > 0) {
                    putpass(curr);
                    curr.x += turn_delta.x;
                    curr.y += turn_delta.y;
                }
            /*
             * Continue digging along
             */
            putpass(curr);
            distance--;
        }
        curr.x += direction.x;
        curr.y += direction.y;
        if (curr.x != endPosiiton.x || curr.y != endPosiiton.y) {
            //   msg("warning, connectivity problem on this level");
        }
    }


    /*
     * door:
     *	Add a door or possibly a secret door.  Also enters the door in
     *	the exits array of the room.
     */
    static void door(Room rm, Coordinate cp) {
        Place pp;

        rm.r_exit[rm.r_nexits++] = cp;

        if (rm.containInfo(RoomInfoEnum.ISMAZE)) {
            return;
        }

        pp = Util.INDEX(cp.y, cp.x);
        if (Util.rnd(10) + 1 < Global.level && Util.rnd(5) == 0) {
            if (cp.y == rm.r_pos.y || cp.y == rm.r_pos.y + rm.r_max.y - 1) {
                pp.p_ch = ObjectType.Horizon;
            } else {
                pp.p_ch = ObjectType.Vert;
            }
            pp.p_flags &= ~Const.F_REAL;
        } else {
            pp.p_ch = ObjectType.DOOR;
        }
    }


    /*
     * passnum:
     *	Assign a number to each passageway
     */
    static int pnum;
    static boolean newpnum;

    static void passnum() {
        pnum = 0;
        newpnum = false;
        for (Room rp : Global.passages) {
            rp.r_nexits = 0;
        }
        for (Room rp : Global.rooms) {
            for (int i = 0; i < rp.r_nexits; i++) {
                newpnum = true;
                numpass(rp.r_exit[i].y, rp.r_exit[i].x);
            }
        }
    }

    /*
     * numpass:
     *	Number a passageway square and its brethren
     */

    static void numpass(int y, int x) {

        if (x >= Const.NUMCOLS || x < 0 || y >= Const.NUMLINES || y <= 0) {
            return;
        }
        int fp = Util.flat(y, x);
        if ((fp & Const.F_PNUM) != 0) {
            return;
        }
        if (newpnum) {
            pnum++;
            newpnum = false;
        }
        /*
         * check to see if it is a door or secret door, i.e., a new exit,
         * or a numerable type of place
         */
        char ch = Global.places.get(((x) << 5) + (y)).p_ch.getValue();
        if (ch == ObjectType.DOOR.getValue() ||
                ((fp & Const.F_REAL) == 0 && (ch == ObjectType.Horizon.getValue() || ch == ObjectType.Vert.getValue()))) {
            Room rp = Global.passages[pnum];
            rp.r_exit[rp.r_nexits].y = y;
            rp.r_exit[rp.r_nexits++].x = x;
        } else if ((fp & Const.F_PASS) == 0) {
            return;
        }
        fp |= pnum; //
        /*
         * recurse on the surrounding places
         */
        numpass(y + 1, x);
        numpass(y - 1, x);
        numpass(y, x + 1);
        numpass(y, x - 1);
    }


    /*
     * add_pass:
     *	Add the passages to the current window (wizard command)
     */
    void add_pass() {
        Place pp;
        int y, x;
        ObjectType ch;

        for (y = 1; y < Const.NUMLINES - 1; y++)
            for (x = 0; x < Const.NUMCOLS; x++) {
                pp = Util.INDEX(y, x);
                if ((pp.p_flags & Const.F_PASS) != 0 || pp.p_ch == ObjectType.DOOR ||
                        ((pp.p_flags & Const.F_REAL) == 0 && (pp.p_ch == ObjectType.Vert || pp.p_ch == ObjectType.Horizon))) {
                    ch = pp.p_ch;
                    if ((pp.p_flags & Const.F_PASS) != 0) {
                        ch = ObjectType.PASSAGE;
                    }
                    pp.p_flags |= Const.F_SEEN;
                    Display.move(y, x);
                    if (pp.p_monst != null) {
                        pp.p_monst._t_oldch = pp.p_ch.getValue();
                    } else if ((pp.p_flags & Const.F_REAL) != 0) {
                        Display.addch(ch.getValue());
                    } else {
                        Display.standout();
                        Display.addch((pp.p_flags & Const.F_PASS) != 0 ? ObjectType.PASSAGE.getValue() : ObjectType.DOOR.getValue());
                        Display.standend();
                    }
                }
            }
    }
}
