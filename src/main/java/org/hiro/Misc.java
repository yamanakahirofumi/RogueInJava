package org.hiro;

import org.hiro.character.Human;
import org.hiro.character.StateEnum;
import org.hiro.map.AbstractCoordinate;
import org.hiro.map.Coordinate;
import org.hiro.map.RoomInfoEnum;
import org.hiro.output.Display;
import org.hiro.things.Food;
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

        if (Human.instance.getLevel() >= Const.AMULETLEVEL) {
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
        int passcount;
        if (DEBUG) {
            boolean done = false;

            if (done) {
                return;
            }
            done = true;
        } /* DEBUG */
        passcount = 0;
        Room rp = Global.player.t_room;
        if (!Global.oldpos.equals(Global.player._t_pos)) {
            erase_lamp(Global.oldpos, Global.oldrp);
            Global.oldpos = Global.player._t_pos;
            Global.oldrp = rp;
        }
        int ey = Global.player._t_pos.y + 1;
        int ex = Global.player._t_pos.x + 1;
        int sx = Global.player._t_pos.x - 1;
        int sy = Global.player._t_pos.y - 1;
        int sumhero = 0;
        int diffhero = 0;
        if (Global.door_stop && !Global.firstmove && Global.running) {
            sumhero = Global.player._t_pos.y + Global.player._t_pos.x;
            diffhero = Global.player._t_pos.y - Global.player._t_pos.x;
        }
        Place pp = Util.getPlace(Global.player._t_pos);
        ObjectType pch = pp.p_ch;
        int pfl = pp.p_flags;

        for (int y = sy; y <= ey; y++)
            if (y > 0 && y < Const.NUMLINES - 1) for (int x = sx; x <= ex; x++) {
                if (x < 0 || x >= Const.NUMCOLS) {
                    continue;
                }
                Coordinate target = new Coordinate(x, y);
                if (!Human.instance.containsState(StateEnum.ISBLIND)) {
                    if (Global.player._t_pos.equals(target)) {
                        continue;
                    }
                }

                pp = Util.INDEX(y, x);
                ObjectType ch = pp.p_ch;
                if (ch.getValue() == ObjectType.Blank.getValue()) {
                    /* nothing need be done with a ' ' */
                    continue;
                }
                int fp = pp.p_flags;
                if (pch != ObjectType.DOOR && ch != ObjectType.DOOR) {
                    if ((pfl & Const.F_PASS) != (fp & Const.F_PASS)) {
                        continue;
                    }
                }
                if (((fp & Const.F_PASS) != 0 || ch == ObjectType.DOOR) &&
                        ((pfl & Const.F_PASS) != 0 || pch == ObjectType.DOOR)) {
                    if (Global.player._t_pos.x != x && Global.player._t_pos.y != y &&
                            !IOUtil.step_ok(Util.INDEX(y, Global.player._t_pos.x).p_ch) &&
                            !IOUtil.step_ok(Util.INDEX(Global.player._t_pos.y, x).p_ch)) {
                        continue;
                    }
                }

                ThingImp tp;
                if ((tp = pp.p_monst) == null) {
                    ch = trip_ch(target, ch);
                } else if (Human.instance.containsState(StateEnum.SEEMONST) && tp.containsState(StateEnum.ISINVIS)) {
                    if (Global.door_stop && !Global.firstmove) {
                        Global.running = false;
                    }
                    continue;
                } else {
                    if (wakeup)
                        Monst.wake_monster(y, x);
                    if (Chase.see_monst(tp)) {
                        if (Human.instance.containsState(StateEnum.ISHALU))
                            ch = ObjectType.get((char) (Util.rnd(26) + 'A'));
                        else
                            ch = ObjectType.get((char) tp._t_disguise);
                    }
                }
                if (Human.instance.containsState(StateEnum.ISBLIND) && (!Global.player._t_pos.equals(target))) {
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
    static ObjectType trip_ch(Coordinate target, ObjectType ch) {
        if (Human.instance.containsState(StateEnum.ISHALU) && Global.after)
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
                    if (!Global.stairs.equals(target) || !Global.seenstairs) {
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
    static void erase_lamp(Coordinate pos, Room rp) {

        if (!(Global.see_floor && rp.containInfo(RoomInfoEnum.ISDARK)
                && !rp.containInfo(RoomInfoEnum.ISGONE)
                && !Human.instance.containsState(StateEnum.ISBLIND))) {
            return;
        }

        int ey = pos.y + 1;
        int ex = pos.x + 1;
        int sy = pos.y - 1;
        for (int x = pos.x - 1; x <= ex; x++) {
            for (int y = sy; y <= ey; y++) {
                Coordinate tmp = new Coordinate(x, y);
                if (Global.player._t_pos.equals(tmp)) {
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

        if (amt == 0) {
            return;
        }
        Global.player._t_stats.s_str = add_str(Human.instance.getCurrentStrength(), amt);
        int comp = Human.instance.getCurrentStrength();
        if (Util.ISRING(Const.LEFT, RingEnum.R_ADDSTR)) {
            comp = add_str(comp, -Global.cur_ring[Const.LEFT]._o_arm);
        }
        if (Util.ISRING(Const.RIGHT, RingEnum.R_ADDSTR)) {
            comp = add_str(comp, -Global.cur_ring[Const.RIGHT]._o_arm);
        }
        if (comp > Global.max_stats.s_str) {
            Global.max_stats.s_str = comp;
        }
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
        return (Human.instance.containsState(StateEnum.ISHALU) ? ts : ns);
    }

    /*
     * check_level:
     *	Check to see if the guy has gone up a level.
     */
    public static void check_level() {
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
            Human.instance.addHp(add);
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

            if (Human.instance.containsState(StateEnum.ISHASTE)) {
                Global.no_command += Util.rnd(8);
                Human.instance.removeState(StateEnum.ISRUN);
                Human.instance.removeState(StateEnum.ISHASTE);
                Daemon.extinguish(m);
                IOUtil.msg("you faint from exhaustion");
                return false;
            } else {
                Human.instance.addState(StateEnum.ISHASTE);
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
     * show_floor:
     *	Should we show the floor in her room at this time?
     */
    static boolean show_floor() {
        if (Global.player.t_room.containInfo(RoomInfoEnum.ISDARK)
                && !Global.player.t_room.containInfo(RoomInfoEnum.ISGONE)
                && !Human.instance.containsState(StateEnum.ISBLIND)) {
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
        if (Human.instance.isEquipped(obj)) {
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
    static ThingImp find_obj(AbstractCoordinate coordinate) {
        for (ThingImp obj : Global.lvl_obj) {
            if (obj._o_pos.equals(coordinate)) {
                return obj;
            }
        }
        boolean MASTER = false;
        if (MASTER) {
            Global.prbuf = "Non-object " + coordinate.toString();
            // Global.prbuf = "Non-object " + y + "," + x;
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
    static void eat(ThingImp obj) {
        if (obj == null) {
            return;
        }
        if (!(obj instanceof Food)) {
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
        if ((Global.food_left += Const.HUNGERTIME - 200 + Util.rnd(400)) > Human.instance.getStomachSize()) {
            Global.food_left = Human.instance.getStomachSize();
        }
        Global.hungry_state = 0;
        // 装備している武器を食べる場合
        Human.instance.removeWeapon(obj);
        if (obj._o_which == 1)
            IOUtil.msg("my, that was a yummy %s", Global.fruit);
        else if (Util.rnd(100) > 70) {
            Human.instance.addExperience(1);
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
        Coordinate last_delt = new Coordinate(0, 0);

        if (Global.again && Global.last_dir != '\0') {
            Global.delta.y = last_delt.y;
            Global.delta.x = last_delt.x;
            Global.dir_ch = Global.last_dir;
        } else {
            String prompt;
            if (!Global.terse) {
                IOUtil.msg(prompt = "which direction? ");
            } else {
                prompt = "direction: ";
            }
            boolean gotit;
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
        if (Human.instance.containsState(StateEnum.ISHUH) && Util.rnd(5) == 0) {
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
