package org.hiro;

import org.hiro.output.Display;
import org.hiro.things.RingEnum;
import org.hiro.things.ThingImp;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class Rip {
    /*
     * death:
     *	Do something really fun break;case he dies
     */
    static void death(int monst) {
        String[] rip = {
                "                       __________\n",
                "                      /          \\\n",
                "                     /    REST    \\\n",
                "                    /      IN      \\\n",
                "                   /     PEACE      \\\n",
                "                  /                  \\\n",
                "                  |                  |\n",
                "                  |                  |\n",
                "                  |   killed by a    |\n",
                "                  |                  |\n",
                "                  |       1980       |\n",
                "                 *|     *  *  *      | *\n",
                "         ________)/\\\\_//(\\/(/\\)/\\//\\/|_)_______\n",
        };
        String killer;
        OffsetDateTime date = OffsetDateTime.now();
        String prbuf;

        // signal(SIGINT, SIG_IGN); // キーボードの割り込み無視
        Global.purse -= Global.purse / 10;
        // signal(SIGINT, leave); // キーボードの割り込みでleave関数実行
        Display.clear();
        killer = killname(monst, false);
        if (!Global.tombstone) {
            Display.mvprintw(Display.LINES - 2, 0, "Killed by ");
            killer = killname(monst, false);
            if (monst != 's' && monst != 'h') {
                // printw("a%s ", Misc.vowelstr(killer));
            }
            // printw("%s with %d gold", killer, Global.purse);
        } else {
            Display.move(8, 0);
//            dp = rip;
//            while (dp){
//            addstr( dp++);
//        }
            Display.mvaddstr(17, center(killer), killer);
            if (monst == 's' || monst == 'h') {
                Display.mvaddch(16, 32, ' ');
            } else {
                Display.mvaddstr(16, 33, String.valueOf(Misc.vowelstr(killer)));
            }
            Display.mvaddstr(14, center(Global.whoami), Global.whoami);
            prbuf = Global.purse + " Au";
            Display.move(15, center(prbuf));
            Display.addstr(prbuf);
            prbuf = String.valueOf(date.getYear());
            Display.mvaddstr(18, 26, prbuf);
        }
        Display.move(Display.LINES - 1, 0);
        Display.refresh();
        Game game = Game.getInstance();
        writelog(Global.purse, game.isGoal() ? 3 : 0, monst);
        score(Global.purse, game.isGoal() ? 3 : 0, monst);
        /* Make sure the output gets through. */
        // printf("[Press return to exit]\n");
        // fflush(null); // 入出力のバッファ吐き出し
        // getchar(); // キーボードの入力検知
        Main2.my_exit(0);
    }

    /*
     * killname:
     *	Convert a code to a monster name
     */
    static String killname(int monst, boolean doart) {
        List<Help_list> nlist = new ArrayList<>();
        {
            Help_list h = new Help_list('a', "arrow", true);
            nlist.add(h);
        }
        {
            Help_list h = new Help_list('b', "bolt", true);
            nlist.add(h);
        }
        {
            Help_list h = new Help_list('d', "dart", true);
            nlist.add(h);
        }
        {
            Help_list h = new Help_list('h', "hypothermia", false);
            nlist.add(h);
        }
        {
            Help_list h = new Help_list('s', "starvation", false);
            nlist.add(h);
        }

        boolean article;
        String sp;
        StringBuilder sb = new StringBuilder();
        if (Character.isUpperCase(monst)) {
            sp = Global.monsters[monst - 'A'].m_name;
            article = true;
        } else {
            sp = "Wally the Wonder Badger";
            article = false;
            for (Help_list hp : nlist) {
                if (hp.h_ch == monst) {
                    sp = hp.h_desc;
                    article = hp.h_print;
                    break;
                }
            }
        }
        if (doart && article) {
            sb.append("a").append(Misc.vowelstr(sp)).append(" ");
        }
        sb.append(sp);
        return sb.toString();
    }

    /*
     * center:
     *	Return the index to center the given string
     */
    static int center(String str) {
        return 28 - ((str.length() + 1) / 2);
    }

    static void writelog(int amount, int flags, int monst) {
        boolean MASTER = false;
        boolean LOGFILE = true;

        if (LOGFILE) {
            String logmessage;
            String ltemp;

            if (Global.logfi == null) {
                return;
            }
            if (MASTER) {
                if (Global.wizard) {
//                    fclose(Global.logfi);
                    return;
                }
            }
            logmessage = OffsetDateTime.now() + " " + amount + " " + Global.whoami + " " + Global.player._t_stats.s_lvl + " ";
            if (flags == 0) /* dead */ {
                ltemp = "killed by " + killname(monst, true) + " on level " + Global.level + "¥n";
                logmessage = logmessage + ltemp;
            } else if (flags == 1) /* quit */ {
                Game game = Game.getInstance();
                if (game.isGoal()) {
                    ltemp = "quit on level " + Global.level + " [max " + Global.max_level
                            + "] with the Amulet\n";
                } else {
                    ltemp = "quit on level " + Global.level + "¥n";
                }
                logmessage = logmessage + ltemp;
            } else if (flags == 2) /* won */ {
                ltemp = "escaped with the Amulet [deepest level: " + Global.max_level
                        + "]\n";
                logmessage = logmessage + ltemp;
            } else if (flags == 3) /* killed with Amulet */ {
                logmessage = logmessage + "killed by ";
                logmessage = logmessage + killname(monst, true);
                ltemp = " on level " + Global.level + " [max " + Global.max_level + "] with the Amulet\n";
                logmessage = logmessage + ltemp;
            } else {
//                fclose(Global.logfi);
                return;
            }
            // TODO:log
//            fprintf(Global.logfi, "%s", logmessage);
//            fclose(Global.logfi);
        }
        return;
    }

    /*
     * score:
     *	Figure score and post it.
     */
    /* VARARGS2 */
    static void score(int amount, int flags, int monst) {
        boolean MASTER = false;
        Score sc2;
        String prbuf = "";
        int prflags = 0; // 本当はMASTERがtrueの時のみ宣言
//    void( * fp)( int);
        int uid;
        String[] reason = {"killed", "quit", "A total winner", "killed with Amulet"};
        String buf;
        String buf2;

        Mach_dep.start_score();

        if (flags >= 0 || (MASTER && Global.wizard)) {
            Display.mvaddstr(Display.LINES - 1, 0, "[Press return to continue]");
            Display.refresh();
            // wgetnstr(stdscr, prbuf, 80);
            Display.endwin();
            // printf("\n");
            Mach_dep.resetltchars();
            /*
             * free up space to "guarantee" there is space for the top_ten
             */
            // Display.delwin(stdscr);
            // Display.delwin(curscr);
//            if (hw != null) {
//                // Display.delwin(hw);
//            }
        }

        List<Score> top_ten = new ArrayList<>();
        for (int i = 0; i < Global.numscores; i++) {
            Score scp = new Score();
            scp.sc_score = 0;
            scp.sc_name = "";
            int seed = 0;
            scp.sc_flags = seed;
            scp.sc_level = seed;
            scp.sc_monster = seed;
            scp.sc_uid = seed;
            top_ten.add(scp);
        }

        // signal(SIGINT, SIG_DFL);

        if (MASTER) {
            if (Global.wizard) {
                if (prbuf.equals("names")) {
                    prflags = 1;
                } else if (prbuf.equals("edit")) {
                    prflags = 2;
                }
            }
        }
        Save.rd_score(top_ten);
        /*
         * Insert her in list if need be
         */
        sc2 = null;
        Score endp = new Score();
        if (!Global.noscore) {
            uid = Mdport.md_getuid();
            Score scp = new Score();
            int i = 0;
            for (i = 0; i < top_ten.size(); i++) {
                scp = top_ten.get(i);
                if (amount > scp.sc_score) {
                    break;
                } else if (Global.allscore == 0 &&    /* only one score per nowin uid */
                        flags != 2 && scp.sc_uid == uid && scp.sc_flags != 2) {
                    scp = endp;
                }
            }
            if (i < top_ten.size()) {
                if (flags != 2 && Global.allscore == 0) {
                    int j = i;
                    for (; j < top_ten.size(); j++) {
                        sc2 = top_ten.get(j);
                        if (sc2.sc_uid == uid && sc2.sc_flags != 2) {
                            break;
                        }
                    }
                    if (j >= top_ten.size()) {
                        i = top_ten.size() - 1;
                    }
                } else {
                    i = top_ten.size() - 1;
                }
//                while (sc2 > scp) {  // TODO: 後で
//                    sc2 = sc2[-1];
//                    sc2--;
//                }
                scp.sc_score = amount;
                scp.sc_name = Global.whoami;
                scp.sc_flags = flags;
                if (flags == 2) {
                    scp.sc_level = Global.max_level;
                } else {
                    scp.sc_level = Global.level;
                }
                scp.sc_monster = monst;
                scp.sc_uid = uid;
                sc2 = scp;
            }
        }
        /*
         * Print the list
         */
        if (flags != -1) {
            // putchar('\n');
        }
        // printf("Top %s %s:\n", Const.Numname, Global.allscore !=0 ? "Scores" : "Rogueists");
        // printf("   Score Name\n");
        Score scp;
//        for (scp = top_ten; scp < endp; scp++) {  // TODO: 後で
//            if (scp.sc_score !=0) {
//                if (sc2 == scp) {
//                    md_raw_standout();
//                    // printf("\x1b[7m");
//                }
//                sprintf(buf, "%2d %5d %s: %s on level %d", (int) (scp - top_ten + 1),
//                        scp.sc_score, scp.sc_name, reason[(int) scp.sc_flags],
//                        scp.sc_level);
//                if (scp.sc_flags == 0 || scp.sc_flags == 3) {
//                    buf2= " by " +killname(scp.sc_monster, true);
//                    buf=buf2;  // もしかしたら80文字にした方が良いかも。
//                }
//                if (MASTER) {
//                    if (prflags == 1) {
//                        buf2 =" ("+ Mdport.md_getrealname(scp.sc_uid)+ ")";
//                        buf =buf2;   // , 80);
//                    } else if (prflags == 2) {
//                        // fflush(stdout);
//                        // (void) fgets(prbuf, 10, stdin);
//                        if (prbuf[0] == 'd') {
//                            for (sc2 = scp; sc2 < endp - 1; sc2++)
//                                sc2 = (sc2 + 1);
//                            sc2 = endp - 1;
//                            sc2.sc_score = 0;
//                            for (int i = 0; i < Const.MAXSTR; i++) {
//                                sc2.sc_name[i] = (char) Util.rnd(255);
//                            }
//                            sc2.sc_flags = RN;
//                            sc2.sc_level = RN;
//                            sc2.sc_monster = RN;
//                            scp--;
//                        }
//                    } else {
//                    }
//                }
//                buf2 =  buf + ".";
//                // printf("%s", buf2); // 元からコメントアウトされてる
//                // fputs(buf2, stdout);
//                if (sc2 == scp) {
//                    // printf("\x1b[m");
//                    Mdport.md_raw_standend();
//                }
//                // putchar('\n');
//            } else
//                break;
//        }
//        /*
//         * Update the list file
//         */
//        if (sc2 != null) {
//            if (lock_sc()) {
//                // fp = signal(SIGINT, SIG_IGN);
//                wr_score(top_ten);
//                unlock_sc();
//                // signal(SIGINT, fp);
//            }
//        }
    }

    /*
     * total_winner:
     *	Code for a winner
     */
    static void total_winner() {
        Display.clear();
        Display.standout();
        Display.addstr("                                                               \n");
        Display.addstr("  @   @               @   @           @          @@@  @     @  \n");
        Display.addstr("  @   @               @@ @@           @           @   @     @  \n");
        Display.addstr("  @   @  @@@  @   @   @ @ @  @@@   @@@@  @@@      @  @@@    @  \n");
        Display.addstr("   @@@@ @   @ @   @   @   @     @ @   @ @   @     @   @     @  \n");
        Display.addstr("      @ @   @ @   @   @   @  @@@@ @   @ @@@@@     @   @     @  \n");
        Display.addstr("  @   @ @   @ @  @@   @   @ @   @ @   @ @         @   @  @     \n");
        Display.addstr("   @@@   @@@   @@ @   @   @  @@@@  @@@@  @@@     @@@   @@   @  \n");
        Display.addstr("                                                               \n");
        Display.addstr("     Congratulations, you have made it to the light of day!    \n");
        Display.standend();
        Display.addstr("\nYou have joined the elite ranks of those who have escaped the\n");
        Display.addstr("Dungeons of Doom alive.  You journey home and sell all your loot at\n");
        Display.addstr("a great profit and are admitted to the Fighters' Guild.\n");
        Display.mvaddstr(Display.LINES - 1, 0, "--Press space to continue--");
        Display.refresh();
        // wait_for(stdscr, ' ');
        Display.clear();
        Display.mvaddstr(0, 0, "   Worth  Item\n");

        int oldpurse = Global.purse;
        int worth = 0;
        for (ThingImp obj : Global.player.getBaggage()) {
            Obj_info op;
            switch (obj._o_type) {
                case FOOD:
                    worth = 2 * obj._o_count;
                    break;
                case WEAPON:
                    worth = Global.weap_info[obj._o_which].getWorth();
                    worth *= 3 * (obj._o_hplus + obj._o_dplus) + obj._o_count;
                    obj.add_o_flags(Const.ISKNOW);
                    break;
                case ARMOR:
                    worth = Global.arm_info[obj._o_which].getWorth();
                    worth += (9 - obj._o_arm) * 100;
                    worth += (10 * (Global.a_class[obj._o_which] - obj._o_arm));
                    obj.add_o_flags(Const.ISKNOW);
                    break;
                case SCROLL:
                    worth = Global.scr_info[obj._o_which].getWorth();
                    worth *= obj._o_count;
                    op = Global.scr_info[obj._o_which];
                    if (!op.oi_know) {
                        worth /= 2;
                    }
                    op.oi_know = true;
                    break;
                case POTION:
                    worth = Global.pot_info[obj._o_which].getWorth();
                    worth *= obj._o_count;
                    op = Global.pot_info[obj._o_which];
                    if (!op.oi_know) {
                        worth /= 2;
                    }
                    op.oi_know = true;
                    break;
                case RING:
                    op = Global.ring_info[obj._o_which];
                    worth = op.getWorth();
                    if (obj._o_which == RingEnum.R_ADDSTR.getValue() || obj._o_which == RingEnum.R_ADDDAM.getValue() ||
                            obj._o_which == RingEnum.R_PROTECT.getValue() || obj._o_which == RingEnum.R_ADDHIT.getValue()) {
                        if (obj._o_arm > 0) {
                            worth += obj._o_arm * 100;
                        } else {
                            worth = 10;
                        }
                    }
                    if (!obj.contains_o_flags(Const.ISKNOW))
                        worth /= 2;
                    obj.add_o_flags(Const.ISKNOW);
                    op.oi_know = true;
                    break;
                case STICK:
                    op = Global.ws_info[obj._o_which];
                    worth = op.getWorth();
                    worth += 20 * obj._o_arm;
                    if (!obj.contains_o_flags(Const.ISKNOW)) {
                        worth /= 2;
                    }
                    obj.add_o_flags(Const.ISKNOW);
                    op.oi_know = true;
                    break;
                case AMULET:
                    worth = 1000;
            }
            if (worth < 0)
                worth = 0;
            // printw("%c) %5d  %s\n", obj._o_packch, worth, ThingMethod.inv_name(obj, false));
            Global.purse += worth;
        }
        Display.printw("   %5d  Gold Pieces          ", oldpurse);
        Display.refresh();
        writelog(Global.purse, 2, ' ');
        score(Global.purse, 2, ' ');
        // printf("[Press return to exit]\n");
        // fflush(null);
        // getchar();
        Main2.my_exit(0);
    }

}
