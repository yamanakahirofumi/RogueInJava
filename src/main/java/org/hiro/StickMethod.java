package org.hiro;

import org.hiro.character.StateEnum;
import org.hiro.map.Coordinate;
import org.hiro.map.RoomInfoEnum;
import org.hiro.output.Display;
import org.hiro.things.*;

import java.util.ArrayList;
import java.util.List;

/**
 *  Functions to implement the various sticks one might find
 *  while wandering around the dungeon.
 */
public class StickMethod {
    /*
     * fix_stick:
     *	Set up a new stick
     */
    public static void fix_stick(Stick cur) {
        if (Global.ws_type[cur._o_which] == "staff") {
            cur._o_damage = "2x3";
        } else {
            cur._o_damage = "1x1";
        }
        cur._o_hurldmg = "1x1";

        if (cur._o_which == StickEnum.WS_LIGHT.getValue()) {
            cur._o_arm = Util.rnd(10) + 10;
        } else {
            cur._o_arm = Util.rnd(5) + 3;
        }
    }

    /*
     * charge_str:
     *	Return an appropriate string for a wand charge
     */
    static String charge_str(ThingImp obj) {
        String buf;

        if (!(obj.contains_o_flags(Const.ISKNOW))) {
            buf = "";
        } else if (Global.terse) {
            buf = " [" + obj._o_arm + "]";
        } else {
            buf = " [" + obj._o_arm + " charges]";
        }
        return buf;
    }


