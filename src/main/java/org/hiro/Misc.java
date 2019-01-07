package org.hiro;

import org.hiro.character.StateEnum;
import org.hiro.map.Coord;
import org.hiro.map.RoomInfoEnum;
import org.hiro.output.Display;
import org.hiro.things.ObjectType;
import org.hiro.things.RingEnum;
import org.hiro.things.Thing;
import org.hiro.things.ThingImp;

import java.lang.reflect.Method;

/**
 * All sorts of miscellaneous routines
 */
public class Misc {

    /*
     * rnd_thing:
     *	Pick a random thing appropriate for this level
     */
    static ObjectType rnd_thing() {
        int i;
        ObjectType[] thing_list = {
                ObjectType.POTION,
                ObjectType.SCROLL,
                ObjectType.RING,
                ObjectType.STICK,
                ObjectType.FOOD,
                ObjectType.WEAPON,
                ObjectType.ARMOR,
                ObjectType.STAIRS,
                ObjectType.GOLD,
                ObjectType.AMULET
        };

        if (Global.level >= Const.AMULETLEVEL) {
            i = Util.rnd(thing_list.length);
        } else {
            i = Util.rnd(thing_list.length - 1);
        }
        return thing_list[i];
    }

    /*
     * spread:
     *	Give a spread around a given number (+/- 20%)
     */
    static int spread(int nm) {
        return nm - nm / 20 + Util.rnd(nm / 10);
    }

    /*
     * vowelstr:
     *	For printfs: if string starts with a vowel, return "n" for an
     *	"an".
     */
    static char vowelstr(String str) {
        switch (str.charAt(0)) {
            case 'a':
            case 'A':
            case 'e':
            case 'E':
            case 'i':
            case 'I':
            case 'o':
            case 'O':
            case 'u':
            case 'U':
                return 'n';
            default:
                return Character.MIN_VALUE;
        }
    }

