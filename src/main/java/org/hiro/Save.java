package org.hiro;

import java.io.File;
import java.util.List;

public class Save {


    static int encerrno = 0;

    /*
     * read_scrore
     *	Read in the score file
     */
    static void rd_score(List<Score> top_ten) {
        String scoreline;

        if (Global.scoreboard == null) {
            return;
        }

        // rewind(Global.scoreboard); // 先頭から

        for (Score s : top_ten) {
            encread(s.getSc_name(), Const.MAXSTR, Global.scoreboard);
            scoreline = "";
            encread(scoreline, 100, Global.scoreboard);
//            (void) sscanf(scoreline, " %u %d %u %d %d %x \n",
//                    s.sc_uid, s.sc_score,
//                    s.sc_flags, s.sc_monster,
//                    s.sc_level, s.sc_time);
        }

        // rewind(Global.scoreboard);  // 先頭から
    }


    /*
     * encread:
     *	Perform an encrypted read
     */
    static int encread(String start, int size, File inf) {
        String e1, e2;
        char fb;
        int temp;
        int read_size;
        int items;
        fb = 0;

        int errno =0; // 適当に付け加えた

        if (encerrno !=0) {
            errno = encerrno;
            return 0;
        }

//        items = read_size = fread(start, 1, size, inf);

        e1 = Global.encstr;
        e2 = Global.statlist;

//        while (read_size-- !=0) {
//            start++ ^= e1 ^ e2 ^ fb;
//            temp = e1++;
//            fb = fb + (char) (temp * * e2++);
//            if (e1 == '\0') {
//                e1 = encstr;
//            }
//            if (e2 == '\0') {
//                e2 = statlist;
//            }
//        }

        items = 0;
        if (items != size) {
            encerrno = errno;
        }

        return items;
    }


}
