package org.hiro;

import org.hiro.character.Human;
import org.hiro.character.Player;
import org.hiro.character.StateEnum;
import org.hiro.output.Display;
import org.hiro.things.ObjectType;
import org.hiro.things.OriginalMonster;
import org.hiro.things.Potion;
import org.hiro.things.PotionEnum;
import org.hiro.things.Thing;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Function(s) for dealing with potions
 */
public class Potions {

    /*
     * turn_see:
     *	Put on or off seeing monsters on this level
     */
    public static int turn_see(boolean turn_off) {

        int add_new = 0;
        for (OriginalMonster mp : Global.mlist) {
            Display.move(mp.getPosition());
            boolean can_see = Chase.see_monst(mp);
            if (turn_off) {
                if (!can_see) {
                    Display.addch((char) mp.getFloorTile());
                }
            } else {
                if (!can_see) {
                    Display.standout();
                }
                if (!Human.instance.containsState(StateEnum.ISHALU)) {
                    Display.addch((char) mp.getType());
                } else {
                    Display.addch((char) (Util.rnd(26) + 'A'));
                }
                if (!can_see) {
                    Display.standend();
                    add_new++;
                }
            }
        }
        if (turn_off) {
            Human.instance.removeState(StateEnum.SEEMONST);
        } else {
            Human.instance.addState(StateEnum.SEEMONST);
        }
        return add_new;
    }

    /*
     * quaff:
     *	Quaff a potion from the pack
     */
    public static void quaff(Player player, Thing obj) {
        boolean MASTER = false;

        /*
         * Make certain that it is somethings that we want to drink
         */
        if (obj == null) {
            return;
        }
        if (!(obj instanceof Potion)) {
            if (!Global.terse) {
                IOUtil.msg("yuk! Why would you want to drink that?");
            } else {
                IOUtil.msg("that's undrinkable");
            }
            return;
        }
        Potion potion = (Potion) obj;
        player.removeWeapon(potion);

        /*
         * Calculate the effect it has on the poor guy.
         */
        boolean trip = player.containsState(StateEnum.ISHALU);
        Pack.leave_pack(potion, false, false);
        PotionEnum p = PotionEnum.get(potion._o_which);
        boolean show;
        potion.quaff(player);
        IOUtil.status(player);
        /*
         * Throw the item away
         */


    }