    /*
     * look:
     *	A quick glance all around the player
     */
    static void look(boolean wakeup) {
        boolean DEBUG = false;
        int x, y;
        ObjectType ch;
        ThingImp tp;
        Place pp;
        Room rp;
        int ey, ex;
        int passcount;
        int pfl, fp;
        ObjectType pch;
        int sy, sx, sumhero = 0, diffhero = 0;
        if (DEBUG) {
            boolean done = false;

            if (done) {
                return;
            }
            done = true;
        } /* DEBUG */
        passcount = 0;
        rp = Global.player.t_room;
        if (!Global.oldpos.equals(Global.player._t_pos)) {
            erase_lamp(Global.oldpos, Global.oldrp);
            Global.oldpos = Global.player._t_pos;
            Global.oldrp = rp;
        }
        ey = Global.player._t_pos.y + 1;
        ex = Global.player._t_pos.x + 1;
        sx = Global.player._t_pos.x - 1;
        sy = Global.player._t_pos.y - 1;
        if (Global.door_stop && !Global.firstmove && Global.running) {
            sumhero = Global.player._t_pos.y + Global.player._t_pos.x;
            diffhero = Global.player._t_pos.y - Global.player._t_pos.x;
        }
        pp = Util.INDEX(Global.player._t_pos.y, Global.player._t_pos.x);
        pch = pp.p_ch;
        pfl = pp.p_flags;

        for (y = sy; y <= ey; y++)
            if (y > 0 && y < Const.NUMLINES - 1) for (x = sx; x <= ex; x++) {
                if (x < 0 || x >= Const.NUMCOLS) {
                    continue;
                }
                if (!Global.player.containsState(StateEnum.ISBLIND)) {
                    if (y == Global.player._t_pos.y && x == Global.player._t_pos.x)
                        continue;
                }

                pp = Util.INDEX(y, x);
                ch = pp.p_ch;
                if (ch.getValue() == ObjectType.Blank.getValue()) {
                    /* nothing need be done with a ' ' */
                    continue;
                }
                fp = pp.p_flags;
                if (pch != ObjectType.DOOR && ch != ObjectType.DOOR) {
                    if ((pfl & Const.F_PASS) != (fp & Const.F_PASS)) {
                        continue;
                    }
                }
                if (((fp & Const.F_PASS) != 0 || ch == ObjectType.DOOR) &&
                        ((pfl & Const.F_PASS) != 0 || pch == ObjectType.DOOR)) {
                    if (Global.player._t_pos.x != x && Global.player._t_pos.y != y &&
                            !IOUtil.step_ok(Global.places.get((Global.player._t_pos.x << 5) + y).p_ch) &&
                            !IOUtil.step_ok(Global.places.get((x << 5) + Global.player._t_pos.y).p_ch)) {
                        continue;
                    }
                }

                if ((tp = pp.p_monst) == null) {
                    ch = trip_ch(y, x, ch);
                } else if (Global.player.containsState(StateEnum.SEEMONST) && tp.containsState(StateEnum.ISINVIS)) {
                    if (Global.door_stop && !Global.firstmove) {
                        Global.running = false;
                    }
                    continue;
                } else {
                    if (wakeup)
                        Monst.wake_monster(y, x);
                    if (Chase.see_monst(tp)) {
                        if (Global.player.containsState(StateEnum.ISHALU))
                            ch = ObjectType.get((char) (Util.rnd(26) + 'A'));
                        else
                            ch = ObjectType.get((char) tp._t_disguise);
                    }
                }
                if (Global.player.containsState(StateEnum.ISBLIND) && (y != Global.player._t_pos.y || x != Global.player._t_pos.x)) {
                    continue;
                }

                Display.move(y, x);

                if (Global.player.t_room.containInfo(RoomInfoEnum.ISDARK) &&
                        !Global.see_floor && ch == ObjectType.FLOOR) {
                    ch = ObjectType.Blank;
                }

                if (tp != null || ch.getValue() != Util.CCHAR(Display.inch())) {
                    Display.addch(ch.getValue());
                }

                if (Global.door_stop && !Global.firstmove && Global.running) {
                    switch ((char) Global.runch) {
                        case 'h':
                            if (x == ex)
                                continue;
                            break;
                        case 'j':
                            if (y == sy)
                                continue;
                            break;
                        case 'k':
                            if (y == ey) {
                                continue;
                            }
                            break;
                        case 'l':
                            if (x == sx)
                                continue;
                            break;
                        case 'y':
                            if ((y + x) - sumhero >= 1)
                                continue;
                            break;
                        case 'u':
                            if ((y - x) - diffhero >= 1)
                                continue;
                            break;
                        case 'n':
                            if ((y + x) - sumhero <= -1)
                                continue;
                            break;
                        case 'b':
                            if ((y - x) - diffhero <= -1)
                                continue;
                    }
                    switch (ch) {
                        case DOOR:
                            if (x == Global.player._t_pos.x || y == Global.player._t_pos.y) {
                                Global.running = false;
                            }
                            break;
                        case PASSAGE:
                            if (x == Global.player._t_pos.x || y == Global.player._t_pos.y) {
                                passcount++;
                            }
                            break;
                        case FLOOR:
                        case Vert:
                        case Horizon:
                        case Blank:
                            break;
                        default:
                            Global.running = false;
                            break;
                    }
                }
            }
        if (Global.door_stop && !Global.firstmove && passcount > 1)
            Global.running = false;
        if (!Global.running || !Global.jump)
            Display.mvaddch(Global.player._t_pos.y, Global.player._t_pos.x, ObjectType.PLAYER.getValue());
        if (DEBUG) {
            boolean done = false;
        } /* DEBUG */
    }

    /*
     * trip_ch:
     *	Return the character appropriate for this space, taking into
     *	account whether or not the player is tripping.
     */
    static ObjectType trip_ch(int y, int x, ObjectType ch) {
        if (Global.player.containsState(StateEnum.ISHALU) && Global.after)
            switch (ch) {
                case FLOOR:
                case Blank:
                case PASSAGE:
                case Horizon:
                case Vert:
                case DOOR:
                case TRAP:
                    break;
                default:
                    if (y != Global.stairs.y || x != Global.stairs.x || !Global.seenstairs) {
                        ch = rnd_thing();
                    }
                    break;
            }
        return ch;
    }

    /*
     * erase_lamp:
     *	Erase the area shown by a lamp in a dark room.
     */
    static void erase_lamp(Coord pos, Room rp) {
        int y, x, ey, sy, ex;

        if (!(Global.see_floor && rp.containInfo(RoomInfoEnum.ISDARK)
                && !rp.containInfo(RoomInfoEnum.ISGONE)
                && !Global.player.containsState(StateEnum.ISBLIND))) {
            return;
        }

        ey = pos.y + 1;
        ex = pos.x + 1;
        sy = pos.y - 1;
        for (x = pos.x - 1; x <= ex; x++) {
            for (y = sy; y <= ey; y++) {
                if (y == Global.player._t_pos.y && x == Global.player._t_pos.x) {
                    continue;
                }
                Display.move(y, x);
                if (Util.CCHAR(Display.inch()) == ObjectType.FLOOR.getValue())
                    Display.addch(' ');
            }
        }
    }

