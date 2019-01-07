package org.hiro;

import org.hiro.character.StateEnum;
import org.hiro.map.RoomInfoEnum;
import org.hiro.output.Display;
import org.hiro.things.ThingImp;

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
            if (Chase.cansee(tp._o_pos.y, tp._o_pos.x)) {
                Display.mvaddch(tp._o_pos.y, tp._o_pos.x, Misc.rnd_thing().getValue());
            }
        }

        /*
         * change the stairs
         */
        if (!Global.seenstairs && Chase.cansee(Global.stairs.y, Global.stairs.x)) {
            Display.mvaddch(Global.stairs.y, Global.stairs.x, Misc.rnd_thing().getValue());
        }

        /*
         * change the monsters
         */
        boolean seemonst = Global.player.containsState(StateEnum.SEEMONST);
        for (ThingImp tp : Global.mlist) {
            Display.move(tp._t_pos.y, tp._t_pos.x);
            if (Chase.see_monst(tp)) {
                if (tp._t_type == 'X' && tp._t_disguise != 'X') {
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
        for (ThingImp th : Global.mlist) {
            if (th.containsState(StateEnum.ISINVIS) && Chase.see_monst(th)) {
                Display.mvaddch(th._t_pos.y, th._t_pos.x, (char) th._t_oldch);
            }
        }
        Global.player.removeState(StateEnum.CANSEE);
    }

    /*
     * sight:
     *	He gets his sight back
     */
    static void sight() {
        if (Global.player.containsState(StateEnum.ISBLIND)) {
            try {
                Method m = Daemons.class.getMethod("sight");
                Daemon.extinguish(m);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            Global.player.removeState(StateEnum.ISBLIND);
            if (!Global.player.t_room.containInfo(RoomInfoEnum.ISGONE)) {
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
        Global.player.removeState(StateEnum.ISLEVIT);
        IOUtil.msg(Misc.choose_str("bummer!  You've hit the ground",
                "you float gently to the ground"));
    }

    /*
     * come_down:
     *	Take the hero down off her acid trip.
     */
    static void come_down() {
        boolean seemonst;

        if (!Global.player.containsState(StateEnum.ISHALU)) {
            return;
        }
        try {
            Method m = Daemons.class.getMethod("visuals");
            Daemon.kill_daemon(m);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Global.player.removeState(StateEnum.ISHALU);

        if (Global.player.containsState(StateEnum.ISBLIND)) {
            return;
        }

        /*
         * undo the things
         */
        for (ThingImp tp : Global.lvl_obj) {
            if (Chase.cansee(tp._o_pos.y, tp._o_pos.x)) {
                Display.mvaddch(tp._o_pos.y, tp._o_pos.x, tp._o_type.getValue());
            }
        }

        /*
         * undo the monsters
         */
        seemonst = Global.player.containsState(StateEnum.SEEMONST);
        for (ThingImp tp : Global.mlist) {
            Display.move(tp._t_pos.y, tp._t_pos.x);
            if (Chase.cansee(tp._t_pos.y, tp._t_pos.x)) {
                if (!tp.containsState(StateEnum.ISINVIS) || Global.player.containsState(StateEnum.CANSEE))
                    Display.addch((char) tp._t_disguise);
                else {
                    Display.addch(Global.places.get((tp._t_pos.x << 5) + tp._t_pos.y).p_ch.getValue());
                }
            } else if (seemonst) {
                Display.standout();
                Display.addch((char) tp._t_type);
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
        Global.player.removeState(StateEnum.ISHASTE);
        IOUtil.msg("you feel yourself slowing down");
    }


}
