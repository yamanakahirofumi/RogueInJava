package org.hiro;

import org.hiro.character.Player;
import org.hiro.character.StateEnum;
import org.hiro.map.Coordinate;
import org.hiro.output.Display;
import org.hiro.things.Armor;
import org.hiro.things.ObjectType;
import org.hiro.things.Potion;
import org.hiro.things.Ring;
import org.hiro.things.Scroll;
import org.hiro.things.Stick;
import org.hiro.things.Thing;
import org.hiro.things.ThingImp;
import org.hiro.things.Weapon;

import java.util.ArrayList;
import java.util.List;

public class Wizard {

    /*
     * telport:
     *	Bamf the hero someplace else
     */
    public static void teleport(Player player) {
        Coordinate c = new Coordinate();

        Display.mvaddch(Global.player._t_pos, Pack.floor_at().getValue());
        DrawRoom.find_floor(null, c, false, true);
        if (!Chase.roomin(c).equals(Global.player.t_room)) {
            Rooms.leave_room(Global.player._t_pos);
            Global.player._t_pos = c;
            Rooms.enter_room(Global.player._t_pos);
        } else {
            Global.player._t_pos = c;
            Misc.look(true);
        }
        Display.mvaddch(Global.player._t_pos, ObjectType.PLAYER.getValue());
        /*
         * turn off ISHELD in case teleportation was done while fighting
         * a Flytrap
         */
        if (player.containsState(StateEnum.ISHELD)) {

            player.removeState(StateEnum.ISHELD);
            Global.vf_hit = 0;
            for (ThingImp mp : Global.mlist) {
                if (mp.getType() == 'F')
                    mp._t_stats.s_dmg = "0x0";
            }
        }
        Global.no_move = 0;
        Global.count = 0;
        Global.running = false;
        Mach_dep.flush_type();
    }

    /*
     * whatis:
     *	What a certin object is
     */
    public static void whatis(boolean insist, int type) {

        if (Global.player.getBaggageSize() == 0) {
            IOUtil.msg("you don't have anything in your pack to identify");
            return;
        }

        Thing obj;
        for (; ; ) {
            obj = Pack.get_item("identify", ObjectType.get((char) type));
            if (insist) {
                if (Global.n_objs == 0) {
                    return;
                } else if (obj == null) {
                    IOUtil.msg("you must identify something");
                } else if (type != 0 && obj.getDisplay().getValue() != type &&
                        !(type == Const.R_OR_S && (obj instanceof Ring || obj instanceof Stick))) {
                    IOUtil.msg("you must identify a %s", type_name(type));
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        if (obj == null) {
            return;
        }

        if (obj instanceof Scroll) {
            set_know(obj, Global.scr_info);
        } else if (obj instanceof Potion) {
            set_know(obj, Global.pot_info);
        } else if (obj instanceof Stick) {
            set_know(obj, Global.ws_info);
        } else if (obj instanceof Weapon || obj instanceof Armor) {
            obj.add_o_flags(Const.ISKNOW);
        } else if (obj instanceof Ring) {
            set_know(obj, Global.ring_info);
        }
        IOUtil.msg(ThingMethod.inventoryName(obj, false));
    }

    /*
     * set_know:
     *	Set things up when we really know what a thing is
     */
    static void set_know(Thing obj, Obj_info[] info) {
        info[((ThingImp)obj)._o_which].know();
        obj.add_o_flags(Const.ISKNOW);
    }

    /*
     * type_name:
     *	Return a pointer to the name of the type
     */
    static String type_name(int type) {
        List<Help_list> tlist = new ArrayList<>();
        {
            Help_list h = new Help_list(ObjectType.POTION.getValue(), "potion", false);
            tlist.add(h);
        }
        {
            Help_list h = new Help_list(ObjectType.SCROLL.getValue(), "scroll", false);
            tlist.add(h);
        }
        {
            Help_list h = new Help_list(ObjectType.FOOD.getValue(), "food", false);
            tlist.add(h);
        }
        {
            Help_list h = new Help_list((char) Const.R_OR_S, "ring, wand or staff", false);
            tlist.add(h);
        }
        {
            Help_list h = new Help_list(ObjectType.RING.getValue(), "ring", false);
            tlist.add(h);
        }
        {
            Help_list h = new Help_list(ObjectType.STICK.getValue(), "wand or staff", false);
            tlist.add(h);
        }
        {
            Help_list h = new Help_list(ObjectType.WEAPON.getValue(), "weapon", false);
            tlist.add(h);
        }
        {
            Help_list h = new Help_list(ObjectType.ARMOR.getValue(), "suit of armor", false);
            tlist.add(h);
        }

        for (Help_list hp : tlist) {
            if (type == hp.h_ch) {
                return hp.h_desc;
            }
        }
        /* NOTREACHED */
        return "";
    }


}