    /*
     * chg_str:
     *	used to modify the playes strength.  It keeps track of the
     *	highest it has been, just in case
     */
    static void chg_str(int amt) {
        int comp;

        if (amt == 0) {
            return;
        }
        Global.player._t_stats.s_str = add_str(Global.player._t_stats.s_str, amt);
        comp = Global.player._t_stats.s_str;
        if (Util.ISRING(Const.LEFT, RingEnum.R_ADDSTR))
            comp = add_str(comp, -Global.cur_ring[Const.LEFT]._o_arm);
        if (Util.ISRING(Const.RIGHT, RingEnum.R_ADDSTR))
            comp = add_str(comp, -Global.cur_ring[Const.RIGHT]._o_arm);
        if (comp > Global.max_stats.s_str)
            Global.max_stats.s_str = comp;
    }

    /*
     * add_str:
     *	Perform the actual add, checking upper and lower bound limits
     */
    static int add_str(int sp, int amt) {
        if ((sp += amt) < 3) {
            sp = 3;
        } else if (sp > 31) {
            sp = 31;
        }
        return sp;
    }

    /*
     str str:
     *	Choose the first or second string depending on whether it the
     *	player is tripping
     */
    static String choose_str(String ts, String ns) {
        return (Global.player.containsState(StateEnum.ISHALU) ? ts : ns);
    }

    /*
     * check_level:
     *	Check to see if the guy has gone up a level.
     */
    static void check_level() {
        int i;

        for (i = 0; Global.e_levels[i] != 0; i++) {
            if (Global.e_levels[i] > Global.player._t_stats.s_exp) {
                break;
            }
        }
        i++;
        int olevel = Global.player._t_stats.s_lvl;
        Global.player._t_stats.s_lvl = i;
        if (i > olevel) {
            int add = Dice.roll(i - olevel, 10);
            Global.player._t_stats.s_maxhp += add;
            Global.player._t_stats.s_hpt += add;
            IOUtil.msg("welcome to level %d", String.valueOf(i));
        }
    }

