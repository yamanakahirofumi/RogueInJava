package org.hiro;

import org.hiro.character.Human;
import org.hiro.output.Display;

public class Main2 {

    /*
     * my_exit:
     *	Leave the process properly
     */
    static void my_exit(int st) {
        Mach_dep.resetltchars();
        System.exit(st);
    }

    /*
     * leave:
     *	Leave quickly, but curteously
     */
    static void leave(int sig) {
        // static char[] buf= new[ BUFSIZ];

        // NOOP(sig);

        // setbuf(stdout, buf);    /* throw away pending output */

        if (!Display.isendwin()) {
            Display.mvcur(0, Display.COLS - 1, Display.LINES - 1, 0);
            Display.endwin();
        }

        // putchar('\n');
        my_exit(0);
    }

    /*
     * quit:
     *	Have player make certain, then exit.
     */
    public static void quit(int sig) {

        // NOOP(sig);

        /*
         * Reset the signal in case we got here via an interrupt
         */
        if (!Global.q_comm) {
            Global.mpos = 0;
        }
        int oy;
        int ox;

        // getyx(curscr, oy, ox);
        IOUtil.msg("really quit?");
        if (IOUtil.readchar() == 'y') {
            // signal(SIGINT, leave);
            Display.clear();
            Display.mvprintw(Display.LINES - 2, 0, "You quit with %d gold pieces", String.valueOf(Global.purse));
            Display.move(Display.LINES - 1, 0);
            Display.refresh();
            Rip.writelog(Global.purse, 1, 0);
            Rip.score(Global.purse, 1, 0);
            // printf("[Press return to exit]\n");
            // fflush(null);
            // getchar();
            my_exit(0);
        } else {
            Display.move(0, 0);
            Display.clrtoeol();
            IOUtil.status(Human.instance);
            // Display.move(oy, ox);
            Display.refresh();
            Global.mpos = 0;
            Global.count = 0;
            Global.to_death = false;
        }
    }


}
