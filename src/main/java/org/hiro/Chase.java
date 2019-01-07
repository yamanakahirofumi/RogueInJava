package org.hiro;

import org.hiro.character.StateEnum;
import org.hiro.map.AbstractCoord;
import org.hiro.map.Coord;
import org.hiro.map.RoomInfoEnum;
import org.hiro.output.Display;
import org.hiro.things.ObjectType;
import org.hiro.things.ScrollEnum;
import org.hiro.things.ThingImp;

/**
 * Code for one creature to chase another
 */
public class Chase {
    private static Coord ch_ret;

    /*
     * roomin:
     *	Find what room some coordinates are in. null means they aren't
     *	in any room.
     */
    static Room roomin(Coord cp) {

        int fp = Util.flat(cp.y, cp.x);
        if ((fp & Const.F_PASS) != 0) {
            return Global.passages[fp & Const.F_PNUM];
        }
        for (int i = 0; i < Const.MAXROOMS; i++) {
            Room rp = Global.rooms.get(i);
            if (cp.x <= ((Coord) rp.r_pos).x + ((Coord) rp.r_max).x && ((Coord) rp.r_pos).x <= cp.x
                    && cp.y <= ((Coord) rp.r_pos).y + ((Coord) rp.r_max).y && ((Coord) rp.r_pos).y <= cp.y) {
                return rp;
            }
        }

        // msg("in some bizarre place (%d, %d)", unc( * cp));
        boolean MASTER = false;
        if (MASTER) {
            // abort();
            return null;
        } else {
            return null;
        }
    }

    /*
     * runto:
     *	Set a monster running after the hero.
     */
    static void runto(Coord runner) {
        ThingImp tp;

        /*
         * If we couldn't find him, something is funny
         */
        if ((tp = Global.places.get((runner.y << 5) + runner.x).p_monst) == null) {
            boolean MASTER = true;
            if (MASTER) {
                //	msg("couldn't find monster in runto at (%d,%d)", runner. y, runner.x);
            }
            return;
        }

        /*
         * Start the beastie running
         */
        tp.addState(StateEnum.ISRUN);
        tp.removeState(StateEnum.ISHELD);
        tp._t_dest = find_dest(tp);
    }

    /*
     * find_dest:
     *	find the proper destination for the monster
     */
    static Coord find_dest(ThingImp tp) {
        int prob;

        if ((prob = Global.monsters[tp._t_type - 'A'].m_carry) <= 0
                || tp.t_room == Global.player.t_room
                || see_monst(tp)) {
            return Global.player._t_pos;
        }
        for (ThingImp obj : Global.lvl_obj) {
            if (obj._o_type == ObjectType.SCROLL && obj._o_which == ScrollEnum.S_SCARE.getValue()) {
                continue;
            }
            if (roomin(obj._o_pos) == tp.t_room && Util.rnd(100) < prob) {
                for (ThingImp tp2 : Global.mlist) {
                    tp = tp2;
                    if (tp._t_dest == obj._o_pos) {
                        break;
                    }
                }
                if (tp == null) {
                    return obj._o_pos;
                }
            }
        }
        return Global.player._t_pos;
    }

    /*
     * see_monst:
     *	Return true if the hero can see the monster
     *  true: 主人公がモンスターを見える場合
     */
    static boolean see_monst(ThingImp mp) {
        if (Global.player.containsState(StateEnum.ISBLIND)) {
            return false;
        }
        if (mp.containsState(StateEnum.ISINVIS) && !Global.player.containsState(StateEnum.CANSEE)) {
            return false;
        }
        int y = mp._t_pos.y;
        int x = mp._t_pos.x;
        if (dist(y, x, Global.player._t_pos.y, Global.player._t_pos.x) < Const.LAMPDIST) {
            if (y != Global.player._t_pos.y && x != Global.player._t_pos.x
                    && !IOUtil.step_ok(Global.places.get((y << 5) + Global.player._t_pos.x).p_ch)
                    && !IOUtil.step_ok(Global.places.get((Global.player._t_pos.y << 5) + x).p_ch)) {
                return false;
            }
            return true;
        }
        if (mp.t_room != Global.player.t_room) {
            return false;
        }
        return !mp.t_room.containInfo(RoomInfoEnum.ISDARK);
    }

