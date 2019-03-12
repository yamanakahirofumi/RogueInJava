package org.hiro;

import org.hiro.character.Human;
import org.hiro.character.Player;
import org.hiro.map.AbstractCoordinate;
import org.hiro.map.Coordinate;
import org.hiro.output.Display;
import org.hiro.things.Armor;
import org.hiro.things.ObjectType;
import org.hiro.things.Thing;
import org.hiro.things.Weapon;

public class WeaponMethod {
    /*
     * num:
     *	Figure out the plus number for armor/weapons
     */
    static String num(int n1, int n2, ObjectType type) {

        String numbuf = (n1 < 0 ? String.valueOf(n1) : "+" + n1);
        if (type.getValue() == ObjectType.WEAPON.getValue()) {
            numbuf = numbuf + (n2 < 0 ? "," + n2 : ",+" + n2);
        }
        return numbuf;
    }

    /*
     * missile:
     *	Fire a missile in a given direction
     */
    public static void missile(AbstractCoordinate delta) {
        Thing obj = Pack.get_item("throw", ObjectType.WEAPON);

        /*
         * Get which thing we are hurling
         */
        if (obj == null) {
            return;
        }
        if (!ThingMethod.isDrop(obj) || Misc.is_current(obj)) {
            return;
        }
        obj = Pack.leave_pack(obj, true, false);
        do_motion(obj, delta);
        /*
         * AHA! Here it has hit something.  If it is a wall or a door,
         * or if it misses (combat) the monster, put it on the floor
         */
        if (Util.getPlace(obj.getOPos()).p_monst == null ||
                !hit_monster(obj.getOPos(), (Weapon) obj)) {
            fall(obj, true);
        }
    }

    /*
     * fall:
     *	Drop an item someplace around here.
     */
    static void fall(Thing obj, boolean pr) {
        AbstractCoordinate fpos = new Coordinate(); // 多分fallpos()で代入

        if (fallpos(obj.getOPos(), fpos)) {
            Place pp = Util.getPlace(fpos);
            pp.p_ch = obj.getDisplay();
            obj.setOPos(fpos);
            if (Chase.isSee(Human.instance, fpos)) {
                if (pp.p_monst != null) {
                    pp.p_monst.setFloorTile(obj.getDisplay().getValue());
                } else
                    Display.mvaddch(fpos, obj.getDisplay().getValue());
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
                    Global.weap_info[obj.getNumber()].getName());
        }
    }

    /*
     * fallpos:
     *	Pick a random position around the give (y, x) coordinates
     */
    static boolean fallpos(AbstractCoordinate pos, AbstractCoordinate newpos) {
        int cnt = 0;
        for (int y = pos.getY() - 1; y <= pos.getY() + 1; y++)
            for (int x = pos.getX() - 1; x <= pos.getX() + 1; x++) {
                /*
                 * check to make certain the spot is empty, if it is,
                 * put the object there, set it in the level list
                 * and re-draw the room if he can see it
                 */
                if (Human.instance.getPosition().equals(new Coordinate(x, y))) {
                    continue;
                }
                ObjectType ch;
                if (((ch = Util.INDEX(y, x).p_ch) == ObjectType.FLOOR || ch == ObjectType.PASSAGE)
                        && Util.rnd(++cnt) == 0) {
                    newpos.setY(y);
                    newpos.setX(x);
                }
            }
        return (cnt != 0);
    }

    /*
     * hit_monster:
     *	Does the missile hit the monster?
     */
    public static boolean hit_monster(AbstractCoordinate mp, Weapon obj) {
        return Fight.fight(mp, obj, true);
    }

    /*
     * do_motion:
     *	Do the actual motion on the screen done by an object traveling
     *	across the room
     */
    public static void do_motion(Thing obj, AbstractCoordinate delta) {
        ObjectType ch;

        /*
         * Come fly with us ...
         */
        obj.setOPos(Human.instance.getPosition());
        for (; ; ) {
            /*
             * Erase the old one
             */
            if (!obj.getOPos().equals(Human.instance.getPosition()) && Chase.isSee(Human.instance, obj.getOPos()) && !Global.terse) {
                ch = Util.getPlace(obj.getOPos()).p_ch;
                if (ch == ObjectType.FLOOR && !Misc.show_floor())
                    ch = ObjectType.Blank;
                Display.mvaddch(obj.getOPos(), ch.getValue());
                if (!Global.jump) {
                    Command.msleep(10L);
                }
            }
            /*
             * Get the new position
             */
            obj.setOPos(obj.getOPos().add(delta));
            if (IOUtil.step_ok(ch = Util.winat(obj.getOPos())) && ch != ObjectType.DOOR) {
                /*
                 * It hasn't hit anything yet, so display it
                 * If it alright.
                 */
                if (Chase.isSee(Human.instance, obj.getOPos()) && !Global.terse) {
                    Display.mvaddch(obj.getOPos(), obj.getDisplay().getValue());
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
    public static void wield(Player player) {

        Weapon oweapon = player.getWeapons().get(0);
        if (!ThingMethod.isDrop(player.getWeapons().size() > 0 ? player.getWeapons().get(0) : null)) {
            player.putOnWeapon(oweapon);
            return;
        }
        player.putOnWeapon(oweapon);
        Thing obj = Pack.get_item("wield", ObjectType.WEAPON);
        if (obj == null) {
            Global.after = false;
            return;
        }

        if (obj instanceof Armor) {
            IOUtil.msg("you can't wield armor");
            Global.after = false;
            return;
        }
        if (Misc.is_current(obj)) {
            Global.after = false;
            return;
        }

        String sp = ThingMethod.inventoryName(obj, true);
        player.putOnWeapon((Weapon) obj);
        if (!Global.terse) {
            IOUtil.addmsg("you are now ");
        }
        IOUtil.msg("wielding %s (%c)", sp, player.getPositionOfContent(obj));
    }


}
