package org.hiro;

import org.hiro.character.StateEnum;
import org.hiro.map.RoomInfoEnum;
import org.hiro.output.Display;
import org.hiro.things.ObjectType;
import org.hiro.things.ScrollEnum;
import org.hiro.things.ThingImp;

import java.util.List;

public class Pack {

    /*
     * get_item:
     *	Pick something out of a pack for a purpose
     */
    static ThingImp get_item(String purpose, ObjectType type) {

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
                    if (!inventory(Global.player.getBaggage(), type.getValue())) {
                        Global.after = false;
                        return null;
                    }
                    continue;
                }
                ThingImp obj = null;
                for (ThingImp obj2 : Global.player.getBaggage()) {
                    obj = obj2;
                    if (obj2._o_packch == ch) {
                        break;
                    }
                }
                if (obj == null) {
                    IOUtil.msg("'%s' is not a valid item", String.valueOf(Display.unctrl(ch)));
                    continue;
                } else {
                    IOUtil.msg("");
                    return obj;
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
    static boolean inventory(List<ThingImp> list, int type) {
        boolean MASTER = false;
        String inv_temp;

        Global.n_objs = 0;
        for (ThingImp th : list ) {
            if (type != 0 && type != th._o_type.getValue() &&
                    !(type == Const.CALLABLE && th._o_type != ObjectType.FOOD && th._o_type != ObjectType.AMULET) &&
                    !(type == Const.R_OR_S && (th._o_type == ObjectType.RING || th._o_type == ObjectType.STICK))) {
                continue;
            }
            Global.n_objs++;
            inv_temp = th._o_packch + ") %s";
            if (MASTER) {
                if (th._o_packch == 0) {
                    inv_temp = "%s";
                } else {

                }
            }
            Global.msg_esc = true;
            if (ThingMethod.add_line(inv_temp, ThingMethod.inv_name(th, false)) == Const.ESCAPE) {
                Global.msg_esc = false;
                IOUtil.msg("");
                return true;
            }
            Global.msg_esc = false;
        }
        if (Global.n_objs == 0) {
            if (Global.terse) {
                IOUtil.msg(type == 0 ? "empty handed" :
                        "nothing appropriate");
            } else {
                IOUtil.msg(type == 0 ? "you are empty handed" :
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
        if (obj._o_count > 1 && !all) {
            Global.last_pick = obj;
            obj._o_count--;
            if (obj._o_group != 0) {
                Global.inpack++;
            }
            if (newobj) {
                nobj = ListMethod.new_item();
                nobj = obj;
                nobj._l_next = null;
                nobj._l_prev = null;
                nobj._o_count = 1;
            }
        } else {
            Global.last_pick = null;
            Global.pack_used[obj._o_packch - 'a'] = false;
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
        ObjectType ch = Global.places.get((Global.player._t_pos.x << 5) + Global.player._t_pos.y).p_ch;
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
        if (Global.player.t_room.containInfo(RoomInfoEnum.ISGONE)) {
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
    static void add_pack(ThingImp obj, boolean silent) {
        Boolean discarded = false;

        boolean from_floor = false;
        if (obj == null) {
            if ((obj = Misc.find_obj(Global.player._t_pos.y, Global.player._t_pos.x)) == null)
                return;
            from_floor = true;
        }

        /*
         * Check for and deal with scare monster scrolls
         */
        if (obj._o_type == ObjectType.SCROLL && obj._o_which == ScrollEnum.S_SCARE.getValue())
            if (obj.contains_o_flags(StateEnum.ISFOUND.getValue())) { // TODO:o_flagとt_flag共有を考えないと
                Global.lvl_obj.remove(obj);
                Display.mvaddch(Global.player._t_pos.y, Global.player._t_pos.x, floor_ch().getValue());
                Global.places.get((Global.player._t_pos.x << 5) + Global.player._t_pos.y).p_ch = Global.player.t_room.containInfo(RoomInfoEnum.ISGONE) ? ObjectType.PASSAGE : ObjectType.FLOOR;
                update_mdest(obj);
                discarded = true;
                ListMethod.discard(obj);
                IOUtil.msg("the scroll turns to dust as you pick it up");
                return;
            }

        if (Global.player.getBaggageSize() == 0) {
            Global.player.addItem(obj);
            obj._o_packch = pack_char();
            Global.inpack++;
        } else {
            ThingImp lp = null;
            for (ThingImp op : Global.player.getBaggage()) {
                if (op._o_type != obj._o_type)
                    lp = op;
                else {
                    while (op._o_type == obj._o_type && op._o_which != obj._o_which) {
                        lp = op;
                        if (op._l_next == null) {
                            break;
                        } else {
                            op = op._l_next;
                        }
                    }
                    if (op._o_type == obj._o_type && op._o_which == obj._o_which) {
                        if (op._o_type == ObjectType.POTION || op._o_type == ObjectType.SCROLL
                                || op._o_type == ObjectType.FOOD) {
                            if (!pack_room(from_floor, obj)) {
                                return;
                            }
                            op._o_count++;
                            update_mdest(obj);
                            ListMethod.discard(obj);
                            obj = op;
                            discarded = true;
                            lp = null;
                            // goto out;
                        } else if (obj._o_group != 0) {
                            lp = op;
                            while (op._o_type == obj._o_type
                                    && op._o_which == obj._o_which
                                    && op._o_group != obj._o_group) {
                                lp = op;
                                if (op._l_next == null) {
                                    break;
                                } else {
                                    op = op._l_next;
                                }
                            }
                            if (op._o_type == obj._o_type
                                    && op._o_which == obj._o_which
                                    && op._o_group == obj._o_group) {
                                op._o_count += obj._o_count;
                                Global.inpack--;
                                if (!pack_room(from_floor, obj)) {
                                    return;
                                }
                                // goto dump_it;
                                update_mdest(obj);
                                ListMethod.discard(obj);
                                obj = op;
                                discarded = true;
                                lp = null;
                            }
                        } else {
                            lp = op;
                        }
                    }
                    out:
                    break;
                }
            }

            if (lp != null) {
                if (!pack_room(from_floor, obj)){
                    return;}
                else {
                    obj._o_packch = pack_char();
                    obj._l_next = lp._l_next;
                    obj._l_prev = lp;
                    if (lp._l_next != null) {
                        lp._l_next._l_prev = obj;
                    }
                    lp._l_next = obj;
                }
            }
        }

        obj.add_o_flags(StateEnum.ISFOUND.getValue()); // TODO:o_flagとt_flag共有を考えないと

        /*
         * If this was the object of something's desire, that monster will
         * get mad and run at the hero.
         */
        if (!discarded) {
            update_mdest(obj);
        }

        if (obj._o_type == ObjectType.AMULET) {
            Game.getInstance().setGoal(true);
        }
        /*
         * Notify the user
         */
        if (!silent) {
            if (!Global.terse) {
                IOUtil.addmsg("you now have ");
            }
            IOUtil.msg("%s (%c)", ThingMethod.inv_name(obj, !Global.terse), obj._o_packch);
        }
    }

    /*
     * update_mdest:
     *	Called after picking up an object, before discarding it.
     *	If this was the object of something's desire, that monster will
     *	get mad and run at the hero
     */
    static void update_mdest(ThingImp obj) {
        for (ThingImp mp : Global.mlist) {
            if (mp._t_dest == obj._o_pos) {
                mp._t_dest = Global.player._t_pos;
            }
        }
    }


    /*
     * pack_room:
     *	See if there's room in the pack.  If not, print out an
     *	appropriate message
     */
    static boolean pack_room(boolean from_floor, ThingImp obj) {
        if (++Global.inpack > Const.MAXPACK) {
            if (!Global.terse) {
                IOUtil.addmsg("there's ");
            }
            IOUtil.addmsg("no room");
            if (!Global.terse) {
                IOUtil.addmsg(" in your pack");
            }
            IOUtil.endmsg();
            if (from_floor) {
                move_msg(obj);
            }
            Global.inpack = Const.MAXPACK;
            return false;
        }

        if (from_floor) {
            Global.lvl_obj.remove( obj);
            Display.mvaddch(Global.player._t_pos.y, Global.player._t_pos.x, floor_ch().getValue());
            Global.places.get((Global.player._t_pos.x << 5) + Global.player._t_pos.y).p_ch = Global.player.t_room.containInfo(RoomInfoEnum.ISGONE) ? ObjectType.PASSAGE : ObjectType.FLOOR;
        }

        return true;
    }

    /*
     * move_msg:
     *	Print out the message if you are just moving onto an object
     */
    static void move_msg(ThingImp obj) {
        if (!Global.terse) {
            IOUtil.addmsg("you ");
        }
        IOUtil.msg("moved onto %s", ThingMethod.inv_name(obj, true));
    }

    /*
     * pack_char:
     *	Return the next unused pack character.
     */
    static int pack_char() {
        int bp;

        for (bp = 0; Global.pack_used[bp]; bp++) {
            continue;
        }
        Global.pack_used[bp] = true;
        return (bp + 'a');
    }

    /*
     * pick_up:
     *	Add something to characters pack.
     */
    static void pick_up(int ch) {
        Boolean MASTER = false;

        if (Global.player.containsState(StateEnum.ISLEVIT)) {
            return;
        }

        ThingImp obj = Misc.find_obj(Global.player._t_pos.y, Global.player._t_pos.x);
        if (Global.move_on) {
            move_msg(obj);
        } else {
            ObjectType ot = ObjectType.get((char) ch);
            switch (ot) {
                case GOLD:
                    if (obj == null) {
                        return;
                    }
                    money(obj._o_arm);
                    Global.lvl_obj.remove(obj);
                    update_mdest(obj);
                    ListMethod.discard(obj);
                    Global.player.t_room.r_goldval = 0;
                    break;
                default:
                    if (MASTER) {
                        // debug("Where did you pick a '%s' up???", Display.unctrl(ch));
                    }
                case ARMOR:
                case POTION:
                case FOOD:
                case WEAPON:
                case SCROLL:
                case AMULET:
                case RING:
                case STICK:
                    add_pack(null, false);
                    break;
            }
        }
    }

    /*
     * money:
     *	Add or subtract gold from the pack
     */
    static void money(int value) {
        Global.purse += value;
        Display.mvaddch(Global.player._t_pos.y, Global.player._t_pos.x, floor_ch().getValue());
        Global.places.get((Global.player._t_pos.x << 5) + Global.player._t_pos.y).p_ch =
                Global.player.t_room.containInfo(RoomInfoEnum.ISGONE) ? ObjectType.PASSAGE : ObjectType.FLOOR;
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
    static void picky_inven()
    {

        if (Global.player.getBaggageSize() == 0){
            IOUtil.msg("you aren't carrying anything");}
        else if (Global.player.getBaggageSize() == 1){
            IOUtil.msg("a) %s", ThingMethod.inv_name(Global.player.getBaggage().get(0), false));}
        else
        {
            IOUtil.msg(Global.terse ? "item: " : "which item do you wish to inventory: ");
            int mch = IOUtil.readchar();
            Global.mpos = 0;
            if (mch == Const.ESCAPE)
            {
                IOUtil.msg("");
                return;
            }
            for (ThingImp obj : Global.player.getBaggage()) {
                if (mch == obj._o_packch) {
                    IOUtil.msg("%c) %s", mch, ThingMethod.inv_name(obj, false));
                    return;
                }
            }
            IOUtil.msg("'%s' not in pack", Display.unctrl(mch));
        }
    }

}
