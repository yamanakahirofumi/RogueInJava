package org.hiro;

import org.hiro.map.Coordinate;
import org.hiro.output.Display;
import org.hiro.things.ObjectType;
import org.hiro.things.ThingImp;
import org.hiro.things.WeaponEnum;

public class WeaponMethod {
    /*
     * init_weapon:
     *	Set up the initial goodies for a Weapon
     */

    public static void init_weapon(ThingImp weap, int which) {
        InitWeapon iwp;
        //init_weaps iwp;

        weap._o_type = ObjectType.WEAPON;
        weap._o_which = which;
        iwp = Global.init_dam.get(which);
        weap._o_damage = iwp.iw_dam;
        weap._o_hurldmg = iwp.iw_hrl;
        weap._o_launch = iwp.iw_launch;
        weap.set_o_flags(iwp.iw_flags);
        weap._o_hplus = 0;
        weap._o_dplus = 0;
        if (which == WeaponEnum.DAGGER.getValue()) {
            weap._o_count = Util.rnd(4) + 2;
            weap._o_group = Global.group++;
        } else if (weap.contains_o_flags(Const.ISMANY)) {
            //		} else if ((weap._o_flags & Const.ISMANY) != 0) {
            weap._o_count = Util.rnd(8) + 8;
            weap._o_group = Global.group++;
        } else {
            weap._o_count = 1;
            weap._o_group = 0;
        }
    }

    /*
     * num:
     *	Figure out the plus number for armor/weapons
     */
    static String num(int n1, int n2, ObjectType type) {
        String numbuf;

        numbuf = (n1 < 0 ? String.valueOf(n1) : "+" + n1);
        if (type.getValue() == ObjectType.WEAPON.getValue()) {
            numbuf = numbuf + (n2 < 0 ? "," + n2 : ",+" + n2);
        }
        return numbuf;
    }

    /*
     * missile:
     *	Fire a missile in a given direction
     */
    static void missile(int ydelta, int xdelta) {
        ThingImp obj;

        /*
         * Get which thing we are hurling
         */
        if ((obj = Pack.get_item("throw", ObjectType.WEAPON)) == null) {
            return;
        }
        if (!ThingMethod.dropcheck(obj) || Misc.is_current(obj)) {
            return;
        }
        obj = Pack.leave_pack(obj, true, false);
        do_motion(obj, ydelta, xdelta);
        /*
         * AHA! Here it has hit something.  If it is a wall or a door,
         * or if it misses (combat) the monster, put it on the floor
         */
        if (Util.getPlace(obj._o_pos).p_monst == null ||
                !hit_monster(obj._o_pos, obj)) {
            fall(obj, true);
        }
    }

    /*
     * fall:
     *	Drop an item someplace around here.
     */
    static void fall(ThingImp obj, boolean pr) {
        Coordinate fpos = new Coordinate(); // 多分fallpos()で代入

        if (fallpos(obj._o_pos, fpos)) {
            Place pp = Util.getPlace(fpos);
            pp.p_ch = obj._o_type;
            obj._o_pos = fpos;
            if (Chase.isSee(fpos)) {
                if (pp.p_monst != null) {
                    pp.p_monst._t_oldch = obj._o_type.getValue();
                } else
                    Display.mvaddch(fpos.y, fpos.x, obj._o_type.getValue());
            }
            Global.lvl_obj.add(obj);
            return;
        }
        if (pr) {
            if (Global.has_hit) {
                IOUtil.endmsg();
                Global.has_hit = false;
            }
            IOUtil.msg("the %s vanishes as it hits the ground",
                    Global.weap_info[obj._o_which].getName());
        }
    }

    /*
     * fallpos:
     *	Pick a random position around the give (y, x) coordinates
     */
    static boolean fallpos(Coordinate pos, Coordinate newpos) {
        int cnt = 0;
        for (int y = pos.y - 1; y <= pos.y + 1; y++)
            for (int x = pos.x - 1; x <= pos.x + 1; x++) {
                /*
                 * check to make certain the spot is empty, if it is,
                 * put the object there, set it in the level list
                 * and re-draw the room if he can see it
                 */
                if (Global.player._t_pos.equals(new Coordinate(x, y))) {
                    continue;
                }
                ObjectType ch;
                if (((ch = Util.INDEX(y, x).p_ch) == ObjectType.FLOOR || ch == ObjectType.PASSAGE)
                        && Util.rnd(++cnt) == 0) {
                    newpos.y = y;
                    newpos.x = x;
                }
            }
        return (cnt != 0);
    }

    /*
     * hit_monster:
     *	Does the missile hit the monster?
     */
    static boolean hit_monster(Coordinate mp, ThingImp obj) {
        return Fight.fight(mp, obj, true);
    }

    /*
     * do_motion:
     *	Do the actual motion on the screen done by an object traveling
     *	across the room
     */
    static void do_motion(ThingImp obj, int ydelta, int xdelta) {
        ObjectType ch;

        /*
         * Come fly with us ...
         */
        obj._o_pos = Global.player._t_pos;
        for (; ; ) {
            /*
             * Erase the old one
             */
            if (!obj._o_pos.equals(Global.player._t_pos) && Chase.isSee(obj._o_pos) && !Global.terse) {
                ch = Util.getPlace(obj._o_pos).p_ch;
                if (ch == ObjectType.FLOOR && !Misc.show_floor())
                    ch = ObjectType.Blank;
                Display.mvaddch(obj._o_pos.y, obj._o_pos.x, ch.getValue());
                if (!Global.jump) {
                    Command.msleep(10L);
                }
            }
            /*
             * Get the new position
             */
            obj._o_pos.y += ydelta;
            obj._o_pos.x += xdelta;
            if (IOUtil.step_ok(ch = Util.winat(obj._o_pos)) && ch != ObjectType.DOOR) {
                /*
                 * It hasn't hit anything yet, so display it
                 * If it alright.
                 */
                if (Chase.isSee(obj._o_pos) && !Global.terse) {
                    Display.mvaddch(obj._o_pos.y, obj._o_pos.x, obj._o_type.getValue());
                    Display.refresh();
                }
                if (!Global.jump) {
                    Command.msleep(10L);
                }
                continue;
            }
            break;
        }
    }

    /*
     * wield:
     *	Pull out a certain weapon
     */
    static void wield() {

        ThingImp oweapon = Global.cur_weapon;
        if (!ThingMethod.dropcheck(Global.cur_weapon)) {
            Global.cur_weapon = oweapon;
            return;
        }
        Global.cur_weapon = oweapon;
        ThingImp obj;
        if ((obj = Pack.get_item("wield", ObjectType.WEAPON)) == null) {
            Global.after = false;
            return;
        }

        if (obj._o_type == ObjectType.ARMOR) {
            IOUtil.msg("you can't wield armor");
            Global.after = false;
            return;
        }
        if (Misc.is_current(obj)) {
            Global.after = false;
            return;
        }

        String sp = ThingMethod.inv_name(obj, true);
        Global.cur_weapon = obj;
        if (!Global.terse) {
            IOUtil.addmsg("you are now ");
        }
        IOUtil.msg("wielding %s (%c)", sp, obj._o_packch);
    }


}