    /*
     * add_haste:
     *	Add a haste to the player
     */
    static boolean add_haste(boolean potion) {
        try {
            Method m = Daemons.class.getMethod("nohaste");

            if (Global.player.containsState(StateEnum.ISHASTE)) {
                Global.no_command += Util.rnd(8);
                Global.player.removeState(StateEnum.ISRUN);
                Global.player.removeState(StateEnum.ISHASTE);
                Daemon.extinguish(m);
                IOUtil.msg("you faint from exhaustion");
                return false;
            } else {
                Global.player.addState(StateEnum.ISHASTE);
                if (potion) {
                    Daemon.fuse(m, 0, Util.rnd(4) + 4, Const.AFTER);
                }
                return true;
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /*
     * call_it:
     *	Call an object something after use.
     */
    static void call_it(Obj_info info) {
        if (info.oi_know) {
            if (info.oi_guess != null) {
                // free(info.oi_guess);
                info.oi_guess = null;
            }
        } else if (info.oi_guess == null) {
            IOUtil.msg(Global.terse ? "call it: " : "what do you want to call it? ");
//            if (get_str(prbuf, stdscr) == 0) {
//                if (info.oi_guess != null) {
//                    // free(info.oi_guess);
//                }
//                info.oi_guess = malloc(strlen(prbuf) + 1);
//                if (info.oi_guess != null) {
//                    strcpy(info.oi_guess, prbuf);
//                }
//            }
            IOUtil.msg("");
        }
    }

    /*
     * show_floor:
     *	Should we show the floor in her room at this time?
     */
    static boolean show_floor() {
        if (Global.player.t_room.containInfo(RoomInfoEnum.ISDARK)
                && !Global.player.t_room.containInfo(RoomInfoEnum.ISGONE)
                && !Global.player.containsState(StateEnum.ISBLIND)) {
            return Global.see_floor;
        } else {
            return true;
        }
    }

    /*
     * is_current:
     *	See if the object is one of the currently used items
     */
    static boolean is_current(Thing obj) {
        if (obj == null) {
            return false;
        }
        if (obj == Global.cur_armor || obj == Global.cur_weapon
                || obj == Global.cur_ring[Const.LEFT]
                || obj == Global.cur_ring[Const.RIGHT]) {
            if (!Global.terse) {
                IOUtil.addmsg("That's already ");
            }
            IOUtil.msg("in use");
            return true;
        }
        return false;
    }

    /*
     * aggravate:
     *	Aggravate all the monsters on this level
     */
    static void aggravate() {
        for (ThingImp mp : Global.mlist) {
            Chase.runto(mp._t_pos);
        }
    }

    /*
     * find_obj:
     *	Find the unclaimed object at y, x
     */
    static ThingImp find_obj(int y, int x) {
        for (ThingImp obj : Global.lvl_obj) {
            if (obj._o_pos.y == y && obj._o_pos.x == x) {
                return obj;
            }
        }
        boolean MASTER = false;
        if (MASTER) {
            Global.prbuf = "Non-object " + y + "," + x;
            IOUtil.msg(Global.prbuf);
        } else {
            /* NOTREACHED */
        }
        return null;
    }

    /*
     * sign:
     *	Return the sign of the number
     */
    static int sign(int nm) {
        if (nm < 0) {
            return -1;
        } else {
            return (nm > 0 ? 1 : 0);
        }
    }

    /*
     * eat:
     *	She wants to eat something, so let her try
     */
    static void eat() {
        ThingImp obj;

        if ((obj = Pack.get_item("eat", ObjectType.FOOD)) == null) {
            return;
        }
        if (obj._o_type != ObjectType.FOOD) {
            if (!Global.terse) {
                IOUtil.msg("ugh, you would get ill if you ate that");
            } else {
                IOUtil.msg("that's Inedible!");
            }
            return;
        }
        if (Global.food_left < 0) {
            Global.food_left = 0;
        }
        if ((Global.food_left += Const.HUNGERTIME - 200 + Util.rnd(400)) > Const.STOMACHSIZE) {
            Global.food_left = Const.STOMACHSIZE;
        }
        Global.hungry_state = 0;
        if (obj == Global.cur_weapon) {
            Global.cur_weapon = null;
        }
        if (obj._o_which == 1)
            IOUtil.msg("my, that was a yummy %s", Global.fruit);
        else if (Util.rnd(100) > 70) {
            Global.player._t_stats.s_exp++;
            IOUtil.msg("%s, this food tastes awful", choose_str("bummer", "yuk"));
            check_level();
        } else {
            IOUtil.msg("%s, that tasted good", choose_str("oh, wow", "yum"));
        }
        Pack.leave_pack(obj, false, false);
    }

    /*
     * get_dir:
     *	Set up the direction co_ordinate for use in varios "prefix"
     *	commands
     */
    static boolean get_dir() {
        String prompt;
        boolean gotit;
        Coord last_delt = new Coord(0, 0);

        if (Global.again && Global.last_dir != '\0') {
            Global.delta.y = last_delt.y;
            Global.delta.x = last_delt.x;
            Global.dir_ch = Global.last_dir;
        } else {
            if (!Global.terse) {
                IOUtil.msg(prompt = "which direction? ");
            } else {
                prompt = "direction: ";
            }
            do {
                gotit = true;
                switch (Global.dir_ch = IOUtil.readchar()) {
                    case 'h':
                    case 'H':
                        Global.delta.y = 0;
                        Global.delta.x = -1;
                        break;
                    case 'j':
                    case 'J':
                        Global.delta.y = 1;
                        Global.delta.x = 0;
                        break;
                    case 'k':
                    case 'K':
                        Global.delta.y = -1;
                        Global.delta.x = 0;
                        break;
                    case 'l':
                    case 'L':
                        Global.delta.y = 0;
                        Global.delta.x = 1;
                        break;
                    case 'y':
                    case 'Y':
                        Global.delta.y = -1;
                        Global.delta.x = -1;
                        break;
                    case 'u':
                    case 'U':
                        Global.delta.y = -1;
                        Global.delta.x = 1;
                        break;
                    case 'b':
                    case 'B':
                        Global.delta.y = 1;
                        Global.delta.x = -1;
                        break;
                    case 'n':
                    case 'N':
                        Global.delta.y = 1;
                        Global.delta.x = 1;
                        break;
                    case Const.ESCAPE:
                        Global.last_dir = '\0';
                        Pack.reset_last();
                        IOUtil.msg("");
                        return false;
                    default:
                        Global.mpos = 0;
                        IOUtil.msg(prompt);
                        gotit = false;
                }
            } while (!gotit);
            if (Character.isUpperCase(Global.dir_ch)) {
                Global.dir_ch = Character.toLowerCase(Global.dir_ch);
            }
            Global.last_dir = (char) Global.dir_ch;
            last_delt.y = Global.delta.y;
            last_delt.x = Global.delta.x;
        }
        if (Global.player.containsState(StateEnum.ISHUH) && Util.rnd(5) == 0) {
            do {
                Global.delta.y = Util.rnd(3) - 1;
                Global.delta.x = Util.rnd(3) - 1;
            } while (Global.delta.y == 0 && Global.delta.x == 0);
        }
        Global.mpos = 0;
        IOUtil.msg("");
        return true;
    }

}