    /*
     * do_pot:
     *	Do a potion with standard setup.  This means it uses a fuse and
     *	turns on a flag
     */
    public static void do_pot(PotionEnum type, boolean knowit) {

        List<Pact> p_actions = new ArrayList<>();
        try {
            {
                /* Confuse */
                Method m = Daemon.class.getMethod("unconfuse");
                Pact p = new Pact(StateEnum.ISHUH, m, Const.HUHDURATION, "what a tripy feeling!", "wait, what's going on here. Huh? What? Who?");
                p_actions.add(p);
            }
            {
                /* LSD */
                Method m = Daemon.class.getMethod("come_down");
                Pact p = new Pact(StateEnum.ISHALU, m, Const.SEEDURATION, "Oh, wow!  Everything seems so cosmic!", "Oh, wow!  Everything seems so cosmic!");
                p_actions.add(p);
            }
            {
                /* Poison */
                Pact p = new Pact(StateEnum.NoChange, null, 0, "", "");
                p_actions.add(p);
            }
            {
                /* Strength */
                Pact p = new Pact(StateEnum.NoChange, null, 0, "", "");
                p_actions.add(p);
            }
            {
                /* SeeInvisible */
                Method m = Daemons.class.getMethod("unsee");
                Pact p = new Pact(StateEnum.CANSEE, m, Const.SEEDURATION, Global.prbuf, Global.prbuf);
                p_actions.add(p);
            }
            {
                /* Healing */
                Pact p = new Pact(StateEnum.NoChange, null, 0, "", "");
                p_actions.add(p);
            }
            {
                /* MonsterFind */
                Pact p = new Pact(StateEnum.NoChange, null, 0, "", "");
                p_actions.add(p);
            }
            {
                /* TrapFind  */
                Pact p = new Pact(StateEnum.NoChange, null, 0, "", "");
                p_actions.add(p);
            }
            {
                /* P_RAISE */
                Pact p = new Pact(StateEnum.NoChange, null, 0, "", "");
                p_actions.add(p);
            }
            {
                /* P_XHEAL */
                Pact p = new Pact(StateEnum.NoChange, null, 0, "", "");
                p_actions.add(p);
            }
            {
                /* P_HASTE */
                Pact p = new Pact(StateEnum.NoChange, null, 0, "", "");
                p_actions.add(p);
            }
            {
                /* P_RESTORE */
                Pact p = new Pact(StateEnum.NoChange, null, 0, "", "");
                p_actions.add(p);
            }
            {
                /* Blind */
                Method m = Daemons.class.getMethod("sight");
                Pact p = new Pact(StateEnum.ISBLIND, m, Const.SEEDURATION, "oh, bummer!  Everything is dark!  Help!", "a cloak of darkness falls around you");
                p_actions.add(p);
            }
            {
                /* P_LEVIT */
                Method m = Daemons.class.getMethod("land");
                Pact p = new Pact(StateEnum.ISLEVIT, m, Const.HEALTIME, "oh, wow!  You're floating in the air!", "you start to float in the air");
                p_actions.add(p);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        Pact pp = p_actions.get(type.getValue());
        if (!Global.pot_info[type.getValue()].isKnown()) {
            Global.pot_info[type.getValue()].setKnown(knowit);
        }
        int t = Misc.spread(pp.pa_time);
        if (!Human.instance.containsState(pp.pa_flags)) {
            Human.instance.addState(pp.pa_flags);
            Daemon.fuse(pp.pa_daemon, 0, t, Const.AFTER);
            Misc.look(false);
        } else {
            Daemon.lengthen(pp.pa_daemon, t);
        }
        IOUtil.msg(Misc.choose_str(pp.pa_high, pp.pa_straight));
    }

    /*
     * raise_level:
     *	The guy just magically went up a level.
     */
    public static void raise_level() {
        Global.player.getStatus().s_exp = Global.e_levels[Global.player.getStatus().s_lvl - 1] + 1L;
        Misc.check_level();
    }

    /*
     * A wrapper for turn_see(TRUE), intended to be a fuse.
     */
    static void turn_see_off() {
        turn_see(true);
    }

    /*
     * invis_on:
     *	Turn on the ability to see invisible
     */
    public static void invis_on() {
        Human.instance.addState(StateEnum.CANSEE);
        for (OriginalMonster mp : Global.mlist) {
            if (mp.containsState(StateEnum.ISINVIS) && Chase.see_monst(mp)
                    && !Human.instance.containsState(StateEnum.ISHALU)) {
                Display.mvaddch(mp.getPosition(), (char) mp.getDisplayTile());
            }
        }
    }

    /*
     * seen_stairs:
     *	Return TRUE if the player has seen the stairs
     */
    public static boolean seen_stairs() {

        Display.move(Global.stairs);
        if (Util.CCHAR(Display.inch()) == ObjectType.STAIRS.getValue()) {            /* it's on the map */
            return true;
        }
        if (Global.player._t_pos.equals(Global.stairs)) {            /* It's under him */
            return true;
        }

        /*
         * if a monster is on the stairs, this gets hairy
         */
        OriginalMonster tp = Util.getPlace(Global.stairs).p_monst;
        if (tp != null) {
            if (Chase.see_monst(tp) && tp.containsState(StateEnum.ISRUN)) {    /* if it's visible and awake */
                return true;            /* it must have moved there */
            }

            /* if she can detect monster */
            /* and there once were stairs */
            /* it must have moved there */
            return Human.instance.containsState(StateEnum.SEEMONST)
                    && tp.getFloorTile() == ObjectType.STAIRS.getValue();
        }
        return false;
    }

}
