package org.hiro;

import org.hiro.character.Human;
import org.hiro.output.Display;
import org.hiro.things.ObjectType;

public class IOUtil {

    /*
     * step_ok:
     *	Returns true if it is ok to step on ch
     *  歩けるところがtrue
     */
    public static boolean step_ok(ObjectType ch) {
        return step_ok(ch.getValue());
    }

    private static boolean step_ok(char ch) {
        switch (ch) {
            case ' ':
            case '|':
            case '-':
                return false;
            default:
                return (!Character.isAlphabetic(ch));
        }
    }

    /*
     * wait_for
     *	Sit around until the guy types the right key
     */
//	void wait_for(WINDOW win, int ch) {
//		int c;
//
//		Display.move(0, mpos);
//
//		if (ch == '\n')
//			while ((c = wreadchar(win)) != '\n' && c != '\r')
//				continue;
//		else
//			while (wreadchar(win) != ch)
//				continue;
//	}


    static String msgbuf;
    static int newpos = 0;


    /* VARARGS1 */
    public static int msg(String message, Object... args) {
        // va_list args;

        /*
         * if the string is "", just clear the line
         */
        if (message.length() < 1) {
            Display.move(0, 0);
            Display.clrtoeol();
            Global.mpos = 0;
            return ~Const.ESCAPE;
        }
        /*
         * otherwise add to the message and flush it out
         */
        // va_start(args, message);
        // doadd(message, args);
        // va_end(args);
        return endmsg();
    }

    /*
     * endmsg:
     *	Display a new msg (giving him a chance to see the previous one
     *	if it is up there with the --More--)
     */
    public static int endmsg() {
        int ch;

        if (Global.save_msg) {
            Global.huh = msgbuf;
        }
        if (Global.mpos != 0) {
            Misc.look(false);
            Display.mvaddstr(0, Global.mpos, "--More--");
            Display.refresh();
            if (!Global.msg_esc) {
                // IOUtil.wait_for(stdscr, ' ');
            } else {
                while ((ch = readchar()) != ' ')
                    if (ch == Const.ESCAPE) {
                        msgbuf = "";
                        Global.mpos = 0;
                        newpos = 0;
                        msgbuf = "";
                        return Const.ESCAPE;
                    }
            }
        }
        /*
         * All messages should start with uppercase, except ones that
         * start with a pack addressing character
         */
        if (Character.isLowerCase(msgbuf.charAt(0)) && !Global.lower_msg && msgbuf.charAt(0) != ')') {
            msgbuf = Character.toUpperCase(msgbuf.charAt(0)) + msgbuf.substring(1);
        }
        Display.mvaddstr(0, 0, msgbuf);
        Display.clrtoeol();
        Global.mpos = newpos;
        newpos = 0;
        msgbuf = "";
        Display.refresh();
        return ~Const.ESCAPE;
    }

    /*
     * doadd:
     *	Perform an add onto the message buffer
     */
//    static void doadd(String fmt, va_list args) {
//        String buf;
//
//        int MAXMSG = Const.NUMCOLS - "--More--".length();
//        /*
//         * Do the printf into buf
//         */
//        // vsprintf(buf, fmt, args);
//        if (buf.length() + newpos >= MAXMSG)
//            endmsg();
//        msgbuf = buf;
//        newpos = (int) msgbuf.length();
//    }

    /*
     * addmsg:
     *	Add things to the current message
     */
    /* VARARGS1 */
    public static void addmsg(String fmt, Object... args) {
        // va_list args;

        // va_start(args, fmt);
        // doadd(fmt, args);
        // va_end(args);
    }

    /*
     * readchar:
     *	Reads and returns a character, checking for gross input errors
     */
    static int readchar() {
        int ch = 0;

        if (Global.mpos != 0) {
            Display.move(0, Global.mpos++);
        }

        // ch = Mdport.md_readchar(stdscr);

        if (ch == 3) {
            Main2.quit(0);
            return (27);
        }

        return (ch);
    }

    /*
     * status:
     *	Display the important stats line.  Keep the cursor where it was.
     */
    static void status() {
        int oy;
        int ox;
        int s_hungry = 0;
        int s_lvl = 0;
        int s_pur = -1;
        int s_hp = 0;
        int s_arm = 0;
        int s_str = 0;
        long s_exp = 0;
        String[] state_name = {"", "Hungry", "Weak", "Faint"};

        /*
         * If nothing has changed since the last status, don't
         * bother.
         */
        int temp = (Human.instance.isEquippedArmor() ? Human.instance.getArmor()._o_arm : Global.player._t_stats.s_arm);
        if (s_hp == Human.instance.getHp() && s_exp == Global.player._t_stats.s_exp && s_pur == Global.purse
                && s_arm == temp && s_str == Human.instance.getCurrentStrength() && s_lvl == Human.instance.getLevel()
                && s_hungry == Global.hungry_state && !Global.stat_msg) {
            return;
        }

        s_arm = temp;

        // getyx(stdscr, oy, ox);
        int hpwidth = 0;
        if (s_hp != Human.instance.getMaxHp()) {
            temp = Human.instance.getMaxHp();
            s_hp = Human.instance.getMaxHp();
            for (hpwidth = 0; temp != 0; hpwidth++) {
                temp /= 10;
            }
        }

        /*
         * Save current status
         */
        s_lvl = Human.instance.getLevel();
        s_pur = Global.purse;
        s_hp = Human.instance.getHp();
        s_str = Human.instance.getCurrentStrength();
        s_exp = Global.player._t_stats.s_exp;
        s_hungry = Global.hungry_state;

        if (Global.stat_msg) {
            Display.move(0, 0);
            msg("Level: %d  Gold: %-5d  Hp: %*d(%*d)  Str: %2d(%d)  Arm: %-2d  Exp: %d/%d  %s",
                    s_lvl, s_pur, hpwidth, s_hp, hpwidth,
                    Human.instance.getMaxHp(), s_str, Global.max_stats.s_str,
                    10 - s_arm, Global.player._t_stats.s_lvl, s_exp,
                    state_name[s_hungry]);
        } else {
            Display.move(Const.STATLINE, 0);

            Display.printw("Level: %d  Gold: %-5d  Hp: %*d(%*d)  Str: %2d(%d)  Arm: %-2d  Exp: %d/%d  %s",
                    s_lvl, s_pur, hpwidth, s_hp, hpwidth,
                    Human.instance.getMaxHp(), s_str, Global.max_stats.s_str,
                    10 - s_arm, Global.player._t_stats.s_lvl, s_exp,
                    state_name[s_hungry]);
        }

        Display.clrtoeol();
        // Display.move(oy, ox);
    }

    /*
     * show_win:
     *	Function used to display a window and wait before returning
     */
    public static void show_win(String message) {
        // WINDOW win;

//        win = hw;
//        wmove(win, 0, 0);
//        waddstr(win, message);
//        touchwin(win);
//        wrefresh(win);
//        wait_for(win, ' ');
//        clearok(curscr, true);
//        touchwin(stdscr);
    }

    static void endmsg2() {
        Display.move(0, 0);
        Display.clrtoeol();
        Global.mpos = 0;
        Display.refresh();
        return;
    }

//    int wreadchar(WINDOW *win) {
//        int ch;
//
//        ch = md_readchar(win);
//
//        if (ch == 3) {
//            Main2.quit(0);
//            return (27);
//        }
//
//        return (ch);
//    }


}