    /*
     * do_zap:
     *	Perform a zap with a wand
     */
    static void do_zap() {
        boolean MASTER = false;
        ThingImp obj;
        ThingImp bolt = new ThingImp();

        if ((obj = Pack.get_item("zap with", ObjectType.STICK)) == null)
            return;
        if (obj._o_type != ObjectType.STICK) {
            Global.after = false;
            IOUtil.msg("you can't zap with that!");
            return;
        }
        if (obj._o_arm == 0) {
            IOUtil.msg("nothing happens");
            return;
        }
        StickEnum st = StickEnum.get(obj._o_which);
        ThingImp tp;
        int y;
        int x;
        String name;
        switch (st) {
            case WS_LIGHT:
                /*
                 * Reddy Kilowat wand.  Light up the room
                 */
                Global.ws_info[st.getValue()].know();
                if (Global.player.t_room.containInfo(RoomInfoEnum.ISGONE)) {
                    IOUtil.msg("the corridor glows and then fades");
                } else {
                    Global.player.t_room.removeInfo(RoomInfoEnum.ISDARK);
                    /*
                     * Light the room and put the player back up
                     */
                    Rooms.enter_room(Global.player._t_pos);
                    IOUtil.addmsg("the room is lit");
                    if (!Global.terse) {
                        IOUtil.addmsg(" by a shimmering %s light", Init.pick_color("blue"));
                    }
                    IOUtil.endmsg();
                }
                break;
            case WS_DRAIN:
                /*
                 * take away 1/2 of hero's hit points, then take it away
                 * evenly from the monsters in the room (or next to hero
                 * if he is in a passage)
                 */
                if (Global.player._t_stats.s_hpt < 2) {
                    IOUtil.msg("you are too weak to use it");
                    return;
                } else {
                    drain();
                }
                break;
            case WS_INVIS:
            case WS_POLYMORPH:
            case WS_TELAWAY:
            case WS_TELTO:
            case WS_CANCEL:
                y = Global.player._t_pos.y;
                x = Global.player._t_pos.x;
                while (IOUtil.step_ok(Util.winat(y, x))) {
                    y += Global.delta.y;
                    x += Global.delta.x;
                }
                if ((tp = Global.places.get((x << 5) + y).p_monst) != null) {
                    int monster = tp._t_type;
                    if (monster == 'F') {
                        Global.player.removeState(StateEnum.ISHELD);
                    }
                    switch (st) {
                        case WS_INVIS:
                            tp.addState(StateEnum.ISINVIS);
                            if (Chase.isSee(new Coordinate(x,y))) {
                                Display.mvaddch(y, x, (char) tp._t_oldch);
                            }
                            break;
                        case WS_POLYMORPH: {

                            List<ThingImp> pp = tp.getBaggage();
                            Global.mlist.remove(tp);
                            if (Chase.see_monst(tp)) {
                                Display.mvaddch(y, x, Global.places.get((x << 5) + y).p_ch.getValue());
                            }
                            int oldch = tp._t_oldch;
                            Global.delta.y = y;
                            Global.delta.x = x;
                            Monst.new_monster(tp, monster = Util.rnd(26) + 'A', Global.delta);
                            if (Chase.see_monst(tp)) {
                                Display.mvaddch(y, x, (char) monster);
                            }
                            tp._t_oldch = oldch;
                            tp.setBaggage(pp);
                            Global.ws_info[st.getValue()].addKnown(Chase.see_monst(tp));
                            break;
                        }
                        case WS_CANCEL:
                            tp.addState(StateEnum.ISCANC);
                            tp.removeState(StateEnum.ISINVIS);
                            tp.removeState(StateEnum.CANHUH);
                            tp._t_disguise = tp._t_type;
                            if (Chase.see_monst(tp)) {
                                Display.mvaddch(y, x, (char) tp._t_disguise);
                            }
                            break;
                        case WS_TELAWAY:
                        case WS_TELTO: {
                            Coordinate new_pos = new Coordinate();

                            if (obj._o_which == StickEnum.WS_TELAWAY.getValue()) {
                                do {
                                    DrawRoom.find_floor(null, new_pos, false, true);
                                } while (new_pos.equals(Global.player._t_pos));
                            } else {
                                new_pos.y = Global.player._t_pos.y + Global.delta.y;
                                new_pos.x = Global.player._t_pos.x + Global.delta.x;
                            }
                            tp._t_dest = Global.player._t_pos;
                            tp.addState(StateEnum.ISRUN);
                            Chase.relocate(tp, new_pos);
                        }
                    }
                }
                break;
            case WS_MISSILE:
                Global.ws_info[StickEnum.WS_MISSILE.getValue()].know();
                bolt._o_type = ObjectType.GOLD;
                bolt._o_hurldmg = "1x4";
                bolt._o_hplus = 100;
                bolt._o_dplus = 1;
                bolt.set_o_flags(Const.ISMISL);
                if (Global.cur_weapon != null)
                    bolt._o_launch = Global.cur_weapon._o_which;
                WeaponMethod.do_motion(bolt, Global.delta.y, Global.delta.x);
                if ((tp = Global.places.get((bolt._o_pos.x << 5) + bolt._o_pos.y).p_monst) != null
                        && !Monst.save_throw(Const.VS_MAGIC, tp)) {
                    WeaponMethod.hit_monster(bolt._o_pos.y, bolt._o_pos.x, bolt);
                } else if (Global.terse) {
                    IOUtil.msg("missle vanishes");
                } else {
                    IOUtil.msg("the missle vanishes with a puff of smoke");
                }
                break;
            case WS_HASTE_M:
            case WS_SLOW_M:
                y = Global.player._t_pos.y;
                x = Global.player._t_pos.x;
                while (IOUtil.step_ok(Util.winat(y, x))) {
                    y += Global.delta.y;
                    x += Global.delta.x;
                }
                if ((tp = Global.places.get((x << 5) + y).p_monst) != null) {
                    if (obj._o_which == StickEnum.WS_HASTE_M.getValue()) {
                        if (tp.containsState(StateEnum.ISSLOW)) {
                            tp.removeState(StateEnum.ISSLOW);
                        } else {
                            tp.addState(StateEnum.ISHASTE);
                        }
                    } else {
                        if (tp.containsState(StateEnum.ISHASTE)) {
                            tp.removeState(StateEnum.ISHASTE);
                        } else {
                            tp.addState(StateEnum.ISSLOW);
                        }
                        tp._t_turn = true;
                    }
                    Global.delta.y = y;
                    Global.delta.x = x;
                    Chase.runto(Global.delta);
                }
                break;
            case WS_ELECT:
            case WS_FIRE:
            case WS_COLD:
                if (obj._o_which == StickEnum.WS_ELECT.getValue()) {
                    name = "bolt";
                } else if (obj._o_which == StickEnum.WS_FIRE.getValue()) {
                    name = "flame";
                } else {
                    name = "ice";
                }
                fire_bolt(Global.player._t_pos, Global.delta, name);
                Global.ws_info[obj._o_which].know();
                break;
            case WS_NOP:
                break;
            default:
                if (MASTER) {
                    IOUtil.msg("what a bizarre schtick!");
                }
        }
        obj._o_arm--;
    }

