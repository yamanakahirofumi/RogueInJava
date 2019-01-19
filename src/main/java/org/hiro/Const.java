package org.hiro;

import java.util.List;

public class Const {

    /*
     * 過渡期
     */

    /**
     * 移行組
     */
    final public static int MAXROOMS = 9;
    final static int NUMCOLS = 80;
    final static int NUMLINES = 24;

    final static int F_PASS = 0x80;        /* is a passageway */
    static int F_SEEN = 0x40;        /* have seen this spot before */
    static int F_DROPPED = 0x20;        /* object was dropped here */
    static int F_LOCKED = 0x20;        /* door is locked */
    static int F_REAL = 0x10;        /* what you see is what you get */
    public static int F_PNUM = 0x0f;        /* passage number mask */
    static int F_TMASK = 0x07;        /* trap number mask */


    static int LAMPDIST = 3;


    static int CALLABLE = -1;
    static int R_OR_S = -2;


    public static int ISCURSED = 000001;        /* object is cursed */ // 呪い
    public static int ISKNOW = 0000002;        /* player knows details about the object */
    public static int ISMISL = 0000004;        /* object is a missile type */
    public static int ISMANY = 0000010;    /* object comes in groups */


    /*
     * Maximum number of different things
     */
    static int MAXTHINGS = 9;
    static int MAXOBJ = 9;
    static int MAXPACK = 23;
    static int MAXTRAPS = 10;
    static int AMULETLEVEL = 26;
    public static int NUMTHINGS = 7;    /* number of types of things */
    static int MAXPASS = 13;    /* upper limit on number of passages */
    static int STATLINE = (NUMLINES - 1);
    static int BORE_LEVEL = 50;


    static int LEFT = 0;
    static int RIGHT = 1;


    static int MAXSTR = 1024;    /* maximum length of strings */
    static int MAXLINES = 32;    /* maximum number of screen lines used */
    static int MAXCOLS = 80;    /* maximum number of screen columns used */


    /* flags for creatures */
//    public static int CANHUH = 0000001;        /* creature can confuse */
//    public static int CANSEE = 0000002;        /* creature can see invisible creatures */
//    public static int ISBLIND = 0000004;        /* creature is blind */
//    public static int ISCANC = 0000010;        /* creature has special qualities cancelled */
//    public static int ISLEVIT = 0000010;        /* hero is levitating */
//    public static int ISFOUND = 0000020;        /* creature has been seen (used for objects) */
//    public static int ISGREED = 0000040;        /* creature runs to protect gold */
//    public static int ISHASTE = 0000100;        /* creature has been hastened */
//    public static int ISTARGET = 000200;        /* creature is the target of an 'f' command */
//    public static int ISHELD = 0000400;        /* creature has been held */
//    public static int ISHUH = 0001000;    /* creature is confused */
//    public static int ISINVIS = 0002000;    /* creature is invisible */
//    public static int ISMEAN = 0004000;    /* creature can wake when player enters room */
//    public static int ISHALU = 0004000;    /* hero is on acid trip */
//    public static int ISREGEN = 0010000;    /* creature can regenerate */
//    public static int ISRUN = 0020000;    /* creature is running at the player */
//    public static int SEEMONST = 040000;    /* hero can detect unseen monsters */
//    public static int ISFLY = 0040000;    /* creature can fly */
//    public static int ISSLOW = 0100000;    /* creature has been slowed */


    /*
     * Trap types
     */
    public static final int T_DOOR = 00;
    public static final int T_ARROW = 01;
    public static final int T_SLEEP = 02;
    public static final int T_BEAR = 03;
    public static final int T_TELEP = 04;
    public static final int T_DART = 05;
    public static final int T_RUST = 06;
    public static final int T_MYST = 07;
    public static int NTRAPS = 8;


    /*
     * Save against things
     */
    public static int VS_POISON = 00;
    public static int VS_PARALYZATION = 0;
    public static int VS_DEATH = 00;
    public static int VS_BREATH = 02;
    public static int VS_MAGIC = 03;


    public static int MAXDAEMONS = 20;
    public static int EMPTY = 0;


    public static int HEALTIME = 30;
    public static int HUHDURATION = 20;
    public static int SEEDURATION = 850;
    @Deprecated
    public static int HUNGERTIME = 1300;
    public static int MORETIME = 150;
    @Deprecated
    public static int STOMACHSIZE = 2000; // ->移動済み
    public static int STARVETIME = 850;
    public static final int ESCAPE = 27;
    public static int BOLT_LENGTH = 6;


    /*
     * Various constants
     */
    public static int BEARTIME = Misc.spread(3);
    public static int SLEEPTIME = Misc.spread(5);
    public static int HOLDTIME = Misc.spread(2);
    public static int WANDERTIME = Misc.spread(70);
    public static int BEFORE = Misc.spread(1);
    public static int AFTER = Misc.spread(2);


    /*
     * inventory types
     */
    public static int INV_OVER = 0;
    public static int INV_SLOW = 1;
    public static int INV_CLEAR = 2;


    /*
     * return values for get functions
     */
    public static int NORM = 0;    /* normal exit */
    public static int QUIT = 1;    /* quit option setting */
    public static int MINUS = 2;    /* back up one option */


    public static int ISPROT = 0000040;  /* armor is permanently protected */


    public static List<String> inv_t_name = List.of("Overwrite", "Slow", "Clear");

    public static String Numname="Ten";
}
