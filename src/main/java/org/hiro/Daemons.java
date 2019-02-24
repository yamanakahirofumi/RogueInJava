package org.hiro;

import org.hiro.character.Human;
import org.hiro.character.Player;
import org.hiro.character.StateEnum;
import org.hiro.map.RoomInfoEnum;
import org.hiro.output.Display;
import org.hiro.things.OriginalMonster;
import org.hiro.things.ThingImp;
import org.hiro.things.ringtype.RegenerationRing;

import java.lang.reflect.Method;

public class Daemons {


    /*
     * visuals:
     *	change the characters for the player
     */
    static void visuals() {

        if (!Global.after || (Global.running && Global.jump)) {
            return;
        }
        /*
         * change the things
         */
        for (ThingImp tp : Global.lvl_obj) {
            if (Chase.isSee(tp._o_pos)) {
                Display.mvaddch(tp._o_pos, Misc.rnd_thing().getValue());
            }
        }

        /*
         * change the stairs
         */
        if (!Global.seenstairs && Chase.isSee(Global.stairs)) {
            Display.mvaddch(Global.stairs, Misc.rnd_thing().getValue());
        }

        /*
         * change the monsters
         */
        boolean seemonst = Human.instance.containsState(StateEnum.SEEMONST);
        for (OriginalMonster tp : Global.mlist) {
            Display.move(tp.getPosition());
            if (Chase.see_monst(tp)) {
                if (tp.getType() == 'X' && tp.getDisplayTile() != 'X') {
                    Display.addch(Misc.rnd_thing().getValue());
                } else {
                    Display.addch((char) (Util.rnd(26) + 'A'));
                }
            } else if (seemonst) {
                Display.standout();
                Display.addch((char) (Util.rnd(26) + 'A'));
                Display.standend();
            }
        }
    }

    /*
     * unsee:
     *	Turn off the ability to see invisible
     */
    static void unsee() {
        for (OriginalMonster th : Global.mlist) {
            if (th.containsState(StateEnum.ISINVIS) && Chase.see_monst(th)) {
                Display.mvaddch(th.getPosition(), (char) th.getFloorTile());
            }
        }
        Human.instance.removeState(StateEnum.CANSEE);
    }

    /*
     * sight:
     *	He gets his sight back
     */
    static void sight() {
        if (Human.instance.containsState(StateEnum.ISBLIND)) {
            try {
                Method m = Daemons.class.getMethod("sight");
                Daemon.extinguish(m);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            Human.instance.removeState(StateEnum.ISBLIND);
            if (!Human.instance.getRoom().containInfo(RoomInfoEnum.ISGONE)) {
                Rooms.enter_room(Global.player._t_pos);
            }
            IOUtil.msg(Misc.choose_str("far out!  Everything is all cosmic again",
                    "the veil of darkness lifts"));
        }
    }

    /*
     * land:
     *	Land from a levitation potion
     */
    static void land() {
        Human.instance.removeState(StateEnum.ISLEVIT);
        IOUtil.msg(Misc.choose_str("bummer!  You've hit the ground",
                "you float gently to the ground"));
    }

    /*
     * come_down:
     *	Take the hero down off her acid trip.
     */
    static void come_down(Player player) {
        boolean seemonst;

        if (!player.containsState(StateEnum.ISHALU)) {
            return;
        }
        try {
            Method m = Daemons.class.getMethod("visuals");
            Daemon.kill_daemon(m);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        player.removeState(StateEnum.ISHALU);

        if (player.containsState(StateEnum.ISBLIND)) {
            return;
        }

        /*
         * undo the things
         */
        for (ThingImp tp : Global.lvl_obj) {
            if (Chase.isSee(tp._o_pos)) {
                Display.mvaddch(tp._o_pos, tp.getDisplay().getValue());
            }
        }

        /*
         * undo the monsters
         */
        seemonst = player.containsState(StateEnum.SEEMONST);
        for (OriginalMonster tp : Global.mlist) {
            Display.move(tp.getPosition());
            if (Chase.isSee(tp.getPosition())) {
                if (!tp.containsState(StateEnum.ISINVIS) || player.containsState(StateEnum.CANSEE))
                    Display.addch((char) tp.getDisplayTile());
                else {
                    Display.addch(Util.getPlace(tp.getPosition()).p_ch.getValue());
                }
            } else if (seemonst) {
                Display.standout();
                Display.addch((char) tp.getType());
                Display.standend();
            }
        }
        IOUtil.msg("Everything looks SO boring now.");
    }

    /*
     * nohaste:
     *	End the hasting
     */
    static void nohaste() {
        Human.instance.removeState(StateEnum.ISHASTE);
        IOUtil.msg("you feel yourself slowing down");
    }

    /*
     * doctor:
     *	A healing daemon that restors hit points after rest
     */
    void doctor(Player player) {
        int lv, ohp;

        lv = Global.player.getStatus().s_lvl;
        ohp = player.getHp();
        Global.quiet++;
        if (lv < 8) {
            if (Global.quiet + (lv << 1) > 20) {
                player.addHp(1);
            }
        } else if (Global.quiet >= 3) {
            player.addHp(Util.rnd(lv - 7) + 1);
        }
        if (Global.cur_ring[Const.LEFT] instanceof RegenerationRing) {
            player.addHp(1);
        }
        if (Global.cur_ring[Const.RIGHT] instanceof RegenerationRing) {
            player.addHp(1);
        }
        if (ohp != player.getHp()) {
            if (player.getHp() > player.getMaxHp()) {
                Global.player.getStatus().s_hpt = player.getMaxHp();
            }
            Global.quiet = 0;
        }
    }

}