    /*
     * drain:
     *	Do drain hit points from player shtick
     */
    static void drain() {
        /*
         * First cnt how many things we need to spread the hit points among
         */
        Room corp;
        if (Global.places.get((Global.player._t_pos.x << 5) + Global.player._t_pos.y).p_ch == ObjectType.DOOR) {
            corp = Global.passages[Util.flat(Global.player._t_pos.y, Global.player._t_pos.x) & Const.F_PNUM];
        } else {
            corp = null;
        }
        boolean inpass = Global.player.t_room.containInfo(RoomInfoEnum.ISGONE);
        List<ThingImp> drainee = new ArrayList<>();
        for (ThingImp mp : Global.mlist) {
            if (mp.t_room == Global.player.t_room || mp.t_room == corp ||
                    (inpass && Global.places.get((mp._t_pos.x << 5) + mp._t_pos.y).p_ch == ObjectType.DOOR &&
                            Global.passages[Util.flat(mp._t_pos.y, mp._t_pos.x) & Const.F_PNUM] == Global.player.t_room)) {
                drainee.add(mp);
            }
        }
        if (drainee.size() == 0) {
            IOUtil.msg("you have a tingling feeling");
            return;
        }
        Global.player._t_stats.s_hpt /= 2;
        int cnt = Global.player._t_stats.s_hpt / drainee.size();
        /*
         * Now zot all of the monsters
         */
        for (ThingImp dp : drainee) {
            if ((dp._t_stats.s_hpt -= cnt) <= 0) {
                Fight.killed(dp, Chase.see_monst(dp));
            } else {
                Chase.runto(dp._t_pos);
            }
        }
    }

