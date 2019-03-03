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
import org.hiro.things.ringtype.AddStrengthRing;
import org.hiro.things.ringtype.SustainStrengthRing;

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
        switch (p) {
            case P_CONFUSE:
                do_pot(p, !trip);
                break;
            case P_POISON:
                Global.pot_info[p.getValue()].know();
                if (SustainStrengthRing.isInclude(player.getRings())) {
                    IOUtil.msg("you feel momentarily sick");
                } else {
                    Misc.chg_str(-(Util.rnd(3) + 1));
                    IOUtil.msg("you feel very sick now");
                    Daemons.come_down(player);
                }
                break;
            case P_HEALING:
                Global.pot_info[p.getValue()].know();
                player.addHp(Dice.roll(Global.player.getStatus().s_lvl, 4));
                if (player.getHp() > player.getMaxHp()) {
                    Global.player.getStatus().s_hpt = ++Global.player.getStatus().s_maxhp;
                }
                Daemons.sight();
                IOUtil.msg("you begin to feel better");
                break;
            case P_STRENGTH:
                Global.pot_info[p.getValue()].know();
                Misc.chg_str(1);
                IOUtil.msg("you feel stronger, now.  What bulging muscles!");
                break;
            case P_MFIND:
                player.addState(StateEnum.SEEMONST);
                try {
                    Method m = Potions.class.getMethod("turn_see_off");
                    Daemon.fuse(m, 1, Const.HUHDURATION, Const.AFTER);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                if (turn_see(false) == 0) {
                    IOUtil.msg("you have a %s feeling for a moment, then it passes",
                            Misc.choose_str("normal", "strange"));
                }
                break;
            case P_TFIND:
                /*
                 * Potion of magic detection.  Show the potions and scrolls
                 */
                show = false;
                if (Global.lvl_obj.size() != 0) {
                    // wclear(hw);
                    for (Thing tp : Global.lvl_obj) {
                        if (tp.isMagic()) {
                            show = true;
                            // wmove(hw, tp._o_pos.y, tp._o_pos.x);
                            // waddch(hw, ObjectType.MAGIC);
                            Global.pot_info[p.getValue()].know();
                        }
                    }
                    for (OriginalMonster mp : Global.mlist) {
                        for (Thing tp : mp.getBaggage()) {
                            if (tp.isMagic()) {
                                show = true;
                                // wmove(hw, mp._t_pos.y, mp._t_pos.x);
                                // waddch(hw, ObjectType.MAGIC);
                            }
                        }
                    }
                }
                if (show) {
                    Global.pot_info[p.getValue()].know();
                    IOUtil.show_win("You sense the presence of magic on this level.--More--");
                } else {
                    IOUtil.msg("you have a %s feeling for a moment, then it passes",
                            Misc.choose_str("normal", "strange"));
                }
                break;
            case P_LSD:
                if (!trip) {
                    if (player.containsState(StateEnum.SEEMONST)) {
                        turn_see(false);
                    }
                    try {
                        Method m = Daemons.class.getMethod("visuals", Player.class);
                        Daemon.start_daemon(m, 0, Const.BEFORE);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    Global.seenstairs = seen_stairs();
                }
                do_pot(p, true);
                break;
            case P_SEEINVIS:
                Global.prbuf = "this potion tastes like " + Global.fruit + " juice";
                show = player.containsState(StateEnum.CANSEE);
                do_pot(p, false);
                if (!show) {
                    invis_on();
                }
                Daemons.sight();
                break;
            case P_RAISE:
                Global.pot_info[p.getValue()].know();
                IOUtil.msg("you suddenly feel much more skillful");
                raise_level();
                break;
            case P_XHEAL:
                Global.pot_info[p.getValue()].know();
                player.addHp(Dice.roll(Global.player.getStatus().s_lvl, 8));
                if (player.getHp() > player.getMaxHp()) {
                    if (player.getHp() > player.getMaxHp() + Global.player.getStatus().s_lvl + 1) {
                        ++Global.player.getStatus().s_maxhp;
                    }
                    Global.player.getStatus().s_hpt = ++Global.player.getStatus().s_maxhp;
                }
                Daemons.sight();
                Daemons.come_down(player);
                IOUtil.msg("you begin to feel much better");
                break;
            case P_HASTE:
                Global.pot_info[p.getValue()].know();
                Global.after = false;
                if (Misc.add_haste(true))
                    IOUtil.msg("you feel yourself moving much faster");
                break;
            case P_RESTORE:
                if (Global.cur_ring[Const.LEFT] instanceof AddStrengthRing) {
                    Global.player.getStatus().s_str = Misc.add_str(player.getCurrentStrength(), -((AddStrengthRing)Global.cur_ring[Const.LEFT]).getStrength());
                }
                if (Global.cur_ring[Const.RIGHT] instanceof  AddStrengthRing) {
                    Global.player.getStatus().s_str = Misc.add_str(player.getCurrentStrength(), -((AddStrengthRing)Global.cur_ring[Const.RIGHT]).getStrength());
                }
                if (player.getCurrentStrength() < player.getMaxStrength()) {
                    Global.player.getStatus().s_str = player.getMaxStrength();
                }
                if (Global.cur_ring[Const.LEFT] instanceof AddStrengthRing) {
                    Global.player.getStatus().s_str = Misc.add_str(player.getCurrentStrength(), ((AddStrengthRing)Global.cur_ring[Const.LEFT]).getStrength());
                }
                if (Global.cur_ring[Const.RIGHT] instanceof AddStrengthRing) {
                    Global.player.getStatus().s_str = Misc.add_str(player.getCurrentStrength(), ((AddStrengthRing)Global.cur_ring[Const.RIGHT]).getStrength());
                }
                IOUtil.msg("hey, this tastes great.  It make you feel warm all over");
                break;
            case P_BLIND:
                do_pot(p, true);
                break;
            case P_LEVIT:
                do_pot(p, true);
                break;
            default:
                if (MASTER) {
                    IOUtil.msg("what an odd tasting potion!");
                    return;
                }
        }
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
    static void do_pot(PotionEnum type, boolean knowit) {

        List<Pact> p_actions = new ArrayList<>();
        try {
            {
                /* P_CONFUSE */
                Method m = Daemon.class.getMethod("unconfuse");
                Pact p = new Pact(StateEnum.ISHUH, m, Const.HUHDURATION, "what a tripy feeling!", "wait, what's going on here. Huh? What? Who?");
                p_actions.add(p);
            }
            {
                /* P_LSD */
                Method m = Daemon.class.getMethod("come_down");
                Pact p = new Pact(StateEnum.ISHALU, m, Const.SEEDURATION, "Oh, wow!  Everything seems so cosmic!", "Oh, wow!  Everything seems so cosmic!");
                p_actions.add(p);
            }
            {
                /* P_POISON */
                Pact p = new Pact(StateEnum.NoChange, null, 0, "", "");
                p_actions.add(p);
            }
            {
                /* P_STRENGTH */
                Pact p = new Pact(StateEnum.NoChange, null, 0, "", "");
                p_actions.add(p);
            }
            {
                /* P_SEEINVIS */
                Method m = Daemons.class.getMethod("unsee");
                Pact p = new Pact(StateEnum.CANSEE, m, Const.SEEDURATION, Global.prbuf, Global.prbuf);
                p_actions.add(p);
            }
            {
                /* P_HEALING */
                Pact p = new Pact(StateEnum.NoChange, null, 0, "", "");
                p_actions.add(p);
            }
            {
                /* P_MFIND */
                Pact p = new Pact(StateEnum.NoChange, null, 0, "", "");
                p_actions.add(p);
            }
            {
                /* P_TFIND  */
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
                /* P_BLIND */
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
    static void invis_on() {
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
    static boolean seen_stairs() {

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
