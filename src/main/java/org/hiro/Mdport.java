package org.hiro;


/**
 * Machine Dependent Code
 */
public class Mdport {


    static int md_setdsuspchar(int c) {
        if (Global.VDSUSP) {            /* POSIX has priority */
//            struct termios attr;
//            tcgetattr(STDIN_FILENO, & attr);
//            attr.c_cc[Global.VDSUSP] = c;
//            tcgetattr(STDIN_FILENO, & attr);
        } else if (Global.TIOCSLTC) {
//            struct ltchars ltc;
//            ioctl(1, Global.TIOCGLTC, & ltc);
//            ltc.t_dsuspc = c;
//            ioctl(1, Global.TIOCSLTC, & ltc);
        } else {
//            NOOP(c);
        }
        return (0);
    }


//    static int md_readchar(WINDOW win) {
//        int ch = 0;
//        int lastch = 0;
//        int mode = M_NORMAL;
//        int mode2 = M_NORMAL;
//        int nodelayf = 0;
//        int count = 0;
//
//        for (; ; ) {
//            if (mode == M_NORMAL && uindex >= 0) {
//                ch = reread();
//                break;
//            }
//
//            ch = wgetch(win);
//
//            if (ch == ERR) /* timed out  or error */ {
//                if (nodelayf)               /* likely timed out, switch to */ {                           /* normal mode and block on    */
//                    mode = M_NORMAL;        /* next read                   */
//                    nodelayf = 0;
//                    nodelay(win, 0);
//                } else if (count > 10)        /* after 10 errors assume      */
//                    auto_save(0);           /* input stream is broken and  */
//                else                        /* auto save and exit          */
//                    count++;
//
//                continue;
//            }
//
//            count = 0;                      /* reset input error count     */
//
//            if (mode == M_TRAIL) {
//                if (ch == '^')        /* msys console  : 7,5,6,8: modified*/
//                    ch = CTRL(toupper(lastch));
//                else if (ch == '~')        /* cygwin console: 1,5,6,4: normal  */
//                    ch = tolower(lastch);   /* windows telnet: 1,5,6,4: normal  */
//                    /* msys console  : 7,5,6,8: normal  */
//                else if (mode2 == M_ESC)        /* cygwin console: 1,5,6,4: modified*/
//                    ch = CTRL(toupper(ch));
//                else {
//                    mode = M_NORMAL;
//                    unread(ch);
//                    continue;
//                }
//
//                break;
//            }
//
//            if (mode == M_ESC) {
//                if (ch == 27) {
//                    mode2 = M_ESC;
//                    unread(ch);
//                    continue;
//                }
//
//                if ((ch == 'F') || (ch == 'O') || (ch == '[')) {
//                    mode = M_KEYPAD;
//                    unread(ch);
//                    continue;
//                }
//
//
//                switch (ch) {
//                    /* Cygwin Console   */
//                    /* PuTTY	    */
//                    case KEY_LEFT:
//                        ch = Util.CTRL('H');
//                        break;
//                    case KEY_RIGHT:
//                        ch = Util.CTRL('L');
//                        break;
//                    case KEY_UP:
//                        ch = Util.CTRL('K');
//                        break;
//                    case KEY_DOWN:
//                        ch = Util.CTRL('J');
//                        break;
//                    case KEY_HOME:
//                        ch = Util.CTRL('Y');
//                        break;
//                    case KEY_PPAGE:
//                        ch = Util.CTRL('U');
//                        break;
//                    case KEY_NPAGE:
//                        ch = Util.CTRL('N');
//                        break;
//                    case KEY_END:
//                        ch = Util.CTRL('B');
//                        break;
//
//                    default:
//                        mode = M_NORMAL;
//                        mode2 = M_NORMAL;
//                        unread(ch);
//                        continue;
//                }
//
//                break;
//            }
//
//            if (mode == M_KEYPAD) {
//                switch (ch) {
//                    /* ESC F - Interix Console codes */
//                    case '^':
//                        ch = Util.CTRL('H');
//                        break;    /* Shift-Left	    */
//                    case '$':
//                        ch = Util.CTRL('L');
//                        break;    /* Shift-Right	    */
//
//                    /* ESC [ - Interix Console codes */
//                    case 'H':
//                        ch = 'y';
//                        break;        /* Home		    */
//                    case 1:
//                        ch = Util.CTRL('K');
//                        break;    /* Ctl-Keypad Up    */
//                    case 2:
//                        ch = Util.CTRL('J');
//                        break;    /* Ctl-Keypad Down  */
//                    case 3:
//                        ch = Util.CTRL('L');
//                        break;    /* Ctl-Keypad Right */
//                    case 4:
//                        ch = Util.CTRL('H');
//                        break;    /* Ctl-Keypad Left  */
//                    case 263:
//                        ch = Util.CTRL('Y');
//                        break;    /* Ctl-Keypad Home  */
//                    case 19:
//                        ch = Util.CTRL('U');
//                        break;    /* Ctl-Keypad PgUp  */
//                    case 20:
//                        ch = Util.CTRL('N');
//                        break;    /* Ctl-Keypad PgDn  */
//                    case 21:
//                        ch = Util.CTRL('B');
//                        break;    /* Ctl-Keypad End   */
//
//                    /* ESC [ - Cygwin Console codes */
//                    case 'G':
//                        ch = '.';
//                        break;        /* Keypad 5	    */
//                    case '7':
//                        lastch = 'Y';
//                        mode = M_TRAIL;
//                        break;    /* Ctl-Home */
//                    case '5':
//                        lastch = 'U';
//                        mode = M_TRAIL;
//                        break;    /* Ctl-PgUp */
//                    case '6':
//                        lastch = 'N';
//                        mode = M_TRAIL;
//                        break;    /* Ctl-PgDn */
//
//                    /* ESC [ - Win32 Telnet, PuTTY */
//                    case '1':
//                        lastch = 'y';
//                        mode = M_TRAIL;
//                        break;    /* Home	    */
//                    case '4':
//                        lastch = 'b';
//                        mode = M_TRAIL;
//                        break;    /* End	    */
//
//                    /* ESC [ - not understood by screen/tmux */
//                    case 'E':
//                        ch = '.';
//                        break;            /* Keypad 5 */
//
//                    /* ESC O - PuTTY */
//                    case 'D':
//                        ch = Util.CTRL('H');
//                        break;
//                    case 'C':
//                        ch = Util.CTRL('L');
//                        break;
//                    case 'A':
//                        ch = Util.CTRL('K');
//                        break;
//                    case 'B':
//                        ch = Util.CTRL('J');
//                        break;
//                    case 't':
//                        ch = 'h';
//                        break;
//                    case 'v':
//                        ch = 'l';
//                        break;
//                    case 'x':
//                        ch = 'k';
//                        break;
//                    case 'r':
//                        ch = 'j';
//                        break;
//                    case 'w':
//                        ch = 'y';
//                        break;
//                    case 'y':
//                        ch = 'u';
//                        break;
//                    case 's':
//                        ch = 'n';
//                        break;
//                    case 'q':
//                        ch = 'b';
//                        break;
//                    case 'u':
//                        ch = '.';
//                        break;
//                }
//
//                if (mode != M_KEYPAD) {
//                    unread(ch);
//                    continue;
//                }
//            }
//
//            if (ch == 27) {
//                nodelay(win, 1);
//                mode = M_ESC;
//                nodelayf = 1;
//                unread(ch);
//                continue;
//            }
//
//            switch (ch) {
//                case KEY_LEFT:
//                    ch = 'h';
//                    break;
//                case KEY_DOWN:
//                    ch = 'j';
//                    break;
//                case KEY_UP:
//                    ch = 'k';
//                    break;
//                case KEY_RIGHT:
//                    ch = 'l';
//                    break;
//                case KEY_HOME:
//                    ch = 'y';
//                    break;
//                case KEY_PPAGE:
//                    ch = 'u';
//                    break;
//                case KEY_END:
//                    ch = 'b';
//                    break;
//                case KEY_LL:
//                    if (KEY_LL) {
//                        ch = 'b';
//                    }
//                    break;
//                case KEY_NPAGE:
//                    ch = 'n';
//                    break;
//                case KEY_BEG:
//                    ch = '.';
//                    break;
//
//#ifdef KEY_B1
//                case KEY_B1:
//                    ch = 'h';
//                    break;
//                case KEY_C2:
//                    ch = 'j';
//                    break;
//                case KEY_A2:
//                    ch = 'k';
//                    break;
//                case KEY_B3:
//                    ch = 'l';
//                    break;
//#endif
//                case KEY_A1:
//                    ch = 'y';
//                    break;
//                case KEY_A3:
//                    ch = 'u';
//                    break;
//                case KEY_C1:
//                    ch = 'b';
//                    break;
//                case KEY_C3:
//                    ch = 'n';
//                    break;
//                case KEY_B2:
//                    ch = '.';
//                    break;
//
//#ifdef KEY_SLEFT
//                case KEY_SRIGHT:
//                    ch = Util.CTRL('L');
//                    break;
//                case KEY_SLEFT:
//                    ch = Util.CTRL('H');
//                    break;
//#ifdef KEY_SUP
//                case KEY_SUP:
//                    ch = Util.CTRL('K');
//                    break;
//                case KEY_SDOWN:
//                    ch = Util.CTRL('J');
//                    break;
//#endif
//                case KEY_SHOME:
//                    ch = Util.CTRL('Y');
//                    break;
//                case KEY_SPREVIOUS:
//                    ch = Util.CTRL('U');
//                    break;
//                case KEY_SEND:
//                    ch = Util.CTRL('B');
//                    break;
//                case KEY_SNEXT:
//                    ch = Util.CTRL('N');
//                    break;
//#endif
//                case 0x146:
//                    ch = Util.CTRL('K');
//                    break;    /* Shift-Up	*/
//                case 0x145:
//                    ch = Util.CTRL('J');
//                    break;    /* Shift-Down	*/
//
//
//#ifdef CTL_RIGHT
//                case CTL_RIGHT:
//                    ch = Util.CTRL('L');
//                    break;
//                case CTL_LEFT:
//                    ch = Util.CTRL('H');
//                    break;
//                case CTL_UP:
//                    ch = Util.CTRL('K');
//                    break;
//                case CTL_DOWN:
//                    ch = Util.CTRL('J');
//                    break;
//                case CTL_HOME:
//                    ch = Util.CTRL('Y');
//                    break;
//                case CTL_PGUP:
//                    ch = Util.CTRL('U');
//                    break;
//                case CTL_END:
//                    ch = Util.CTRL('B');
//                    break;
//                case CTL_PGDN:
//                    ch = Util.CTRL('N');
//                    break;
//#endif
//#ifdef KEY_EOL
//                case KEY_EOL:
//                    ch = Util.CTRL('B');
//                    break;
//#endif
//
//#ifndef CTL_PAD1
//                /* MSYS rxvt console */
//                case 511:
//                    ch = Util.CTRL('J');
//                    break; /* Shift Dn */
//                case 512:
//                    ch = Util.CTRL('J');
//                    break; /* Ctl Down */
//                case 514:
//                    ch = Util.CTRL('H');
//                    break; /* Ctl Left */
//                case 516:
//                    ch = Util.CTRL('L');
//                    break; /* Ctl Right*/
//                case 518:
//                    ch = Util.CTRL('K');
//                    break; /* Shift Up */
//                case 519:
//                    ch = Util.CTRL('K');
//                    break; /* Ctl Up   */
//#endif
//
//#ifdef CTL_PAD1
//                case CTL_PAD1:
//                    ch = Util.CTRL('B');
//                    break;
//                case CTL_PAD2:
//                    ch = Util.CTRL('J');
//                    break;
//                case CTL_PAD3:
//                    ch = Util.CTRL('N');
//                    break;
//                case CTL_PAD4:
//                    ch = Util.CTRL('H');
//                    break;
//                case CTL_PAD5:
//                    ch = '.';
//                    break;
//                case CTL_PAD6:
//                    ch = Util.CTRL('L');
//                    break;
//                case CTL_PAD7:
//                    ch = Util.CTRL('Y');
//                    break;
//                case CTL_PAD8:
//                    ch = Util.CTRL('K');
//                    break;
//                case CTL_PAD9:
//                    ch = Util.CTRL('U');
//                    break;
//#endif
//
//#ifdef ALT_RIGHT
//                case ALT_RIGHT:
//                    ch = Util.CTRL('L');
//                    break;
//                case ALT_LEFT:
//                    ch = Util.CTRL('H');
//                    break;
//                case ALT_DOWN:
//                    ch = Util.CTRL('J');
//                    break;
//                case ALT_HOME:
//                    ch = Util.CTRL('Y');
//                    break;
//                case ALT_PGUP:
//                    ch = Util.CTRL('U');
//                    break;
//                case ALT_END:
//                    ch = Util.CTRL('B');
//                    break;
//                case ALT_PGDN:
//                    ch = CTRL('N');
//                    break;
//#endif
//
//#ifdef ALT_PAD1
//                case ALT_PAD1:
//                    ch = Util.CTRL('B');
//                    break;
//                case ALT_PAD2:
//                    ch = Util.CTRL('J');
//                    break;
//                case ALT_PAD3:
//                    ch = Util.CTRL('N');
//                    break;
//                case ALT_PAD4:
//                    ch = Util.CTRL('H');
//                    break;
//                case ALT_PAD5:
//                    ch = '.';
//                    break;
//                case ALT_PAD6:
//                    ch = Util.CTRL('L');
//                    break;
//                case ALT_PAD7:
//                    ch = Util.CTRL('Y');
//                    break;
//                case ALT_PAD8:
//                    ch = Util.CTRL('K');
//                    break;
//                case ALT_PAD9:
//                    ch = Util.CTRL('U');
//                    break;
//#endif
//#ifdef KEY_BACKSPACE
//                case KEY_BACKSPACE:
//                    ch = md_erasechar();
//                    break;
//#endif
//            }
//
//            break;
//        }
//
//        if (nodelayf)
//            nodelay(win, 0);
//
//        uindex = -1;
//
//        return (ch & 0x7F);
//    }

