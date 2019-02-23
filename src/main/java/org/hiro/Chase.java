package org.hiro;

import org.hiro.character.Human;
import org.hiro.character.StateEnum;
import org.hiro.map.Coordinate;
import org.hiro.map.RoomInfoEnum;
import org.hiro.output.Display;
import org.hiro.things.ObjectType;
import org.hiro.things.OriginalMonster;
import org.hiro.things.Thing;
import org.hiro.things.ThingImp;
import org.hiro.things.scrolltype.Scare;

/**
 * Code for one creature to chase another
 */
public class Chase {
    private static Coordinate ch_ret;

    /*
     * roomin:
     *	Find what room some coordinates are in. null means they aren't
     *	in any room.
     */
    static Room roomin(Coordinate cp) {

        int fp = Util.flat(cp);
        if ((fp & Const.F_PASS) != 0) {
            return Global.passages[fp & Const.F_PNUM];
        }
        for (int i = 0; i < Const.MAXROOMS; i++) {
            Room rp = Global.rooms.get(i);
            if (rp.isInMyRoom(cp)) {
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
    public static void runto(Coordinate runner) {
        ThingImp tp;

        /*
         * If we couldn't find him, something is funny
         */
        if ((tp = Util.getPlace(runner).p_monst) == null) {
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
    static Coordinate find_dest(ThingImp tp) {
        int prob;

        if ((prob = Global.monsters[tp.getType() - 'A'].m_carry) <= 0
                || tp.getRoom().equals(Human.instance.getRoom())
                || see_monst(tp)) {
            return Global.player._t_pos;
        }
        for (ThingImp obj : Global.lvl_obj) {
            if (obj instanceof Scare) {
                continue;
            }
            if (tp.getRoom().equals(roomin(obj._o_pos)) && Util.rnd(100) < prob) {
                for (ThingImp tp2 : Global.mlist) {
                    tp = tp2;
                    if (tp._t_dest.equals(obj._o_pos)) {
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
    public static boolean see_monst(ThingImp mp) {
        if (Human.instance.containsState(StateEnum.ISBLIND)) {
            return false;
        }
        if (mp.containsState(StateEnum.ISINVIS) && !Human.instance.containsState(StateEnum.CANSEE)) {
            return false;
        }
        int y = mp._t_pos.y;
        int x = mp._t_pos.x;
        if (dist_cp(mp._t_pos, Global.player._t_pos) < Const.LAMPDIST) {
            return y == Global.player._t_pos.y || x == Global.player._t_pos.x
                    || IOUtil.step_ok(Util.INDEX(y, Global.player._t_pos.x).p_ch)
                    || IOUtil.step_ok(Util.INDEX(Global.player._t_pos.y, x).p_ch);
        }
        if (!mp.getRoom().equals(Human.instance.getRoom())) {
            return false;
        }
        return !mp.getRoom().containInfo(RoomInfoEnum.ISDARK);
    }

    /*
     * dist:
     *	Calculate the "distance" between to points.  Actually,
     *	this calculates d^2, not d, but that's good enough for
     *	our purposes, since it's only used comparitively.
     */
    private static int dist(int y1, int x1, int y2, int x2) {
        return ((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    /*
     * isSee:
     *
     * 昔はcan_see()
     *	Returns true if the hero can see a certain coordinate.
     */
    public static boolean isSee(Coordinate c) {
        if (Human.instance.containsState(StateEnum.ISBLIND)) {
            return false;
        }
        if (dist_cp(c, Global.player._t_pos) < Const.LAMPDIST) {
            if ((Util.flat(c) & Const.F_PASS) != 0) {
                return c.y == Global.player._t_pos.y || c.x == Global.player._t_pos.x ||
                        IOUtil.step_ok(Util.INDEX(c.y, Global.player._t_pos.x).p_ch) ||
                        IOUtil.step_ok(Util.INDEX(Global.player._t_pos.y, c.x).p_ch);
            }
        }
        /*
         * We can only see if the hero in the same room as
         * the coordinate and the room is lit or if it is close.
         */
        Room rer = roomin(c);
        return rer.equals(Human.instance.getRoom()) && !rer.containInfo(RoomInfoEnum.ISDARK);
    }

    /*
     * diag_ok:
     *	Check to see if the move is legal if it is diagonal
     */
    public static boolean diag_ok(Coordinate sp, Coordinate ep) {
        if (ep.x < 0 || ep.x >= Const.NUMCOLS || ep.y <= 0 || ep.y >= Const.NUMLINES - 1) {
            return false;
        }
        if (ep.x == sp.x || ep.y == sp.y) {
            return true;
        }
        return (IOUtil.step_ok(Util.INDEX(ep.y, sp.x).p_ch) &&
                IOUtil.step_ok(Util.INDEX(sp.y, ep.x).p_ch));
    }

    /*
     * relocate:
     *	Make the monster's new location be the specified one, updating
     *	all the relevant state.
     */
    public static void relocate(ThingImp th, Coordinate new_loc) {

        if (!new_loc.equals(th._t_pos)) {
            Display.mvaddch(th._t_pos, (char) th._t_oldch);
            th.t_room = roomin(new_loc);
            set_oldch(th, new_loc);
            Room oroom = th.getRoom();
            Util.getPlace(th._t_pos).p_monst = null;

            if (!oroom.equals(th.getRoom())) {
                th._t_dest = find_dest(th);
            }
            th._t_pos = new_loc;
            Util.getPlace(new_loc).p_monst = th;
        }
        Display.move(new_loc);
        if (see_monst(th)) {
            Display.addch((char) th._t_disguise);
        } else if (Human.instance.containsState(StateEnum.SEEMONST)) {
            Display.standout();
            Display.addch((char) th.getType());
            Display.standend();
        }
    }

    /*
     * set_oldch:
     *	Set the oldch character for the monster
     */
    static void set_oldch(ThingImp tp, Coordinate cp) {

        if (tp._t_pos.equals(cp)) {
            return;
        }

        int sch = tp._t_oldch;
        tp._t_oldch = Util.CCHAR(Display.mvinch(cp));
        if (!Human.instance.containsState(StateEnum.ISBLIND)) {
            if ((sch == ObjectType.FLOOR.getValue() || tp._t_oldch == ObjectType.FLOOR.getValue()) &&
                    tp.getRoom().containInfo(RoomInfoEnum.ISDARK)) {
                tp._t_oldch = ' ';
            } else if (dist_cp(cp, Global.player._t_pos) <= Const.LAMPDIST && Global.see_floor) {
                tp._t_oldch = Util.getPlace(cp).p_ch.getValue();
            }
        }
    }

    /*
     * dist_cp:
     *	Call dist() with appropriate arguments for coordinate pointers
     */
    static int dist_cp(Coordinate c1, Coordinate c2) {
        return dist(c1.y, c1.x, c2.y, c2.x);
    }

    /*
     * runners:
     *	Make all the running monsters move.
     */
    static void runners() {

        for(ThingImp tp : Global.mlist){
            /* remember this in case the monster's "next" is changed */
            if (!tp.containsState(StateEnum.ISHELD) && tp.containsState(StateEnum.ISRUN)) {
                Coordinate orig_pos = tp._t_pos;
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
        if (!tp.containsState(StateEnum.ISSLOW) || tp.isSlow()) {
            if (do_chase(tp) == -1) {
                return -1;
            }
        }
        if (tp.containsState(StateEnum.ISHASTE)) {
            if (do_chase(tp) == -1) {
                return -1;
            }
        }
        tp.changeSlow();
        return 0;
    }

    /*
     * do_chase:
     *	Make one thing chase another.
     */
    static int do_chase(ThingImp th) {
        int DRAGONSHOT = 5;   /* one chance in DRAGONSHOT that a dragon will flame */
        int mindist = 32767;
        Coordinate thisTmp = new Coordinate();            /* Temporary destination for chaser */

        /* Find room of chaser */
        /* room of chaser, room of chasee */
        Room rer = th.getRoom();
        if (th.containsState(StateEnum.ISGREED) && rer.r_goldval == 0) {
            th._t_dest = Global.player._t_pos;    /* If gold has been taken, run after hero */
        }
        Room ree;
        if (th._t_dest == Global.player._t_pos) {    /* Find room of chasee */
            ree = Human.instance.getRoom();
        } else {
            ree = roomin(th._t_dest);
        }
        /*
         * We don't count doors as inside rooms for this routine
         */
        boolean door = (Util.getPlace(th._t_pos).p_ch == ObjectType.DOOR);
        /*
         * If the object of our desire is in a different room,
         * and we are not in a corridor, run to the door nearest to
         * our goal.
         */
        while (true) {
            if (rer != ree) {
                for (int i = 0; i < rer.r_nexits; i++) {
                    Coordinate cp = rer.r_exit[i];
                    int curdist = dist_cp(th._t_dest, cp);
                    if (curdist < mindist) {
                        thisTmp = cp;
                        mindist = curdist;
                    }
                }
                if (door) {
                    rer = Global.passages[Util.flat(th._t_pos) & Const.F_PNUM];
                    door = false;
                    continue;
                }
            } else {
                thisTmp = th._t_dest;
                /*
                 * For dragons check and see if (a) the hero is on a straight
                 * line from it, and (b) that it is within shooting distance,
                 * but outside of striking range.
                 */
                if (th.getType() == 'D' && (th._t_pos.y == Global.player._t_pos.y || th._t_pos.x == Global.player._t_pos.x
                        || Math.abs(th._t_pos.y - Global.player._t_pos.y) == Math.abs(th._t_pos.x - Global.player._t_pos.x))
                        && dist_cp(th._t_pos, Global.player._t_pos) <= Const.BOLT_LENGTH * Const.BOLT_LENGTH
                        && !th.containsState(StateEnum.ISCANC) && Util.rnd(DRAGONSHOT) == 0) {
                    Global.delta = new Coordinate(Misc.sign(Global.player._t_pos.x - th._t_pos.x),
                            Misc.sign(Global.player._t_pos.y - th._t_pos.y));
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
            break;
        }
        /*
         * This now contains what we want to run to this time
         * so we run to it.  If we hit it we either want to fight it
         * or stop running
         */
        /* true means we are there */
        boolean stoprun = false;
        if (!chase(th, thisTmp)) {
            if (thisTmp.equals(Global.player._t_pos)) {
                return (Fight.attack(Human.instance, th));
            } else if (thisTmp.equals(th._t_dest)) {
                for (ThingImp obj : Global.lvl_obj) {
                    if (th._t_dest.equals(obj._o_pos)) {
                        Global.lvl_obj.remove(obj);
                        th.addItem(obj);
                        Util.getPlace(obj._o_pos).p_ch =
                                th.getRoom().containInfo(RoomInfoEnum.ISGONE) ? ObjectType.PASSAGE : ObjectType.FLOOR;
                        th._t_dest = find_dest(th);
                        break;
                    }
                }
                if (th.getType() != 'F') {
                    stoprun = true;
                }
            }
        } else {
            if (th.getType() == 'F') {
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
    static boolean chase(ThingImp tp, Coordinate ee) {
        int curdist;
        Coordinate er = tp._t_pos;
        Coordinate tryp = new Coordinate();

        /*
         * If the thing is confused, let it move randomly. Invisible
         * Stalkers are slightly confused all of the time, and bats are
         * quite confused all the time
         */
        if ((tp.containsState(StateEnum.ISHUH) && Util.rnd(5) != 0) || (tp.getType() == 'P' && Util.rnd(5) == 0)
                || (tp.getType() == 'B' && Util.rnd(2) == 0)) {
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
            /*
             * This will eventually hold where we move to get closer
             * If we can't find an empty spot, we stay where we are.
             */
            curdist = dist_cp(er, ee);
            ch_ret = er;

            int ey = er.y + 1;
            if (ey >= Const.NUMLINES - 1) {
                ey = Const.NUMLINES - 2;
            }
            int ex = er.x + 1;
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
                    ObjectType ch = Util.winat(tryp);
                    if (IOUtil.step_ok(ch)) {
                        /*
                         * If it is a scroll, it might be a scare monster scroll
                         * so we need to look it up to see what type it is.
                         */
                        Thing obj2 = null;
                        if (ch == ObjectType.SCROLL) {
                            for (ThingImp obj : Global.lvl_obj) {
                                obj2 = obj;
                                if (obj._o_pos.equals(new Coordinate(x, y))) {
                                    break;
                                }
                            }
                            if (obj2 instanceof Scare) {
                                continue;
                            }
                        }
                        /*
                         * It can also be a Xeroc, which we shouldn't step on
                         */
                        OriginalMonster obj3;
                        if ((obj3 = Util.getPlace(tryp).p_monst) != null && obj3.getType() == 'X') {
                            continue;
                        }
                        /*
                         * If we didn't find any scrolls at this place or it
                         * wasn't a scare scroll, then this place counts
                         */
                        int thisdist = dist_cp(tryp, ee);
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
