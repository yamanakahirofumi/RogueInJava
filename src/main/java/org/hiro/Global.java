package org.hiro;

import org.hiro.map.AbstractCoordinate;
import org.hiro.things.ObjectType;
import org.hiro.things.OriginalMonster;
import org.hiro.things.PotionEnum;
import org.hiro.things.Ring;
import org.hiro.things.ScrollEnum;
import org.hiro.things.Thing;
import org.hiro.things.ThingImp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Global {
    static List<Room> rooms;
    // static boolean amulet = false;      /* He found the amulet */  // Game.goalに移行
    public static List<Place> places;  // sizeは80 * 32
//    static
    static Monster[] monsters;
    static int max_level;
    public static int no_food;  /* Number of levels without food */
    public static int group = 2;
    static int ntraps;                /* Number of traps on this level */
    public static boolean seenstairs;       /* Have seen the stairs (for lsd) */
    public static boolean after;
    static boolean running;
    static boolean jump;
    public static boolean again;
    static boolean has_hit;
    static int no_move;
    public static boolean firstmove;
    public static boolean to_death;
    public static int runch;
    static boolean passgo = false;  /* Follow passages */ // 通路の角で止まらない
    public static int count;  /* Number of times to repeat command */
    public static int no_command;
    static int purse;
    static boolean tombstone;
    static int numscores;
    static int mpos;
    public static boolean save_msg;
    static boolean lower_msg;
    public static boolean door_stop;
    static boolean see_floor;
    static int n_objs;
    public static char last_comm;
    static char l_last_comm;
    static char last_dir;
    static char l_last_dir;
    static boolean msg_esc;
    static int inv_type;
    static boolean inv_describe;
    public static boolean kamikaze;
    public static boolean q_comm;
    static int hungry_state;
    public static boolean stat_msg;
    static int inpack = 0;                    /* Number of things in pack */   // なくす方向で
    static int vf_hit;
    static int lastscore;
    static int quiet;
    static boolean fight_flush;
    static ObjectType take;
    public static boolean noscore;
    public static boolean move_on;
    public static int max_hit;                       /* Max damage done to her in to_death */
    public static int dir_ch;
    static int allscore;
    public static int food_left = Const.HUNGERTIME;

    static boolean use_savedir;

    static boolean[] pack_used;    // なくす方向で

    static int got_ltc;

    static int orig_dsusp;

    static int[] e_levels;

    static File logfi;
    static File scoreboard;

    static AbstractCoordinate stairs;  /* Location of staircase */
    public static AbstractCoordinate delta;
    static AbstractCoordinate oldpos;

    static Room oldrp;

    static String whoami;
    public static String prbuf;

    static final String encstr = "\300k||`\251Y.'\305\321\201+\277~r\"]\240_\223=1\341)\222\212\241t;\t$\270\314/<#\201\254";
    static final String statlist = "\355kl{+\204\255\313idJ\361\214=4:\311\271\341wK<\312\321\213,,7\271/Rk%\b\312\f\246";
    static final String version = "rogue (rogueforge) 09/05/07";
    public static final String release = "5.4.5";

    public static String huh;            /* The last message printed */

    public static int[] a_class = {		/* Armor class for each armor type */
            8,	/* LEATHER */
            7,	/* RING_MAIL */
            7,	/* STUDDED_LEATHER */
            6,	/* SCALE_MAIL */
            5,	/* CHAIN_MAIL */
            4,	/* SPLINT_MAIL */
            4,	/* BANDED_MAIL */
            3,	/* PLATE_MAIL */
    };
    public static boolean wizard;

    public static String[] ws_type;

    static char[] p_colors = new char[PotionEnum.getMaxValue()];        /* Colors of the potions */
    static char[] r_stones;
    static char[] ws_made;
    static String[] s_names= new String[ScrollEnum.getMaxValue()];  /* Names of the scrolls */
    public static String[] tr_name;
    static String[] rainbow;

    public static String fruit = "slime-mold";  /* Favorite fruit */

    public static List<Thing> lvl_obj = new ArrayList<>();  /* List of objects on this level */
    public static List<OriginalMonster> mlist = new ArrayList<>();  /* List of monsters on the level */
    public static Ring cur_ring[];
    @Deprecated
    public static ThingImp player;
    static Thing last_pick;   /* Last object picked in get_item() */   // なくす方向で
    static Thing l_last_pick;   // なくす方向で

    public static Obj_info[] things;
    public static Obj_info[] pot_info; //oi_knowをなんとかしないと
    public static Obj_info[] scr_info;
    public static Obj_info[] weap_info;
    public static Obj_info[] arm_info;
    public static Obj_info[] ring_info;
    public static Obj_info[] ws_info;

    public static Delayed_action[] d_list = new Delayed_action[Const.MAXDAEMONS];

    public static Room[] passages = new Room[Const.MAXPASS];    /* One for each passage */
//	{
//		{ {0, 0}, {0, 0}, {0, 0}, 0, Const.ISGONE|Const.ISDARK, 0, {{0,0}} },
//		{ {0, 0}, {0, 0}, {0, 0}, 0, Const.ISGONE|Const.ISDARK, 0, {{0,0}} },
//		{ {0, 0}, {0, 0}, {0, 0}, 0, Const.ISGONE|Const.ISDARK, 0, {{0,0}} },
//		{ {0, 0}, {0, 0}, {0, 0}, 0, Const.ISGONE|Const.ISDARK, 0, {{0,0}} },
//		{ {0, 0}, {0, 0}, {0, 0}, 0, Const.ISGONE|Const.ISDARK, 0, {{0,0}} },
//		{ {0, 0}, {0, 0}, {0, 0}, 0, Const.ISGONE|Const.ISDARK, 0, {{0,0}} },
//		{ {0, 0}, {0, 0}, {0, 0}, 0, Const.ISGONE|Const.ISDARK, 0, {{0,0}} },
//		{ {0, 0}, {0, 0}, {0, 0}, 0, Const.ISGONE|Const.ISDARK, 0, {{0,0}} },
//		{ {0, 0}, {0, 0}, {0, 0}, 0, Const.ISGONE|Const.ISDARK, 0, {{0,0}} },
//		{ {0, 0}, {0, 0}, {0, 0}, 0, Const.ISGONE|Const.ISDARK, 0, {{0,0}} },
//		{ {0, 0}, {0, 0}, {0, 0}, 0, Const.ISGONE|Const.ISDARK, 0, {{0,0}} },
//		{ {0, 0}, {0, 0}, {0, 0}, 0, Const.ISGONE|Const.ISDARK, 0, {{0,0}} }
//	}; //[MAXPASS]


    public static List<InitWeapon> init_dam;
//	{ "2x4",	"1x3",	-1,	0,		},	/* Mace */
//	{ "3x4",	"1x2",	-1,	0,		},	/* Long sword */
//	{ "1x1",	"1x1",	-1,	0,		},	/* Bow */
//	{ "1x1",	"2x3",	BOW,		ISMANY|ISMISL,	},	/* Arrow */
//	{ "1x6",	"1x4",	-1,	ISMISL|ISMISL,	},	/* Dagger */
//	{ "4x4",	"1x2",	-1,	0,		},	/* 2h sword */
//	{ "1x1",	"1x3",	-1,	ISMANY|ISMISL,	},	/* Dart */
//	{ "1x2",	"2x4",	-1,	ISMANY|ISMISL,	},	/* Shuriken */
//	{ "2x3",	"1x6",	-1,	ISMISL,		},	/* Spear */

    public static boolean terse = false;  /* True if we should be short */
    static String file_name;

    static boolean VDSUSP;
    static boolean TIOCSLTC;

    static Stats max_stats;

    public static List<Help_list> helpstr;
//    {
//        {'?',	"	prints help",				TRUE},
//        {'/',	"	identify object",			TRUE},
//        {'h',	"	left",					TRUE},
//        {'j',	"	down",					TRUE},
//        {'k',	"	up",					TRUE},
//        {'l',	"	right",					TRUE},
//        {'y',	"	up & left",				TRUE},
//        {'u',	"	up & right",				TRUE},
//        {'b',	"	down & left",				TRUE},
//        {'n',	"	down & right",				TRUE},
//        {'H',	"	run left",				FALSE},
//        {'J',	"	run down",				FALSE},
//        {'K',	"	run up",				FALSE},
//        {'L',	"	run right",				FALSE},
//        {'Y',	"	run up & left",				FALSE},
//        {'U',	"	run up & right",			FALSE},
//        {'B',	"	run down & left",			FALSE},
//        {'N',	"	run down & right",			FALSE},
//        {CTRL('H'),	"	run left until adjacent",		FALSE},
//        {CTRL('J'),	"	run down until adjacent",		FALSE},
//        {CTRL('K'),	"	run up until adjacent",			FALSE},
//        {CTRL('L'),	"	run right until adjacent",		FALSE},
//        {CTRL('Y'),	"	run up & left until adjacent",		FALSE},
//        {CTRL('U'),	"	run up & right until adjacent",		FALSE},
//        {CTRL('B'),	"	run down & left until adjacent",	FALSE},
//        {CTRL('N'),	"	run down & right until adjacent",	FALSE},
//        {'\0',	"	<SHIFT><dir>: run that way",		TRUE},
//        {'\0',	"	<CTRL><dir>: run till adjacent",	TRUE},
//        {'f',	"<dir>	fight till death or near death",	TRUE},
//        {'t',	"<dir>	throw something",			TRUE},
//        {'m',	"<dir>	move onto without picking up",		TRUE},
//        {'z',	"<dir>	zap a wand in a direction",		TRUE},
//        {'^',	"<dir>	identify trap type",			TRUE},
//        {'s',	"	search for trap/secret door",		TRUE},
//        {'>',	"	go down a staircase",			TRUE},
//        {'<',	"	go up a staircase",			TRUE},
//        {'.',	"	rest for a turn",			TRUE},
//        {',',	"	pick something up",			TRUE},
//        {'i',	"	inventory",				TRUE},
//        {'I',	"	inventory single item",			TRUE},
//        {'q',	"	quaff potion",				TRUE},
//        {'r',	"	read scroll",				TRUE},
//        {'e',	"	eat food",				TRUE},
//        {'w',	"	wield a weapon",			TRUE},
//        {'W',	"	wear armor",				TRUE},
//        {'T',	"	take armor off",			TRUE},
//        {'P',	"	put on ring",				TRUE},
//        {'R',	"	remove ring",				TRUE},
//        {'d',	"	drop object",				TRUE},
//        {'c',	"	call object",				TRUE},
//        {'a',	"	repeat last command",			TRUE},
//        {')',	"	print current weapon",			TRUE},
//        {']',	"	print current armor",			TRUE},
//        {'=',	"	print current rings",			TRUE},
//        {'@',	"	print current stats",			TRUE},
//        {'D',	"	recall what's been discovered",		TRUE},
//        {'o',	"	examine/set options",			TRUE},
//        {CTRL('R'),	"	redraw screen",				TRUE},
//        {CTRL('P'),	"	repeat last message",			TRUE},
//        {ESCAPE,	"	cancel command",			TRUE},
//        {'S',	"	save game",				TRUE},
//        {'Q',	"	quit",					TRUE},
//        {'!',	"	shell escape",				TRUE},
//        {'F',	"<dir>	fight till either of you dies",		TRUE},
//        {'v',	"	print version number",			TRUE},
//        {0,		NULL }
//    }

}
