package org.hiro;

public class Mach_dep {

    /*
     * resetltchars:
     *	Reset the local tty chars to original values.
     */
    static void resetltchars() {
        if (Global.got_ltc != 0) {
            Mdport.md_setdsuspchar(Global.orig_dsusp);
        }
    }


    /*
     * start_score:
     *	Start the scoring sequence
     */
    static void start_score() {
        boolean CHECKTIME = false;
        if (CHECKTIME) {
            md_stop_checkout_timer();
        }
    }

    static void md_stop_checkout_timer() {
        boolean SIGALRM = false;
        if (SIGALRM) {
            // signal(SIGALRM, SIG_IGN);
        }
    }

    /*
     * flush_type:
     *	Flush typeahead for traps, etc.
     */
    static void flush_type()
    {
        // flushinp();
    }

}
