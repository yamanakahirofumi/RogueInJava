package org.hiro;

import org.hiro.character.Human;
import org.hiro.character.Player;
import org.hiro.things.ObjectType;
import org.hiro.things.Ring;
import org.hiro.things.RingEnum;
import org.hiro.things.Thing;
import org.hiro.things.ThingImp;
import org.hiro.things.ringtype.SlowDigestionRing;

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
            case Protection:
            case AddStrength:
            case AddDamage:
            case Dexterity:
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
                1,    /* Protection */         1,    /* AddStrength */
                1,    /* SustainStrength */        -3,    /* Searching */
                -5,    /* SeeInvisible */     0,    /* Adornment */
                0,    /* AggravateMonster */        -3,    /* Dexterity */
                -3,    /* AddDamage */         2,    /* Regeneration */
                -2,    /* SlowDigestion */         0,    /* Teleportation */
                1,    /* Stealth */         1    /* MaintainArmor */
        };
        if ((eat = uses[ring._o_which]) < 0) {
            eat = (Util.rnd(-eat) == 0 ? 1 : 0);
        }
        if (ring instanceof SlowDigestionRing) {
            eat = -eat;
        }
        return eat;
    }

    /*
     * ring_on:
     *	Put a ring on a hand
     */
    public static void ring_on(Player player, Thing obj) {
        /*
         * Make certain that it is somethings that we want to wear
         */
        if (obj == null) {
            return;
        }
        if (!(obj instanceof Ring)) {
            if (!Global.terse) {
                IOUtil.msg("it would be difficult to wrap that around a finger");
            } else {
                IOUtil.msg("not a ring");
            }
            return;
        }
        Ring ringObject = (Ring) obj;

        /*
         * find out which hand to put it on
         */
        if (Misc.is_current(ringObject)) {
            return;
        }


        boolean result = player.putOnRing(ringObject);
        if(!result) {
            if (!Global.terse) {
                IOUtil.msg("you already have a ring on each hand");
            } else {
                IOUtil.msg("wearing two");
            }
            return;
        }

        /*
         * Calculate the effect it has on the poor guy.
         */
        RingEnum r = RingEnum.get(ringObject._o_which);
        switch (r) {
            case AddStrength:
                Misc.chg_str(ringObject._o_arm);
                break;
            case SeeInvisible:
                Potions.invis_on();
                break;
            case AggravateMonster:
                Misc.aggravate();
                break;
        }

        if (!Global.terse) {
            IOUtil.addmsg("you are now wearing ");
        }
        IOUtil.msg("%s (%c)", ThingMethod.inventoryName(ringObject, true),
                player.getPositionOfContent(ringObject));
    }

    /*
     * ring_off:
     *	take off a ring
     */
    public static void ring_off() {
        int ring;

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
        Ring obj = Global.cur_ring[ring];
        if (obj == null) {
            IOUtil.msg("not wearing such a ring");
            return;
        }
        if (ThingMethod.isDrop(obj))
            IOUtil.msg("was wearing %s(%c)", ThingMethod.inventoryName(obj, true),
                    Human.instance.getPositionOfContent(obj));
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
