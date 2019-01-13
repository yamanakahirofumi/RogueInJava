package org.hiro;

import org.hiro.output.Display;
import org.hiro.things.ObjectType;
import org.hiro.things.RingEnum;
import org.hiro.things.Thing;
import org.hiro.things.ThingImp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ThingMethod {

    /*
     * discovered:
     *	list what the player has discovered in this game of a certain type
     */
    static int line_cnt = 0;
    static boolean newpage = false;
    static String lastfmt;
    static String[] lastarg;


    /*
     * pick_one:
     *	Pick an item out of a list of nitems possible objects
     */
    public static int pick_one(List<Obj_info> info, int nitems) {

        int rnd = Util.rnd(100);
        int j;
        for (j = 0; j < nitems; j++) {
            if (rnd < info.get(j).getProbability()) {
                break;
            }
        }
        if (j == nitems) {
            boolean MASTER = false;
            if (MASTER) {
                if (Global.wizard) {
                    // msg("bad pick_one: %d from %d items", i, nitems);
                    // for (info = start; info < end; info++)
                    // msg("%s: %d%%", info . oi_name, info . oi_prob);
                }
            }
            j = 0;
        }

        return j;
    }

    /*
     * end_line:
     *	End the list of lines
     */
    static void end_line() {
        if (Global.inv_type != Const.INV_SLOW) {
            if (line_cnt == 1 && !newpage) {
                Global.mpos = 0;
                // IOUtil.msg(lastfmt, lastarg);
            } else {
                add_line("", "");
                IOUtil.msg("");
            }
        }
        line_cnt = 0;
        newpage = false;
    }


    /*
     * add_line:
     *	Add a line to the list of discoveries
     */
    /* VARARGS1 */
    static int add_line(String fmt, String... arg) {
        // WINDOW *tw, *sw;

        if (line_cnt == 0) {
            // Display.wclear(hw);
            if (Global.inv_type == Const.INV_SLOW) {
                Global.mpos = 0;
            }
        }
        if (Global.inv_type == Const.INV_SLOW) {
            if (fmt != null && fmt.length() != 0) {
                if (IOUtil.msg(fmt,(Object) arg) == Const.ESCAPE) {
                    return Const.ESCAPE;
                }
            }
            line_cnt++;
        } else {
            String prompt = "--Press space to continue--";
            int maxlen = -1;
            if (maxlen < 0) {
                maxlen = prompt.length();
            }
            int x = 0;
            if (line_cnt >= Display.LINES - 1 || fmt == null) {
                if (Global.inv_type == Const.INV_OVER && fmt == null && !newpage) {
                    IOUtil.msg("");
                    Display.refresh();
                    // tw = Display.newwin(line_cnt + 1, maxlen + 2, 0, Display.COLS - maxlen - 3);
                    // sw = Display.subwin(tw, line_cnt + 1, maxlen + 1, 0, Display.COLS - maxlen - 2);
                    for (int y = 0; y <= line_cnt; y++) {
                        // Display.wmove(sw, y, 0);
                        for (x = 0; x <= maxlen; x++) {
                            // Display.waddch(sw, Display.mvwinch(hw, y, x));
                        }
                    }
                    // Display.wmove(tw, line_cnt, 1);
//                    waddstr(tw, prompt);
                    /*
                     * if there are lines below, use 'em
                     */
                    if (Display.LINES > Const.NUMLINES) {
                        if (Const.NUMLINES + line_cnt > Display.LINES) {
//                            mvwin(tw, Display.LINES - (line_cnt + 1), Display.COLS - maxlen - 3);
                        }
                    } else {
//                            mvwin(tw, Const.NUMLINES, 0);}
                    }
//                    touchwin(tw);
//                    wrefresh(tw);
//                    wait_for(tw, ' ');
                    if (Mdport.md_hasclreol()) {
//                        werase(tw);
//                        leaveok(tw, true);
//                        wrefresh(tw);
                    }
                    // Display.delwin(tw);
                    // Display.touchwin(stdscr);
                } else {
                    // Display.wmove(hw, Display.LINES - 1, 0);
//                    waddstr(hw, prompt);
//                    wrefresh(hw);
//                    wait_for(hw, ' ');
                    // Display.clearok(curscr, true);
                    // Display.wclear(hw);
                    // Display.touchwin(stdscr);
                }
                newpage = true;
                line_cnt = 0;
                maxlen = prompt.length();
            }
            if (fmt != null && !(line_cnt == 0 && fmt.length() != 0)) {
//                mvwprintw(hw, line_cnt++, 0, fmt, arg);
//                getyx(hw, y, x);
                if (maxlen < x) {
                    maxlen = x;
                }
                lastfmt = fmt;
                lastarg = arg;
            }
        }
        return ~Const.ESCAPE;
    }

    /*
     * inv_name:
     *	Return the name of something as it would appear in an
     *	inventory.
     */
    static String inv_name(ThingImp obj, boolean drop) {
        String pb;
        Obj_info op;
        String sp;
        int which;

        boolean MASTER = false;

        pb = Global.prbuf;
        which = obj._o_which;
        switch (obj._o_type) {
            case POTION:
                try {
                    Method m = ThingMethod.class.getMethod("nullstr", ThingImp.class);
                    nameit(obj, "potion", Global.p_colors[which], Global.pot_info[which], m);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                break;
            case RING:
                try {
                    Method m = RingMethod.class.getMethod("ring_num", ThingImp.class);
                    nameit(obj, "ring", Global.r_stones[which], Global.ring_info[which], m);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                break;
            case STICK:
                try {
                    Method m = StickMethod.class.getMethod("charge_str", ThingImp.class);
                    nameit(obj, Global.ws_type[which], Global.ws_made[which], Global.ws_info[which], m);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                break;
            case SCROLL:
                if (obj._o_count == 1) {
                    Global.prbuf = "A scroll ";
                } else {
                    Global.prbuf = obj._o_count + " scrolls ";
                }
                op = Global.scr_info[which];
                if (op.isKnown()) {
                    Global.prbuf = Global.prbuf + "of " + op.getName();
                } else if (op.isTemporaryNamed()) {
                    Global.prbuf = Global.prbuf + "called " + op.getTemporaryName();
                } else {
                    Global.prbuf = Global.prbuf + "titled '" + Global.s_names[which] + "'";
                }
                break;
            case FOOD:
                if (which == 1) {
                    if (obj._o_count == 1) {
                        Global.prbuf = "A" + Misc.vowelstr(Global.fruit) + " " + Global.fruit;
                    } else {
                        Global.prbuf = obj._o_count + " " + Global.fruit + "s";
                    }
                } else if (obj._o_count == 1) {
                    Global.prbuf = "Some food";
                } else {
                    Global.prbuf = obj._o_count + " rations of food";
                }
                break;
            case WEAPON:
                sp = Global.weap_info[which].getName();
                if (obj._o_count > 1) {
                    Global.prbuf = obj._o_count + " ";
                } else {
                    Global.prbuf = "A" + Misc.vowelstr(sp) + " ";
                }
                if (obj.contains_o_flags(Const.ISKNOW)) {
                    Global.prbuf = Global.prbuf + WeaponMethod.num(obj._o_hplus, obj._o_dplus, ObjectType.WEAPON)
                            + " " + sp;
                } else {
                    Global.prbuf = Global.prbuf + sp;
                }
                if (obj._o_count > 1) {
                    Global.prbuf = Global.prbuf + "s";
                }
                if (obj._o_label != Character.MIN_VALUE) {
                    Global.prbuf = Global.prbuf + " called " + obj._o_label;
                }
                break;
            case ARMOR:
                sp = Global.arm_info[which].getName();
                if (obj.contains_o_flags(Const.ISKNOW)) {
                    Global.prbuf = WeaponMethod.num(Global.a_class[which] - obj._o_arm, 0, ObjectType.ARMOR)
                            + " " + sp + " [";
                    if (!Global.terse) {
                        Global.prbuf = Global.prbuf + "protection ";
                    }
                    Global.prbuf = Global.prbuf + (10 - obj._o_arm) + "]";
                } else {
                    Global.prbuf = sp;
                }
                if (obj._o_label != Character.MIN_VALUE) {
                    Global.prbuf = Global.prbuf + " called " + obj._o_label;
                }
                break;
            case AMULET:
                Global.prbuf = "The Amulet of Yendor";
                break;
            case GOLD:
                Global.prbuf = obj._o_arm + " Gold pieces";
                break;
            default:
                if (MASTER) {
                    // debug("Picked up something funny %s", Display.unctrl(obj._o_type));
                    // sprintf(pb, "Something bizarre %s", Display.unctrl(obj._o_type));
                }
        }
        if (Global.inv_describe) {
            if (obj == Global.cur_armor) {
                Global.prbuf = Global.prbuf + " (being worn)";
            }
            if (obj == Global.cur_weapon) {
                Global.prbuf = Global.prbuf + " (weapon in hand)";
            }
            if (obj == Global.cur_ring[Const.LEFT]) {
                Global.prbuf = Global.prbuf + " (on left hand)";
            } else if (obj == Global.cur_ring[Const.RIGHT]) {
                Global.prbuf = Global.prbuf + " (on right hand)";
            }
        }
        if (drop && Character.isUpperCase(Global.prbuf.charAt(0))) {
            Global.prbuf = Character.toLowerCase(Global.prbuf.charAt(0)) + Global.prbuf.substring(1);
        } else if (!drop && Character.isLowerCase(Global.prbuf.charAt(0))) {
            Global.prbuf = Character.toUpperCase(Global.prbuf.charAt(0)) + Global.prbuf.substring(1);
        }
        return Global.prbuf;
    }

    /*
     * nameit:
     *	Give the proper name to a potion, stick, or ring
     */
    static void nameit(ThingImp obj, String type, char which, Obj_info op, Method prfunc) {
        char pb;

        if (op.isKnown() || op.isTemporaryNamed()) {
            if (obj._o_count == 1) {
                Global.prbuf = "A " + type + " ";
            } else {
                Global.prbuf = obj._o_count + " " + type + " ";
            }
            try {
                if (op.isKnown()) {
                    Global.prbuf = Global.prbuf + "of " + op.getName() + prfunc.invoke(obj) + "(" + which + ")";
                } else if (op.isTemporaryNamed()) {
                    Global.prbuf = Global.prbuf + "called " + op.getTemporaryName() + prfunc.invoke(obj) + "(" + which + ")";
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else if (obj._o_count == 1) {
            Global.prbuf = "A" + Misc.vowelstr(String.valueOf(which)) + " " + which + " " + type;
        } else {
            Global.prbuf = obj._o_count + " " + which + " " + type;
        }
    }

    /*
     * nullstr:
     *	Return a pointer to a null-length string
     */
    static String nullstr(Thing ignored) {
        // NOOP(ignored);
        return "";
    }

    /*
     * dropcheck:
     *	Do special checks for dropping or unweilding|unwearing|unringing
     */
    static boolean dropcheck(ThingImp obj) {
        if (obj == null)
            return true;
        if (obj != Global.cur_armor && obj != Global.cur_weapon
                && obj != Global.cur_ring[Const.LEFT]
                && obj != Global.cur_ring[Const.RIGHT]) {
            return true;
        }
        if (obj.contains_o_flags(Const.ISCURSED)) {
            IOUtil.msg("you can't.  It appears to be cursed");
            return false;
        }
        if (obj == Global.cur_weapon) {
            Global.cur_weapon = null;
        } else if (obj == Global.cur_armor) {
            ArmorMethod.waste_time();
            Global.cur_armor = null;
        } else {
            Global.cur_ring[obj == Global.cur_ring[Const.LEFT] ? Const.LEFT : Const.RIGHT] = null;
            if (obj._o_which == RingEnum.R_ADDSTR.getValue()) {
                Misc.chg_str(-obj._o_arm);
            } else if (obj._o_which == RingEnum.R_SEEINVIS.getValue()) {
                Daemons.unsee();
                try {
                    Method m = Daemons.class.getMethod("unsee");
                    Daemon.extinguish(m);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    /*
     * drop:
     *	Put something down
     */
    static void drop() {

        ObjectType ch = Util.getPlace(Global.player._t_pos).p_ch;
        if (ch != ObjectType.FLOOR && ch != ObjectType.PASSAGE) {
            Global.after = false;
            IOUtil.msg("there is something there already");
            return;
        }
        ThingImp obj;
        if ((obj = Pack.get_item("drop", ObjectType.Initial)) == null) {
            return;
        }
        if (!dropcheck(obj)) {
            return;
        }
        obj = Pack.leave_pack(obj, true, !Util.ISMULT(obj._o_type));
        /*
         * Link it into the level object list
         */
        Global.lvl_obj.add(obj);
        Util.getPlace(Global.player._t_pos).p_ch = obj._o_type;
        Util.getPlace(Global.player._t_pos).p_flags |= Const.F_DROPPED;
        obj._o_pos = Global.player._t_pos;
        if (obj._o_type == ObjectType.AMULET) {
            Game.getInstance().setGoal(false);
        }
        IOUtil.msg("dropped %s", inv_name(obj, true));
    }

}
