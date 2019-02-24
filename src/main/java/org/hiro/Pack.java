package org.hiro;

import org.hiro.character.Human;
import org.hiro.character.StateEnum;
import org.hiro.map.RoomInfoEnum;
import org.hiro.output.Display;
import org.hiro.things.Amulet;
import org.hiro.things.Gold;
import org.hiro.things.ObjectType;
import org.hiro.things.OriginalMonster;
import org.hiro.things.Thing;
import org.hiro.things.ThingImp;
import org.hiro.things.scrolltype.Scare;

import java.util.List;

public class Pack {

    /*
     * get_item:
     *	Pick something out of a pack for a purpose
     */
    public static ThingImp get_item(String purpose, ObjectType type) {

        if (Global.player.getBaggageSize() == 0) {
            IOUtil.msg("you aren't carrying anything");
        } else if (Global.again) {
            if (Global.last_pick != null) { // 条件よくわからん
                return Global.last_pick;
            } else {
                IOUtil.msg("you ran out");
            }
        } else {
            for (; ; ) {
                if (!Global.terse) {
                    IOUtil.addmsg("which object do you want to ");
                }
                IOUtil.addmsg(purpose);
                if (Global.terse) {
                    IOUtil.addmsg(" what");
                }
                IOUtil.msg("? (* for list): ");
                int ch = IOUtil.readchar();
                Global.mpos = 0;
                /*
                 * Give the poor player a chance to abort the command
                 */
                if (ch == Const.ESCAPE) {
                    reset_last();
                    Global.after = false;
                    IOUtil.msg("");
                    return null;
                }
                Global.n_objs = 1;        /* normal case: person types one char */
                if (ch == '*') {
                    Global.mpos = 0;
                    if (!inventory(Global.player.getBaggage(), type)) {
                        Global.after = false;
                        return null;
                    }
                    continue;
                }
                Thing obj = null;
                for (Thing obj2 : Global.player.getBaggage()) {
                    obj = obj2;
                    if (Human.instance.getPositionOfContent(obj2) == ch) {
                        break;
                    }
                }
                if (obj == null) {
                    IOUtil.msg("'%s' is not a valid item", String.valueOf(Display.unctrl(ch)));
                } else {
                    IOUtil.msg("");
                    return (ThingImp) obj;
                }
            }
        }
        return null;
    }

    /*
     * reset_last:
     *	Reset the last command when the current one is aborted
     */
    static void reset_last() {
        Global.last_comm = Global.l_last_comm;
        Global.last_dir = Global.l_last_dir;
        Global.last_pick = Global.l_last_pick;
    }

    /*
     * inventory:
     *	List what is in the pack.  Return true if there is something of
     *	the given type.
     */
    public static boolean inventory(List<ThingImp> list, ObjectType type) {
        boolean MASTER = false;
        String inv_temp;

        Global.n_objs = 0;
        for (Thing th : list) {
            if (type != ObjectType.Initial && type != th.getDisplay()) {
                continue;
            }
            Global.n_objs++;
            inv_temp = Human.instance.getPositionOfContent(th) + ") %s";
            if (MASTER && !Human.instance.isContent(th)) {
                inv_temp = "%s";
            }
            Global.msg_esc = true;
            if (ThingMethod.add_line(inv_temp, ThingMethod.inventoryName(th, false)) == Const.ESCAPE) {
                Global.msg_esc = false;
                IOUtil.msg("");
                return true;
            }
            Global.msg_esc = false;
        }
        if (Global.n_objs == 0) {
            if (Global.terse) {
                IOUtil.msg(type == ObjectType.Initial ? "empty handed" :
                        "nothing appropriate");
            } else {
                IOUtil.msg(type == ObjectType.Initial ? "you are empty handed" :
                        "you don't have anything appropriate");
            }
            return false;
        }
        ThingMethod.end_line();
        return true;
    }

    /*
     * leave_pack:
     *	take an item out of the pack
     */
    static ThingImp leave_pack(ThingImp obj, boolean newobj, boolean all) {

        Global.inpack--;
        ThingImp nobj = obj;
        if (obj.getCount() > 1 && !all) {
            Global.last_pick = obj;
            obj._o_count--;
            if (obj.isGroup()) {
                Global.inpack++;
            }
            if (newobj) {
                nobj = new ThingImp();
                nobj = obj;
                nobj.addCount(1);
            }
        } else {
            Global.last_pick = null;
            Global.pack_used[Human.instance.getPositionOfContent(obj) - 'a'] = false;
            Global.player.removeItem(obj);
        }
        return nobj;
    }

    /*
     * floor_at:
     *	Return the character at hero's position, taking see_floor
     *	into account
     */
    static ObjectType floor_at() {
        ObjectType ch = Util.getPlace(Global.player._t_pos).p_ch;
        if (ch == ObjectType.FLOOR) {
            ch = floor_ch();
        }
        return ch;
    }

    /*
     * floor_ch:
     *	Return the appropriate floor character for her room
     */
    static ObjectType floor_ch() {
        if (Human.instance.getRoom().containInfo(RoomInfoEnum.ISGONE)) {
            return ObjectType.PASSAGE;
        }
        return (Misc.show_floor() ? ObjectType.FLOOR : ObjectType.Blank);
    }


