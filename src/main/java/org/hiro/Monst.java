package org.hiro;

import org.hiro.character.Human;
import org.hiro.character.StateEnum;
import org.hiro.map.AbstractCoordinate;
import org.hiro.map.Coordinate;
import org.hiro.map.RoomInfoEnum;
import org.hiro.output.Display;
import org.hiro.things.OriginalMonster;
import org.hiro.things.ThingImp;
import org.hiro.things.ringtype.AggravateMonsterRing;
import org.hiro.things.ringtype.ProtectionRing;
import org.hiro.things.ringtype.StealthRing;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class Monst {

    /*
     * randmonster:
     *	Pick a monster to show up.  The lower the level,
     *	the meaner the monster.
     */
    public static int randmonster(boolean wander) {
        /*
         * ListMethod of monsters in rough order of vorpalness
         */
        int[] lvl_mons = {
                'K', 'E', 'B', 'S', 'H', 'I', 'R', 'O', 'Z', 'L', 'C', 'Q', 'A',
                'N', 'Y', 'F', 'T', 'W', 'P', 'X', 'U', 'M', 'V', 'G', 'J', 'D'
        };

        int[] wand_mons = {
                'K', 'E', 'B', 'S', 'H', 0, 'R', 'O', 'Z', 0, 'C', 'Q', 'A',
                0, 'Y', 0, 'T', 'W', 'P', 0, 'U', 'M', 'V', 'G', 'J', 0
        };

        int[] mons = (wander ? wand_mons : lvl_mons);
        int d;
        do {
            d = Human.instance.getLevel() + (Util.rnd(10) - 6);
            if (d < 0) {
                d = Util.rnd(5);
            }
            if (d > 25) {
                d = Util.rnd(5) + 21;
            }
        } while (mons[d] == 0);
        return mons[d];
    }

    /*
     * new_monster:
     *	Pick a new monster and add it to the list
     */
    public static void new_monster(OriginalMonster tp, int type, AbstractCoordinate cp) {
        Monster mp;
        int lev_add;

        if ((lev_add = Human.instance.getLevel() - Const.AMULETLEVEL) < 0) {
            lev_add = 0;
        }
        Global.mlist.add(tp);
        tp.setType(type);
        tp.setDisplayTile(type);
        tp.setPosition(cp);
        Display.move(cp); //ncursesライブラリ move()
        tp.setFloorTile(Util.CCHAR(Display.inch()));
        tp.setRoom(Chase.roomin(cp));
        Util.getPlace(cp).p_monst = tp;
        mp = Global.monsters[tp.getType() - 'A'];
        tp.getStatus().s_lvl = mp.m_stats.s_lvl + lev_add;
        tp.getStatus().s_maxhp = tp.getStatus().s_hpt = Dice.roll(tp.getStatus().s_lvl, 8);
        tp.getStatus().s_arm = mp.m_stats.s_arm - lev_add;
        tp.getStatus().s_dmg = mp.m_stats.s_dmg; //  strcpy(tp._t_stats.s_dmg, mp.m_stats.s_dmg);
        tp.getStatus().s_str = mp.m_stats.s_str;
        tp.getStatus().s_exp = mp.m_stats.s_exp + lev_add * 10 + exp_add(tp);
        tp.setState(mp.m_flags);  // TODO:o_flagとt_flag共有を考えないと
        if (Human.instance.getLevel() > 29) {
            tp.addState(StateEnum.ISHASTE);
        }
        tp.slow();
        tp.setBaggage(new ArrayList<>());
        if (AggravateMonsterRing.isInclude(Human.instance.getRings())) {
            Chase.runto(cp);
        }
        if (type == 'X') {
            tp.setDisplayTile(Misc.rnd_thing().getValue());
        }
    }

    /*
     * expadd:
     *	Experience to add for this monster's level/hit points
     */
    static int exp_add(OriginalMonster tp) {
        int mod;

        if (tp.getStatus().s_lvl == 1) {
            mod = tp.getStatus().s_maxhp / 8;
        } else {
            mod = tp.getStatus().s_maxhp / 6;
        }

        if (tp.getStatus().s_lvl > 9) {
            mod *= 20;
        } else if (tp.getStatus().s_lvl > 6) {
            mod *= 4;
        }
        return mod;
    }

    /*
     * give_pack:
     *	Give a pack to a monster if it deserves one
     *
     */

    static void give_pack(OriginalMonster tp) {
        if (Human.instance.getLevel() >= Global.max_level
                && Util.rnd(100) < Global.monsters[tp.getType() - 'A'].m_carry) {
            tp.addItem(new ThingImp());
        }
    }

    /*
     * wake_monster:
     *	What to do when the hero steps next to a monster
     */
    static OriginalMonster wake_monster(int y, int x) {
        OriginalMonster tp;
        Room rp;
        int ch;
        String mname;
        boolean MASTER = false;

        if ((tp = Util.INDEX(y, x).p_monst) == null) {
            if (MASTER) {
//    msg("can't find monster in wake_monster");
            }
            return null;
        }

        ch = tp.getType();
        /*
         * Every time he sees mean monster, it might start chasing him
         */
        if (!tp.containsState(StateEnum.ISRUN) && Util.rnd(3) != 0
                && tp.containsState(StateEnum.ISMEAN) && !tp.containsState(StateEnum.ISHELD)
                && !StealthRing.isInclude(Human.instance.getRings()) && !Human.instance.containsState(StateEnum.ISLEVIT)) {
            tp.setRunPosition(Global.player._t_pos);
            tp.addState(StateEnum.ISRUN);
        }
        if (ch == 'M' && !Human.instance.containsState(StateEnum.ISBLIND) && !Human.instance.containsState(StateEnum.ISHALU)
                && !tp.containsState(StateEnum.ISFOUND) && !tp.containsState(StateEnum.ISCANC)
                && tp.containsState(StateEnum.ISRUN)) {
            rp = Human.instance.getRoom();
            if (rp == null || rp.containInfo(RoomInfoEnum.ISDARK)
                    || Chase.dist_cp(new Coordinate(x, y), Global.player._t_pos) < Const.LAMPDIST) {
                tp.addState(StateEnum.ISFOUND);
                if (!save(Const.VS_MAGIC)) {
                    try {
                        Method unconfuse = Daemon.class.getMethod("unconfuse");
                        if (Human.instance.containsState(StateEnum.ISHUH)) {
                            Daemon.lengthen(unconfuse, Misc.spread(Const.HUHDURATION));
                        } else {
                            Daemon.fuse(unconfuse, 0, Misc.spread(Const.HUHDURATION), Const.AFTER);
                        }
                    } catch (NoSuchMethodException ex) {
                        throw new RuntimeException("This is bug.", ex);
                    }
                    Human.instance.addState(StateEnum.ISHUH);
                    mname = Fight.set_mname(tp);
                    // addmsg("%s", mname);
                    if (!mname.equals("it")) {
                        // addmsg("'");
                    }
                    // msg("s gaze has confused you");
                }
            }
        }
        /*
         * Let greedy ones guard gold
         */
        if (tp.containsState(StateEnum.ISGREED) && !tp.containsState(StateEnum.ISRUN)) {
            tp.addState(StateEnum.ISRUN);
            if (Human.instance.getRoom().r_goldval != 0) {
                tp.setRunPosition(Human.instance.getRoom().r_gold);
            } else {
                tp.setRunPosition( Global.player._t_pos);
            }
        }
        return tp;
    }

    /*
     * save:
     *	See if he saves against various nasty things
     *  アイテムを落とすか確認する
     */
    static boolean save(int which) {
        if (which == Const.VS_MAGIC) {
            if (Global.cur_ring[Const.LEFT] instanceof ProtectionRing ) {
                which -= ((ProtectionRing)Global.cur_ring[Const.LEFT]).getDefence();
            }
            if (Global.cur_ring[Const.RIGHT] instanceof  ProtectionRing) {
                which -= ((ProtectionRing)Global.cur_ring[Const.RIGHT]).getDefence();
            }
        }
        return save_throw(which, Global.player);
    }


    /*
     * save_throw:
     *	See if a creature save against something
     */
    public static boolean save_throw(int which, OriginalMonster tp) {
        int need;

        need = 14 + which - tp.getStatus().s_lvl / 2;
        return (Dice.roll(1, 20) >= need);
    }

}
