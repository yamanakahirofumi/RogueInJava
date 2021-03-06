package org.hiro;

import org.hiro.character.Human;
import org.hiro.character.Player;
import org.hiro.character.StateEnum;
import org.hiro.map.AbstractCoordinate;
import org.hiro.map.Coordinate;
import org.hiro.map.RoomInfoEnum;
import org.hiro.output.Display;
import org.hiro.things.ObjectType;
import org.hiro.things.OriginalMonster;
import org.hiro.things.Thing;
import org.hiro.things.ringtype.AddStrengthRing;

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
        Player player = Human.instance;
        boolean DEBUG = false;
        if (DEBUG) {
            boolean done = false;

            if (done) {
                return;
            }
            done = true;
        } /* DEBUG */
        int passcount = 0;
        Room rp = player.getRoom();
        if (!Global.oldpos.equals(player.getPosition())) {
            erase_lamp(Global.oldpos, Global.oldrp);
            Global.oldpos = player.getPosition();
            Global.oldrp = rp;
        }
        int ey = player.getPositionY() + 1;
        int ex = player.getPositionX() + 1;
        int sx = player.getPositionX() - 1;
        int sy = player.getPositionY() - 1;
        int sumhero = 0;
        int diffhero = 0;
        if (Global.door_stop && !Global.firstmove && Global.running) {
            sumhero = player.getPositionY() + player.getPositionX();
            diffhero = player.getPositionY() - player.getPositionX();
        }
        Place pp = Util.getPlace(player.getPosition());
        ObjectType pch = pp.p_ch;
        int pfl = pp.p_flags;

        for (int y = sy; y <= ey; y++) {
            if (y > 0 && y < Const.NUMLINES - 1) {
                for (int x = sx; x <= ex; x++) {
                    if (x < 0 || x >= Const.NUMCOLS) {
                        continue;
                    }
                    AbstractCoordinate target = new Coordinate(x, y);
                    if (!player.containsState(StateEnum.ISBLIND)) {
                        if (player.getPosition().equals(target)) {
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
                        if (player.getPositionX() != x && player.getPositionY() != y &&
                                !IOUtil.step_ok(Util.INDEX(y, player.getPositionX()).p_ch) &&
                                !IOUtil.step_ok(Util.INDEX(player.getPositionY(), x).p_ch)) {
                            continue;
                        }
                    }

                    OriginalMonster tp;
                    if ((tp = pp.p_monst) == null) {
                        ch = trip_ch(target, ch);
                    } else if (player.containsState(StateEnum.SEEMONST) && tp.containsState(StateEnum.ISINVIS)) {
                        if (Global.door_stop && !Global.firstmove) {
                            Global.running = false;
                        }
                        continue;
                    } else {
                        if (wakeup)
                            Monst.wake_monster(y, x);
                        if (Chase.see_monst(tp)) {
                            if (player.containsState(StateEnum.ISHALU))
                                ch = ObjectType.get((char) (Util.rnd(26) + 'A'));
                            else
                                ch = ObjectType.get((char) tp.getDisplayTile());
                        }
                    }
                    if (player.containsState(StateEnum.ISBLIND) && (!player.getPosition().equals(target))) {
                        continue;
                    }

                    Display.move(y, x);

                    if (player.getRoom().containInfo(RoomInfoEnum.ISDARK) &&
                            !Global.see_floor && ch == ObjectType.FLOOR) {
                        ch = ObjectType.Blank;
                    }

                    if (tp != null || ch.getValue() != Util.CCHAR(Display.inch())) {
                        Display.addch(ch.getValue());
                    }

                    if (Global.door_stop && !Global.firstmove && Global.running) {
                        switch (Global.runch) {
                            case 'h':
                                if (x == ex) {
                                    continue;
                                }
                                break;
                            case 'j':
                                if (y == sy) {
                                    continue;
                                }
                                break;
                            case 'k':
                                if (y == ey) {
                                    continue;
                                }
                                break;
                            case 'l':
                                if (x == sx) {
                                    continue;
                                }
                                break;
                            case 'y':
                                if ((y + x) - sumhero >= 1) {
                                    continue;
                                }
                                break;
                            case 'u':
                                if ((y - x) - diffhero >= 1) {
                                    continue;
                                }
                                break;
                            case 'n':
                                if ((y + x) - sumhero <= -1) {
                                    continue;
                                }
                                break;
                            case 'b':
                                if ((y - x) - diffhero <= -1) {
                                    continue;
                                }
                        }
                        switch (ch) {
                            case DOOR:
                                if (x == player.getPositionX() || y == player.getPositionY()) {
                                    Global.running = false;
                                }
                                break;
                            case PASSAGE:
                                if (x == player.getPositionX() || y == player.getPositionY()) {
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
            }
        }
        if (Global.door_stop && !Global.firstmove && passcount > 1)
            Global.running = false;
        if (!Global.running || !Global.jump)
            Display.mvaddch(Global.player._t_pos, ObjectType.PLAYER.getValue());
        if (DEBUG) {
            boolean done = false;
        } /* DEBUG */
    }

    /*
     * trip_ch:
     *	Return the character appropriate for this space, taking into
     *	account whether or not the player is tripping.
     */
    static ObjectType trip_ch(AbstractCoordinate target, ObjectType ch) {
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
    static void erase_lamp(AbstractCoordinate pos, Room rp) {

        if (!(Global.see_floor && rp.containInfo(RoomInfoEnum.ISDARK)
                && !rp.containInfo(RoomInfoEnum.ISGONE)
                && !Human.instance.containsState(StateEnum.ISBLIND))) {
            return;
        }

        int ey = pos.getY() + 1;
        int ex = pos.getX() + 1;
        int sy = pos.getY() - 1;
        for (int x = pos.getX() - 1; x <= ex; x++) {
            for (int y = sy; y <= ey; y++) {
                AbstractCoordinate tmp = new Coordinate(x, y);
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
    public static void chg_str(int amt) {

        if (amt == 0) {
            return;
        }
        Global.player.getStatus().s_str = add_str(Human.instance.getCurrentStrength(), amt);
        int comp = Human.instance.getCurrentStrength();
        if (Global.cur_ring[Const.LEFT] instanceof AddStrengthRing) {
            comp = add_str(comp, -((AddStrengthRing) Global.cur_ring[Const.LEFT]).getStrength());
        }
        if (Global.cur_ring[Const.RIGHT] instanceof AddStrengthRing) {
            comp = add_str(comp, -((AddStrengthRing) Global.cur_ring[Const.RIGHT]).getStrength());
        }
        if (comp > Human.instance.getMaxStrength()) {
            Global.max_stats.s_str = comp;
        }
    }

    /*
     * add_str:
     *	Perform the actual add, checking upper and lower bound limits
     */
    public static int add_str(int sp, int amt) {
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
    public static String choose_str(String ts, String ns) {
        return (Human.instance.containsState(StateEnum.ISHALU) ? ts : ns);
    }

    /*
     * check_level:
     *	Check to see if the guy has gone up a level.
     */
    public static void check_level() {
        int i;

        for (i = 0; Global.e_levels[i] != 0; i++) {
            if (Global.e_levels[i] > Global.player.getStatus().s_exp) {
                break;
            }
        }
        i++;
        int olevel = Global.player.getStatus().s_lvl;
        Global.player.getStatus().s_lvl = i;
        if (i > olevel) {
            int add = Dice.roll(i - olevel, 10);
            Global.player.getStatus().s_maxhp += add;
            Human.instance.addHp(add);
            IOUtil.msg("welcome to level %d", String.valueOf(i));
        }
    }

    /*
     * add_haste:
     *	Add a haste to the player
     */
    public static boolean add_haste(boolean potion) {
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
        if (Human.instance.getRoom().containInfo(RoomInfoEnum.ISDARK)
                && !Human.instance.getRoom().containInfo(RoomInfoEnum.ISGONE)
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
    public static void aggravate() {
        for (OriginalMonster mp : Global.mlist) {
            Chase.runto(mp.getPosition());
        }
    }

    /*
     * find_obj:
     *	Find the unclaimed object at y, x
     */
    public static Thing find_obj(AbstractCoordinate coordinate) {
        for (Thing obj : Global.lvl_obj) {
            if (obj.getOPos().equals(coordinate)) {
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
     * get_dir:
     *	Set up the direction co_ordinate for use in varios "prefix"
     *	commands
     */
    public static boolean get_dir() {
        AbstractCoordinate last_delt = new Coordinate(0, 0);

        if (Global.again && Global.last_dir != '\0') {
            Global.delta = new Coordinate(last_delt);
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
                        Global.delta = new Coordinate(-1, 0);
                        break;
                    case 'j':
                    case 'J':
                        Global.delta = new Coordinate(0, 1);
                        break;
                    case 'k':
                    case 'K':
                        Global.delta = new Coordinate(0, -1);
                        break;
                    case 'l':
                    case 'L':
                        Global.delta = new Coordinate(1, 0);
                        break;
                    case 'y':
                    case 'Y':
                        Global.delta = new Coordinate(-1, -1);
                        break;
                    case 'u':
                    case 'U':
                        Global.delta = new Coordinate(-1, -1);
                        break;
                    case 'b':
                    case 'B':
                        Global.delta = new Coordinate(-1, 1);
                        break;
                    case 'n':
                    case 'N':
                        Global.delta = new Coordinate(1, 1);
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
            last_delt = new Coordinate(Global.delta);
        }
        if (Human.instance.containsState(StateEnum.ISHUH) && Util.rnd(5) == 0) {
            do {
                Global.delta = new Coordinate(Util.rnd(3) - 1, Util.rnd(3) - 1);
            } while (Global.delta.equals(new Coordinate(0, 0)));
        }
        Global.mpos = 0;
        IOUtil.msg("");
        return true;
    }

}
