package org.hiro;

import org.hiro.things.ObjectType;
import org.hiro.things.RingEnum;
import org.hiro.things.ThingImp;

public class RingMethod {

    /*
     * ring_num:
     *	Print ring bonuses
     */
    static String ring_num(ThingImp obj) {
        String buf;

        if (!(obj.contains_o_flags(Const.ISKNOW))) {
            return "";
        }
        switch (RingEnum.valueOf(String.valueOf(obj._o_which))) {
            case R_PROTECT:
            case R_ADDSTR:
            case R_ADDDAM:
            case R_ADDHIT:
                buf = " [" + WeaponMethod.num(obj._o_arm, 0, ObjectType.RING) + "]";
                break;
            default:
                buf = "";
        }
        return buf;
    }

    /*
     * ring_eat:
     *	How much food does this ring use up?
     */
    static int ring_eat(int hand) {
        ThingImp ring;

        if ((ring = Global.cur_ring[hand]) == null) {
            return 0;
        }
        int eat;
        int[] uses = {
                1,    /* R_PROTECT */         1,    /* R_ADDSTR */
                1,    /* R_SUSTSTR */        -3,    /* R_SEARCH */
                -5,    /* R_SEEINVIS */     0,    /* R_NOP */
                0,    /* R_AGGR */        -3,    /* R_ADDHIT */
                -3,    /* R_ADDDAM */         2,    /* R_REGEN */
                -2,    /* R_DIGEST */         0,    /* R_TELEPORT */
                1,    /* R_STEALTH */         1    /* R_SUSTARM */
        };
        if ((eat = uses[ring._o_which]) < 0) {
            eat = (Util.rnd(-eat) == 0 ? 1 : 0);
        }
        if (ring._o_which == RingEnum.R_DIGEST.getValue()) {
            eat = -eat;
        }
        return eat;
    }

    /*
     * ring_on:
     *	Put a ring on a hand
     */
    static void ring_on() {
        ThingImp obj;
        int ring;

        obj = Pack.get_item("put on", ObjectType.RING);
        /*
         * Make certain that it is somethings that we want to wear
         */
        if (obj == null) {
            return;
        }
        if (obj._o_type != ObjectType.RING) {
            if (!Global.terse) {
                IOUtil.msg("it would be difficult to wrap that around a finger");
            } else {
                IOUtil.msg("not a ring");
            }
            return;
        }

        /*
         * find out which hand to put it on
         */
        if (Misc.is_current(obj)) {
            return;
        }

        if (Global.cur_ring[Const.LEFT] == null && Global.cur_ring[Const.RIGHT] == null) {
            if ((ring = gethand()) < 0)
                return;
        } else if (Global.cur_ring[Const.LEFT] == null)
            ring = Const.LEFT;
        else if (Global.cur_ring[Const.RIGHT] == null)
            ring = Const.RIGHT;
        else {
            if (!Global.terse) {
                IOUtil.msg("you already have a ring on each hand");
            } else {
                IOUtil.msg("wearing two");
            }
            return;
        }
        Global.cur_ring[ring] = obj;

        /*
         * Calculate the effect it has on the poor guy.
         */
        RingEnum r = RingEnum.get(obj._o_which);
        switch (r) {
            case R_ADDSTR:
                Misc.chg_str(obj._o_arm);
                break;
            case R_SEEINVIS:
                Potions.invis_on();
                break;
            case R_AGGR:
                Misc.aggravate();
                break;
        }

        if (!Global.terse) {
            IOUtil.addmsg("you are now wearing ");
        }
        IOUtil.msg("%s (%c)", ThingMethod.inv_name(obj, true), obj._o_packch);
    }

    /*
     * ring_off:
     *	take off a ring
     */
    static void ring_off() {
        int ring;
        ThingImp obj;

        if (Global.cur_ring[Const.LEFT] == null && Global.cur_ring[Const.RIGHT] == null) {
            if (Global.terse) {
                IOUtil.msg("no rings");
            } else {
                IOUtil.msg("you aren't wearing any rings");
            }
            return;
        } else if (Global.cur_ring[Const.LEFT] == null)
            ring = Const.RIGHT;
        else if (Global.cur_ring[Const.RIGHT] == null)
            ring = Const.LEFT;
        else if ((ring = gethand()) < 0)
            return;
        Global.mpos = 0;
        obj = Global.cur_ring[ring];
        if (obj == null) {
            IOUtil.msg("not wearing such a ring");
            return;
        }
        if (ThingMethod.dropcheck(obj))
            IOUtil.msg("was wearing %s(%c)", ThingMethod.inv_name(obj, true), obj._o_packch);
    }

    /*
     * gethand:
     *	Which hand is the hero interested in?
     */
    static int gethand() {
        int c;

        for (; ; ) {
            if (Global.terse) {
                IOUtil.msg("left or right ring? ");
            } else {
                IOUtil.msg("left hand or right hand? ");
            }
            if ((c = IOUtil.readchar()) == Const.ESCAPE) {
                return -1;
            }
            Global.mpos = 0;
            if (c == 'l' || c == 'L')
                return Const.LEFT;
            else if (c == 'r' || c == 'R')
                return Const.RIGHT;
            if (Global.terse) {
                IOUtil.msg("L or R");
            } else {
                IOUtil.msg("please type L or R");
            }
        }
    }


}