    static boolean md_hasclreol() {
//        if (clr_eol) {
//            if (NCURSES_VERSION) {
//                if (cur_term == null) {
//                    return false;
//                }
//                if (0 != 0) {
//                    if (cur_term.type.Strings == null) {
//                        return false;
//                    }
//                }
//            }
//            return ((clr_eol != null) && (clr_eol != 0));
//        } else if (__PDCURSES__) {
//            return true;
//        } else {
//            return ((CE != null) && (CE != 0));
//        }
        return true;
    }

    static int md_getuid() {
        boolean HAVE_GETUID = false;
        if (HAVE_GETUID) {
            return 0;
            // return (getuid());
        } else {
            return 42;
        }
    }

    static int md_standout_mode = 0;

    static void md_raw_standend() {
        boolean _WIN32 = false;
        boolean SE = false;
        if (_WIN32) {
            // CONSOLE_SCREEN_BUFFER_INFO csbiInfo;
            // HANDLE hStdout;
            // WORD fgattr, bgattr;

            if (md_standout_mode == 1) {
                // hStdout = GetStdHandle(STD_OUTPUT_HANDLE);
                // GetConsoleScreenBufferInfo(hStdout,  csbiInfo);
                // fgattr = (csbiInfo.wAttributes & 0xF);
                // bgattr = (csbiInfo.wAttributes & 0xF0);
                // SetConsoleTextAttribute(hStdout, (fgattr << 4) | (bgattr >> 4));
                md_standout_mode = 0;
            }
        } else if (SE) {
            // tputs(SE, 0, md_putchar);
            // fflush(stdout);
        }
    }

    static String md_getrealname(int uid) {
        String uidstr;
        boolean _WIN32 = false;
        boolean DJGPP = true;
        if (!_WIN32 && !DJGPP) {
//            Passwd pp;
//
//            if ((pp = getpwuid(uid)) == null) {
//                uidstr = String.valueOf(uid);
//                return (uidstr);
//            } else
//                return (pp.pw_name);
            return ""; // 仮
        } else {
            uidstr = String.valueOf(uid);
            return (uidstr);
        }
    }


}
