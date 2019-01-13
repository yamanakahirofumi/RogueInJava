package org.hiro;

import org.hiro.map.Coordinate;
import org.hiro.things.ObjectType;
import org.hiro.things.PotionEnum;
import org.hiro.things.ScrollEnum;
import org.hiro.things.ThingImp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Global {
    static List<Room> rooms;
    // static boolean amulet = false;      /* He found the amulet */  // Game.goalに移行
    static List<Place> places;  // sizeは80 * 32
//    static
    static Monster[] monsters;
    static int max_level;
    public static int no_food;  /* Number of levels without food */
    public static int group = 2;
    static int ntraps;                /* Number of traps on this level */
    static boolean seenstairs;       /* Have seen the stairs (for lsd) */
    static boolean after;
    static boolean running;
    static boolean jump;
    static boolean again;
    static boolean has_hit;
    static int no_move;
    static boolean firstmove;
    static boolean to_death;
    static int runch;
    static boolean passgo = false;  /* Follow passages */ // 通路の角で止まらない
    static int count;  /* Number of times to repeat command */
    static int no_command;
    static int purse;
    static boolean tombstone;
    static int numscores;
    static int mpos;
    static boolean save_msg;
    static boolean lower_msg;
    static boolean door_stop;
    static boolean see_floor;
    static int n_objs;
    static char last_comm;
    static char l_last_comm;
    static char last_dir;
    static char l_last_dir;
    static boolean msg_esc;
    static int inv_type;
    static boolean inv_describe;
    static boolean kamikaze;
    static boolean q_comm;
    static int hungry_state;
    static boolean stat_msg;
    static int inpack = 0;                    /* Number of things in pack */
    static int vf_hit;
    static int lastscore;
    static int quiet;
    static boolean fight_flush;
    static ObjectType take;
    static boolean noscore;
    static boolean move_on;
    static int max_hit;                       /* Max damage done to her in to_death */
    static int dir_ch;
    static int allscore;
    static int food_left = Const.HUNGERTIME;

    static boolean use_savedir;

    static boolean[] pack_used;

    static int got_ltc;

    static int orig_dsusp;

    static int[] e_levels;

    static File logfi;
    static File scoreboard;

    static Coordinate stairs;
    static Coordinate delta;
    static Coordinate oldpos;

    static Room oldrp;

    static String whoami;
    static String prbuf;

    static final String encstr = "\300k||`\251Y.'\305\321\201+\277~r\"]\240_\223=1\341)\222\212\241t;\t$\270\314/<#\201\254";
    static final String statlist = "\355kl{+\204\255\313idJ\361\214=4:\311\271\341wK<\312\321\213,,7\271/Rk%\b\312\f\246";
    static final String version = "rogue (rogueforge) 09/05/07";
    static final String release = "5.4.5";

    static String huh;            /* The last message printed */

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
    static boolean wizard;

    static String[] ws_type;

    static char[] p_colors = new char[PotionEnum.getMaxValue()];        /* Colors of the potions */
    static char[] r_stones;
    static char[] ws_made;
    static String[] s_names= new String[ScrollEnum.getMaxValue()];  /* Names of the scrolls */
    static String[] tr_name;
    static String[] rainbow;

    static String fruit = "slime-mold";  /* Favorite fruit */

    static List<ThingImp> lvl_obj = new ArrayList<>();  /* List of objects on this level */
    static List<ThingImp> mlist = new ArrayList<>();  /* List of monsters on the level */
    static ThingImp cur_ring[];
    @Deprecated
    static ThingImp player;
    static ThingImp cur_armor;
    static ThingImp last_pick;   /* Last object picked in get_item() */
    static ThingImp l_last_pick;

    public static Obj_info[] things;
    public static Obj_info[] pot_info; //oi_knowをなんとかしないと
    public static Obj_info[] scr_info;
    public static Obj_info[] weap_info;
    public static Obj_info[] arm_info;
    public static Obj_info[] ring_info;
    public static Obj_info[] ws_info;

    public static Delayed_action[] d_list = new Delayed_action[Const.MAXDAEMONS];

    static Room[] passages = new Room[Const.MAXPASS];    /* One for each passage */
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
//	{ "2x4",	"1x3",	NO_WEAPON,	0,		},	/* Mace */
//	{ "3x4",	"1x2",	NO_WEAPON,	0,		},	/* Long sword */
//	{ "1x1",	"1x1",	NO_WEAPON,	0,		},	/* Bow */
//	{ "1x1",	"2x3",	BOW,		ISMANY|ISMISL,	},	/* Arrow */
//	{ "1x6",	"1x4",	NO_WEAPON,	ISMISL|ISMISL,	},	/* Dagger */
//	{ "4x4",	"1x2",	NO_WEAPON,	0,		},	/* 2h sword */
//	{ "1x1",	"1x3",	NO_WEAPON,	ISMANY|ISMISL,	},	/* Dart */
//	{ "1x2",	"2x4",	NO_WEAPON,	ISMANY|ISMISL,	},	/* Shuriken */
//	{ "2x3",	"1x6",	NO_WEAPON,	ISMISL,		},	/* Spear */

    static boolean terse = false;  /* True if we should be short */
    static String file_name;

    static boolean VDSUSP;
    static boolean TIOCSLTC;

    static Stats max_stats;

}