    /*
     * dist:
     *	Calculate the "distance" between to points.  Actually,
     *	this calculates d^2, not d, but that's good enough for
     *	our purposes, since it's only used comparitively.
     */
    static int dist(int y1, int x1, int y2, int x2) {
        return ((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    /*
     * cansee:
     *	Returns true if the hero can see a certain coordinate.
     */
    static boolean cansee(int y, int x) {

        if (Global.player.containsState(StateEnum.ISBLIND)) {
            return false;
        }
        if (dist(y, x, Global.player._t_pos.y, Global.player._t_pos.x) < Const.LAMPDIST) {
            if ((Util.flat(y, x) & Const.F_PASS) != 0) {
                if (y != Global.player._t_pos.y && x != Global.player._t_pos.x &&
                        !IOUtil.step_ok(Global.places.get((Global.player._t_pos.x << 5) + y).p_ch) &&
                        !IOUtil.step_ok(Global.places.get((x << 5) + Global.player._t_pos.y).p_ch)) {
                    return false;
                }
                return true;
            }
        }
        /*
         * We can only see if the hero in the same room as
         * the coordinate and the room is lit or if it is close.
         */
        Coord tp = new Coord(x, y);
        Room rer;
        return ((rer = roomin(tp)) == Global.player.t_room && !rer.containInfo(RoomInfoEnum.ISDARK));
    }

    /*
     * diag_ok:
     *	Check to see if the move is legal if it is diagonal
     */
    static boolean diag_ok(Coord sp, Coord ep) {
        if (ep.x < 0 || ep.x >= Const.NUMCOLS || ep.y <= 0 || ep.y >= Const.NUMLINES - 1) {
            return false;
        }
        if (ep.x == sp.x || ep.y == sp.y) {
            return true;
        }
        return (IOUtil.step_ok(Global.places.get((sp.x << 5) + ep.y).p_ch) &&
                IOUtil.step_ok(Global.places.get((ep.x << 5) + sp.y).p_ch));
    }

    /*
     * relocate:
     *	Make the monster's new location be the specified one, updating
     *	all the relevant state.
     */
    static void relocate(ThingImp th, Coord new_loc) {

        if (!new_loc.equals(th._t_pos)) {
            Display.mvaddch(th._t_pos.y, th._t_pos.x, (char) th._t_oldch);
            th.t_room = roomin(new_loc);
            set_oldch(th, new_loc);
            Room oroom = th.t_room;
            Global.places.get((th._t_pos.x << 5) + th._t_pos.y).p_monst = null;

            if (oroom != th.t_room) {
                th._t_dest = find_dest(th);
            }
            th._t_pos = new_loc;
            Global.places.get((new_loc.x << 5) + new_loc.y).p_monst = th;
        }
        Display.move(new_loc.y, new_loc.x);
        if (see_monst(th)) {
            Display.addch((char) th._t_disguise);
        } else if (Global.player.containsState(StateEnum.SEEMONST)) {
            Display.standout();
            Display.addch((char) th._t_type);
            Display.standend();
        }
    }

    /*
     * set_oldch:
     *	Set the oldch character for the monster
     */
    static void set_oldch(ThingImp tp, Coord cp) {

        if (tp._t_pos.equals(cp)) {
            return;
        }

        int sch = tp._t_oldch;
        tp._t_oldch = Util.CCHAR(Display.mvinch(cp.y, cp.x));
        if (!Global.player.containsState(StateEnum.ISBLIND)) {
            if ((sch == ObjectType.FLOOR.getValue() || tp._t_oldch == ObjectType.FLOOR.getValue()) &&
                    tp.t_room.containInfo(RoomInfoEnum.ISDARK)) {
                tp._t_oldch = ' ';
            } else if (dist_cp(cp, Global.player._t_pos) <= Const.LAMPDIST && Global.see_floor) {
                tp._t_oldch = Global.places.get((cp.x << 5) + cp.y).p_ch.getValue();
            }
        }
    }

    /*
     * dist_cp:
     *	Call dist() with appropriate arguments for coord pointers
     */
    static int dist_cp(Coord c1, Coord c2) {
        return dist(c1.y, c1.x, c2.y, c2.x);
    }

    /*
     * runners:
     *	Make all the running monsters move.
     */
    static void runners() {
        ThingImp next;

        for (ThingImp tp = Global.mlist.get(0); tp != null; tp = next) {
            /* remember this in case the monster's "next" is changed */
            next = tp._l_next;
            if (!tp.containsState(StateEnum.ISHELD) && tp.containsState(StateEnum.ISRUN)) {
                Coord orig_pos = tp._t_pos;
                boolean wastarget = tp.containsState(StateEnum.ISTARGET);
                if (move_monst(tp) == -1)
                    continue;
                if (tp.containsState(StateEnum.ISFLY) && dist_cp(Global.player._t_pos, tp._t_pos) >= 3)
                    move_monst(tp);
                if (wastarget && !orig_pos.equals(tp._t_pos)) {
                    tp.removeState(StateEnum.ISTARGET);
                    Global.to_death = false;
                }
            }
        }
        if (Global.has_hit) {
            IOUtil.endmsg();
            Global.has_hit = false;
        }
    }

    /*
     * move_monst:
     *	Execute a single turn of running for a monster
     */
    static int move_monst(ThingImp tp) {
        if (!tp.containsState(StateEnum.ISSLOW) || tp._t_turn) {
            if (do_chase(tp) == -1) {
                return (-1);
            }
        }
        if (tp.containsState(StateEnum.ISHASTE)) {
            if (do_chase(tp) == -1) {
                return (-1);
            }
        }
        tp._t_turn ^= true;
        return (0);
    }

    /*
     * do_chase:
     *	Make one thing chase another.
     */
    static int do_chase(ThingImp th) {
        int DRAGONSHOT = 5;   /* one chance in DRAGONSHOT that a dragon will flame */
        AbstractCoord cp;
        Room rer, ree;    /* room of chaser, room of chasee */
        int mindist = 32767, curdist;
        boolean stoprun = false;    /* true means we are there */
        boolean door;
        Coord thisTmp = new Coord();            /* Temporary destination for chaser */

        rer = th.t_room;        /* Find room of chaser */
        if (th.containsState(StateEnum.ISGREED) && rer.r_goldval == 0) {
            th._t_dest = Global.player._t_pos;    /* If gold has been taken, run after hero */
        }
        if (th._t_dest == Global.player._t_pos) {    /* Find room of chasee */
            ree = Global.player.t_room;
        } else {
            ree = roomin(th._t_dest);
        }
        /*
         * We don't count doors as inside rooms for this routine
         */
        door = (Global.places.get((th._t_pos.x << 5) + th._t_pos.y).p_ch == ObjectType.DOOR);
        /*
         * If the object of our desire is in a different room,
         * and we are not in a corridor, run to the door nearest to
         * our goal.
         */
        over:
        while (true) {
            if (rer != ree) {
                for (int i = 0; i < rer.r_nexits; i++) {
                    cp = rer.r_exit[i];
                    curdist = dist_cp(th._t_dest, (Coord) cp);
                    if (curdist < mindist) {
                        thisTmp = (Coord) cp;
                        mindist = curdist;
                    }
                }
                if (door) {
                    rer = Global.passages[Util.flat(th._t_pos.y, th._t_pos.x) & Const.F_PNUM];
                    door = false;
                    continue over;
                }
            } else {
                thisTmp = th._t_dest;
                /*
                 * For dragons check and see if (a) the hero is on a straight
                 * line from it, and (b) that it is within shooting distance,
                 * but outside of striking range.
                 */
                if (th._t_type == 'D' && (th._t_pos.y == Global.player._t_pos.y || th._t_pos.x == Global.player._t_pos.x
                        || Math.abs(th._t_pos.y - Global.player._t_pos.y) == Math.abs(th._t_pos.x - Global.player._t_pos.x))
                        && dist_cp(th._t_pos, Global.player._t_pos) <= Const.BOLT_LENGTH * Const.BOLT_LENGTH
                        && !th.containsState(StateEnum.ISCANC) && Util.rnd(DRAGONSHOT) == 0) {
                    Global.delta.y = Misc.sign(Global.player._t_pos.y - th._t_pos.y);
                    Global.delta.x = Misc.sign(Global.player._t_pos.x - th._t_pos.x);
                    if (Global.has_hit) {
                        IOUtil.endmsg();
                    }
                    StickMethod.fire_bolt(th._t_pos, Global.delta, "flame");
                    Global.running = false;
                    Global.count = 0;
                    Global.quiet = 0;
                    if (Global.to_death && !th.containsState(StateEnum.ISTARGET)) {
                        Global.to_death = false;
                        Global.kamikaze = false;
                    }
                    return (0);
                }
            }
            break over;
        }
        /*
         * This now contains what we want to run to this time
         * so we run to it.  If we hit it we either want to fight it
         * or stop running
         */
        if (!chase(th, thisTmp)) {
            if (thisTmp.equals(Global.player._t_pos)) {
                return (Fight.attack(th));
            } else if (thisTmp.equals(th._t_dest)) {
                for (ThingImp obj : Global.lvl_obj) {
                    if (th._t_dest == obj._o_pos) {
                        Global.lvl_obj.remove(obj);
                        th.addItem(obj);
                        Global.places.get((obj._o_pos.x << 5) + obj._o_pos.y).p_ch =
                                th.t_room.containInfo(RoomInfoEnum.ISGONE) ? ObjectType.PASSAGE : ObjectType.FLOOR;
                        th._t_dest = find_dest(th);
                        break;
                    }
                }
                if (th._t_type != 'F') {
                    stoprun = true;
                }
            }
        } else {
            if (th._t_type == 'F') {
                return (0);
            }
        }
        relocate(th, ch_ret);
        /*
         * And stop running if need be
         */
        if (stoprun && th._t_pos.equals(th._t_dest)) {
            th.removeState(StateEnum.ISRUN);
        }
        return (0);
    }

    /*
     * chase:
     *	Find the spot for the chaser(er) to move closer to the
     *	chasee(ee).  Returns TRUE if we want to keep on chasing later
     *	FALSE if we reach the goal.
     */
    static boolean chase(ThingImp tp, Coord ee) {
        int curdist;
        Coord er = tp._t_pos;
        Coord tryp = new Coord();

        /*
         * If the thing is confused, let it move randomly. Invisible
         * Stalkers are slightly confused all of the time, and bats are
         * quite confused all the time
         */
        if ((tp.containsState(StateEnum.ISHUH) && Util.rnd(5) != 0) || (tp._t_type == 'P' && Util.rnd(5) == 0)
                || (tp._t_type == 'B' && Util.rnd(2) == 0)) {
            /*
             * get a valid random move
             */
            ch_ret = Move.rndmove(tp);
            curdist = dist_cp(ch_ret, ee);
            /*
             * Small chance that it will become un-confused
             */
            if (Util.rnd(20) == 0) {
                tp.removeState(StateEnum.ISHUH);
            }
        }
        /*
         * Otherwise, find the empty spot next to the chaser that is
         * closest to the chasee.
         */
        else {
            int ey, ex;
            /*
             * This will eventually hold where we move to get closer
             * If we can't find an empty spot, we stay where we are.
             */
            curdist = dist_cp(er, ee);
            ch_ret = er;

            ey = er.y + 1;
            if (ey >= Const.NUMLINES - 1) {
                ey = Const.NUMLINES - 2;
            }
            ex = er.x + 1;
            if (ex >= Const.NUMCOLS) {
                ex = Const.NUMCOLS - 1;
            }

            int plcnt = 1;
            for (int x = er.x - 1; x <= ex; x++) {
                if (x < 0)
                    continue;
                tryp.x = x;
                for (int y = er.y - 1; y <= ey; y++) {
                    tryp.y = y;
                    if (!diag_ok(er, tryp))
                        continue;
                    ObjectType ch = Util.winat(y, x);
                    if (IOUtil.step_ok(ch)) {
                        /*
                         * If it is a scroll, it might be a scare monster scroll
                         * so we need to look it up to see what type it is.
                         */
                        ThingImp obj2 = null;
                        if (ch == ObjectType.SCROLL) {
                            for (ThingImp obj : Global.lvl_obj) {
                                obj2 = obj;
                                if (y == obj._o_pos.y && x == obj._o_pos.x) {
                                    break;
                                }
                            }
                            if (obj2 != null && obj2._o_which == ScrollEnum.S_SCARE.getValue()) {
                                continue;
                            }
                        }
                        /*
                         * It can also be a Xeroc, which we shouldn't step on
                         */
                        if ((obj2 = Global.places.get((x << 5) + y).p_monst) != null && obj2._t_type == 'X') {
                            continue;
                        }
                        /*
                         * If we didn't find any scrolls at this place or it
                         * wasn't a scare scroll, then this place counts
                         */
                        int thisdist = dist(y, x, ee.y, ee.x);
                        if (thisdist < curdist) {
                            plcnt = 1;
                            ch_ret = tryp;
                            curdist = thisdist;
                        } else if (thisdist == curdist && Util.rnd(++plcnt) == 0) {
                            ch_ret = tryp;
                            curdist = thisdist;
                        }
                    }
                }
            }
        }
        return (curdist != 0 && !ch_ret.equals(Global.player._t_pos));
    }

}