    /*
     * fire_bolt:
     *	Fire a bolt in a given direction from a specific starting place
     */
    static void fire_bolt(Coordinate start, Coordinate dir, String name) {
        List<Coordinate> spotpos = new ArrayList<>();
        ThingImp bolt = new ThingImp();

        bolt._o_type = ObjectType.WEAPON;
        bolt._o_which = WeaponEnum.FLAME.getValue();
        bolt._o_hurldmg = "6x6";
        bolt._o_hplus = 100;
        bolt._o_dplus = 0;
        Global.weap_info[WeaponEnum.FLAME.getValue()].setName(name);
        int dirch = 0;
        switch (dir.y + dir.x) {
            case 0:
                dirch = '/';
                break;
            case 1:
            case -1:
                dirch = (dir.y == 0 ? '-' : '|');
                break;
            case 2:
            case -2:
                dirch = '\\';
        }
        Coordinate pos = start;
        boolean hit_hero = (start != Global.player._t_pos);
        boolean used = false;
        boolean changed = false;
        int i;
        for (i = 0; i < Const.BOLT_LENGTH && !used; i++) {
            Coordinate c1;
            if(spotpos.size() <= i) {
                c1 = new Coordinate();
                spotpos.add(c1);
            }else{
                c1 = spotpos.get(i);
            }

            pos.y += dir.y;
            pos.x += dir.x;
            c1 = pos;
            ObjectType ch = Util.winat(pos.y, pos.x);
            ThingImp tp;
            switch (ch) {
                case DOOR:
                    /*
                     * this code is necessary if the hero is on a door
                     * and he fires at the wall the door is in, it would
                     * otherwise loop infinitely
                     */
                    if (Global.player._t_pos.equals( pos)) {
                        // defaultと同じ
                        if (!hit_hero && (tp = Global.places.get((pos.x << 5) + pos.y).p_monst) != null) {
                            hit_hero = true;
                            changed = !changed;
                            tp._t_oldch = Global.places.get((pos.x << 5) + pos.y).p_ch.getValue();
                            if (!Monst.save_throw(Const.VS_MAGIC, tp)) {
                                bolt._o_pos = pos;
                                used = true;
                                if (tp._t_type == 'D' && name.equals("flame")) {
                                    IOUtil.addmsg("the flame bounces");
                                    if (!Global.terse) {
                                        IOUtil.addmsg(" off the dragon");
                                    }
                                    IOUtil.endmsg();
                                } else
                                    WeaponMethod.hit_monster(pos.y, pos.x, bolt);
                            } else if (ch.getValue() != 'M' || tp._t_disguise == 'M') {
                                if (start == Global.player._t_pos)
                                    Chase.runto(pos);
                                if (Global.terse) {
                                    IOUtil.msg("%s misses", name);
                                } else {
                                    IOUtil.msg("the %s whizzes past %s", name, Fight.set_mname(tp));
                                }
                            }
                        } else if (hit_hero && pos.equals( Global.player._t_pos)) {
                            hit_hero = false;
                            changed = !changed;
                            if (!Monst.save(Const.VS_MAGIC)) {
                                if ((Global.player._t_stats.s_hpt -= Dice.roll(6, 6)) <= 0) {
                                    if (start == Global.player._t_pos) {
                                        Rip.death('b');
                                    } else {
                                        Rip.death(Global.places.get((start.x << 5) + start.y).p_monst._t_type);
                                    }
                                }
                                used = true;
                                if (Global.terse) {
                                    IOUtil.msg("the %s hits", name);
                                } else {
                                    IOUtil.msg("you are hit by the %s", name);
                                }
                            } else {
                                IOUtil.msg("the %s whizzes by you", name);
                            }
                        }
                        Display.mvaddch(pos.y, pos.x, (char) dirch);
                        Display.refresh();
                    }
                    /* FALLTHROUGH */
                case Vert:
                case Horizon:
                case Blank:
                    if (!changed) {
                        hit_hero = !hit_hero;
                    }
                    changed = false;
                    dir.y = -dir.y;
                    dir.x = -dir.x;
                    i--;
                    IOUtil.msg("the %s bounces", name);
                    break;
                default:
                    if (!hit_hero && (tp = Global.places.get((pos.x << 5) + pos.y).p_monst) != null) {
                        hit_hero = true;
                        changed = !changed;
                        tp._t_oldch = Global.places.get((pos.x << 5) + pos.y).p_ch.getValue();
                        if (!Monst.save_throw(Const.VS_MAGIC, tp)) {
                            bolt._o_pos = pos;
                            used = true;
                            if (tp._t_type == 'D' && name.equals("flame")) {
                                IOUtil.addmsg("the flame bounces");
                                if (!Global.terse) {
                                    IOUtil.addmsg(" off the dragon");
                                }
                                IOUtil.endmsg();
                            } else
                                WeaponMethod.hit_monster(pos.y, pos.x, bolt);
                        } else if (ch.getValue() != 'M' || tp._t_disguise == 'M') {
                            if (start == Global.player._t_pos)
                                Chase.runto(pos);
                            if (Global.terse) {
                                IOUtil.msg("%s misses", name);
                            } else {
                                IOUtil.msg("the %s whizzes past %s", name, Fight.set_mname(tp));
                            }
                        }
                    } else if (hit_hero && pos.equals(Global.player._t_pos)) {
                        hit_hero = false;
                        changed = !changed;
                        if (!Monst.save(Const.VS_MAGIC)) {
                            if ((Global.player._t_stats.s_hpt -= Dice.roll(6, 6)) <= 0) {
                                if (start == Global.player._t_pos) {
                                    Rip.death('b');
                                } else {
                                    Rip.death(Global.places.get((start.x << 5) + start.y).p_monst._t_type);
                                }
                            }
                            used = true;
                            if (Global.terse) {
                                IOUtil.msg("the %s hits", name);
                            } else {
                                IOUtil.msg("you are hit by the %s", name);
                            }
                        } else {
                            IOUtil.msg("the %s whizzes by you", name);
                        }
                    }
                    Display.mvaddch(pos.y, pos.x, (char) dirch);
                    Display.refresh();
            }
        }
        for(int j =0; j<i; j++){
            Coordinate c2 = spotpos.get(j);
            Display.mvaddch(c2.y, c2.x, Global.places.get((c2.x << 5) + c2.y).p_ch.getValue());
        }
    }


}