    /*
     * add_pack:
     *	Pick up an object and add it to the pack.  If the argument is
     *	non-null use it as the linked_list pointer instead of gettting
     *	it off the ground.
     */
    public static void add_pack(ThingImp obj, boolean silent) {

        boolean from_floor = false;
        if (obj == null) {
            if ((obj = Misc.find_obj(Global.player._t_pos)) == null) {
                return;
            }
            from_floor = true;
        }

        /*
         * Check for and deal with scare monster scrolls
         */
        if (obj instanceof Scare && obj.contains_o_flags(StateEnum.ISFOUND.getValue())) {
            // TODO:o_flagとt_flag共有を考えないと
            Global.lvl_obj.remove(obj);
            Display.mvaddch(Global.player._t_pos, floor_ch().getValue());
            Util.getPlace(Global.player._t_pos).p_ch = Human.instance.getRoom().containInfo(RoomInfoEnum.ISGONE) ? ObjectType.PASSAGE : ObjectType.FLOOR;
            update_mdest(obj);
            IOUtil.msg("the scroll turns to dust as you pick it up");
            return;
        }

        if(!pack_room(from_floor ,obj)){
          return;
        }

        obj.add_o_flags(StateEnum.ISFOUND.getValue()); // TODO:o_flagとt_flag共有を考えないと

        /*
         * If this was the object of something's desire, that monster will
         * get mad and run at the hero.
         */
        update_mdest(obj);

        if (obj instanceof Amulet) {
            Game.getInstance().setGoal(true);
        }
        /*
         * Notify the user
         */
        if (!silent) {
            if (!Global.terse) {
                IOUtil.addmsg("you now have ");
            }
            IOUtil.msg("%s (%c)", ThingMethod.inventoryName(obj, !Global.terse),
                    Human.instance.getPositionOfContent(obj));
        }
    }

    /*
     * update_mdest:
     *	Called after picking up an object, before discarding it.
     *	If this was the object of something's desire, that monster will
     *	get mad and run at the hero
     */
    static void update_mdest(ThingImp obj) {
        for (OriginalMonster mp : Global.mlist) {
            if (mp.getRunPosition().equals(obj._o_pos)) {
                mp.setRunPosition(Global.player._t_pos);
            }
        }
    }


    /*
     * pack_room:
     *	See if there's room in the pack.  If not, print out an
     *	appropriate message
     */
    static boolean pack_room(boolean from_floor, Thing obj) {
        boolean b = Human.instance.addContent(obj);
        if (!b && from_floor) {
            move_msg(obj);
        }

        if (from_floor) {
            Global.lvl_obj.remove(obj);
            Display.mvaddch(Global.player._t_pos, floor_ch().getValue());
            Util.getPlace(Global.player._t_pos).p_ch = Human.instance.getRoom().containInfo(RoomInfoEnum.ISGONE) ? ObjectType.PASSAGE : ObjectType.FLOOR;
        }

        return true;
    }

    /*
     * move_msg:
     *	Print out the message if you are just moving onto an object
     */
    static void move_msg(Thing obj) {
        if (!Global.terse) {
            IOUtil.addmsg("you ");
        }
        IOUtil.msg("moved onto %s", ThingMethod.inventoryName(obj, true));
    }

    /*
     * pick_up:
     *	Add something to characters pack.
     */
    public static void pick_up() {
        if (Human.instance.containsState(StateEnum.ISLEVIT)) {
            return;
        }

        Thing obj = Misc.find_obj(Global.player._t_pos);
        if (Global.move_on) {
            move_msg(obj);
        } else {
            if (obj instanceof Gold) {
                Gold gold = (Gold) obj;
                money(gold.getGold());
                Global.lvl_obj.remove(gold);
                update_mdest(gold);
                Global.lvl_obj.remove(gold);
                Human.instance.getRoom().r_goldval = 0;
            } else {
                add_pack(null, false);
            }
        }
    }

    /*
     * money:
     *	Add or subtract gold from the pack
     */
    static void money(int value) {
        Global.purse += value;
        Display.mvaddch(Global.player._t_pos, floor_ch().getValue());
        Util.getPlace(Global.player._t_pos).p_ch =
                Human.instance.getRoom().containInfo(RoomInfoEnum.ISGONE) ? ObjectType.PASSAGE : ObjectType.FLOOR;
        if (value > 0) {
            if (!Global.terse) {
                IOUtil.addmsg("you found ");
            }
            IOUtil.msg("%d gold pieces", value);
        }
    }


    /*
     * picky_inven:
     *	Allow player to inventory a single item
     */
    public static void picky_inven() {

        if (Global.player.getBaggageSize() == 0) {
            IOUtil.msg("you aren't carrying anything");
        } else if (Global.player.getBaggageSize() == 1) {
            IOUtil.msg("a) %s", ThingMethod.inventoryName(Global.player.getBaggage().get(0), false));
        } else {
            IOUtil.msg(Global.terse ? "item: " : "which item do you wish to inventory: ");
            int mch = IOUtil.readchar();
            Global.mpos = 0;
            if (mch == Const.ESCAPE) {
                IOUtil.msg("");
                return;
            }
            for (Thing obj : Global.player.getBaggage()) {
                if (mch == Human.instance.getPositionOfContent(obj)) {
                    IOUtil.msg("%c) %s", mch, ThingMethod.inventoryName(obj, false));
                    return;
                }
            }
            IOUtil.msg("'%s' not in pack", Display.unctrl(mch));
        }
    }

}
