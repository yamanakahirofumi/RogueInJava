package org.hiro;

import org.hiro.character.Human;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Options {

    static List<Optstruct> optlist;

    static {
        optlist = new ArrayList<>();
        try {
            {
                // 第３引数はポインタを覚えて置きたいのであって、変数値を覚えたわけではない
                Optstruct op = new Optstruct("terse", "Terse output", Global.terse);
                op.o_putfunc = Options.class.getMethod("put_bool", Object.class);
                op.o_getfunc = Options.class.getMethod("get_bool", Object.class);
                optlist.add(op);
            }
            {
                Optstruct op = new Optstruct("flush", "Flush typeahead during battle", Global.fight_flush);
                op.o_putfunc = Options.class.getMethod("put_bool", Object.class);
                op.o_getfunc = Options.class.getMethod("get_bool", Object.class);
                optlist.add(op);
            }
            {
                Optstruct op = new Optstruct("jump", "Show position only at end of run", Global.jump);
                op.o_putfunc = Options.class.getMethod("put_bool", Object.class);
                op.o_getfunc = Options.class.getMethod("get_bool", Object.class);
                optlist.add(op);
            }
            {
                Optstruct op = new Optstruct("seefloor", "Show the lamp-illuminated floor", Global.see_floor);
                op.o_putfunc = Options.class.getMethod("put_bool", Object.class);
                op.o_getfunc = Options.class.getMethod("get_sf", Object.class);
                optlist.add(op);
            }
            {
                Optstruct op = new Optstruct("passgo", "Follow turnings in passageways", Global.passgo);
                op.o_putfunc = Options.class.getMethod("put_bool", Object.class);
                op.o_getfunc = Options.class.getMethod("get_bool", Object.class);
                optlist.add(op);
            }
            {
                Optstruct op = new Optstruct("tombstone", "Print out tombstone when killed", Global.tombstone);
                op.o_putfunc = Options.class.getMethod("put_bool", Object.class);
                op.o_getfunc = Options.class.getMethod("get_bool", Object.class);
                optlist.add(op);
            }
            {
                Optstruct op = new Optstruct("inven", "Inventory style", Global.inv_type);
                op.o_putfunc = Options.class.getMethod("put_inv_t", Object.class);
                op.o_getfunc = Options.class.getMethod("get_inv_t", Object.class);
                optlist.add(op);
            }
            {
                Optstruct op = new Optstruct("name", "Name", Global.whoami);
                op.o_putfunc = Options.class.getMethod("put_str", Object.class);
                op.o_getfunc = Options.class.getMethod("get_str", Object.class);
                optlist.add(op);
            }
            {
                Optstruct op = new Optstruct("fruit", "Fruit", Global.fruit);
                op.o_putfunc = Options.class.getMethod("put_str", Object.class);
                op.o_getfunc = Options.class.getMethod("get_str", Object.class);
                optlist.add(op);
            }
            {
                Optstruct op = new Optstruct("file", "Save file", Global.file_name);
                op.o_putfunc = Options.class.getMethod("put_str", Object.class);
                op.o_getfunc = Options.class.getMethod("get_str", Object.class);
                optlist.add(op);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /*
     * option:
     *	Print and then set options from the terminal
     */
    public static void option() {
        int retval;

        // wclear(hw);
        /*
         * Display current values of options
         */
        try {
            for (Optstruct op : optlist) {
                if (allowchange(op)) {
                    pr_optname(op);
                    op.o_putfunc.invoke(op.o_opt);
                    // waddch(hw, '\n');
                }
            }
            /*
             * Set values
             */
            // wmove(hw, 0, 0);
//            for (op = optlist; op <= optlist[NUM_OPTS - 1]; op++) {
            for (Optstruct op : optlist) {
                if (!allowchange(op)) {
                    continue;
                }
                pr_optname(op);
                // retval = op.o_getfunc.invoke(op.o_opt, hw);
                retval = (int) op.o_getfunc.invoke(op.o_opt);
                if (retval != 0) {
                    if (retval == Const.QUIT) {
                        break;
                        /* Support for MINUS removed until this section is rewritten. */
//                    } else if (i > 0) {    /* MINUS */
//                        // wmove(hw, (int) (op - optlist) - 1, 0);
//                        i -= 2;
//                    } else {   /* trying to back up beyond the top */ 
//                        // putchar('\007');
//                        // wmove(hw, 0, 0);
//                        i--;
                    }
                }
            }
            /*
             * Switch back to original screen
             */
            // wmove(hw, Display.LINES - 1, 0);
            // waddstr(hw, "--Press space to continue--");
            // wrefresh(hw);
            // wait_for(hw, ' ');
            // clearok(curscr, true);
            // touchwin(stdscr);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        Global.after = false;
    }

    /* Tells whether the user is allowed to change the option. */
    static boolean allowchange(Optstruct opt) {
        if (!Global.use_savedir) {
            return true;
        }
        if (!"name".equals(opt.o_name)) {
            return false;
        }
        return "file".equals(opt.o_name);
    }

    /*
     * pr_optname:
     *	Print out the option name prompt
     */
    static void pr_optname(Optstruct op) {
        // wprintw(hw, "%s (\"%s\"): ", op.o_prompt, op.o_name);
    }

    /*
     * put_bool
     *	Put out a boolean
     */
    static void put_bool(Object b) {
        // waddstr(hw,  ( boolean )b ? "True" : "False");
    }

    /*
     * put_str:
     *	Put out a string
     */
    static void put_str(Object str) {
        // waddstr(hw, ( String)str);
    }

    /*
     * put_inv_t:
     *	Put out an inventory type
     */
    void put_inv_t(Object ip) {
        // waddstr(hw, Const.inv_t_name.get(( int )ip));
    }

    /*
     * get_bool:
     *	Allow changing a boolean option and print it out
     *  引数のWindowは一旦削除
     */
    int get_bool(Object vp) {
        //  WINDOW *win;　一旦削除された引数
        boolean bp = (boolean) vp;
        int oy, ox;
        boolean op_bad;

        op_bad = false;  // 本当は true
        // getyx(win, oy, ox);
        // waddstr(win,  bp ? "True" : "False");
        while (op_bad) {
            // wmove(win, oy, ox);
            // wrefresh(win);
//            switch (wreadchar(win)) {
//                case 't':
//                case 'T':
//                    bp = true;
//                    op_bad = false;
//                    break;
//                case 'f':
//                case 'F':
//                    bp = false;
//                    op_bad = false;
//                    break;
//                case '\n':
//                case '\r':
//                    op_bad = false;
//                    break;
//                case Const.ESCAPE:
//                    return Const.QUIT;
//                case '-':
//                    return Const.MINUS;
//                default:
//                    // wmove(win, oy, ox + 10);
//                    // waddstr(win, "(T or F)");
//            }
        }
        // wmove(win, oy, ox);
        // waddstr(win,  bp ? "True" : "False");
        // waddch(win, '\n');
        return Const.NORM;
    }

    /*
     * get_sf:
     *	Change value and handle transition problems from see_floor to
     *	!see_floor.
     *
     *  引数のWindowは一旦削除
     */
    int get_sf(Object vp) {
        // , WINDOW *win  一旦削除された引数
        int bp = (int) vp;
        boolean was_sf;
        int retval;

        was_sf = Global.see_floor;
        retval = get_bool(bp);
        if (retval == Const.QUIT || retval == Const.MINUS) {
            return retval;
        }
        if (was_sf != Global.see_floor) {
            if (!Global.see_floor) {
                Global.see_floor = true;
                Misc.erase_lamp(Human.instance.getPosition(), Human.instance.getRoom());
                Global.see_floor = false;
            } else
                Misc.look(false);
        }
        return Const.NORM;
    }


}
