package org.hiro;

import org.hiro.character.Human;
import org.hiro.character.Player;
import org.hiro.map.Coordinate;
import org.hiro.output.Display;
import org.hiro.things.ObjectType;
import org.hiro.things.Stick;
import org.hiro.things.Thing;
import org.hiro.things.ThingImp;
import org.hiro.things.Weapon;
import org.hiro.things.WeaponEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Functions to implement the various sticks one might find
 * while wandering around the dungeon.
 */
public class StickMethod {

    /*
     * charge_str:
     *	Return an appropriate string for a wand charge
     */
    static String charge_str(Stick obj) {
        String buf = "";

        if (obj.contains_o_flags(Const.ISKNOW) && Global.terse) {
            buf = " [" + obj.getTimes() + "]";
        } else if(obj.contains_o_flags(Const.ISKNOW)) {
            buf = " [" + obj.getTimes() + " charges]";
        }
        return buf;
    }


    /*
     * do_zap:
     *	Perform a zap with a wand
     */
    public static void do_zap(Player player, Thing obj) {
        if (obj == null) {
            return;
        }
        if (!(obj instanceof Stick)) {
            Global.after = false;
            IOUtil.msg("you can't zap with that!");
            return;
        }
        Stick stick = (Stick) obj;
        if (stick.isUse()) {
            IOUtil.msg("nothing happens");
            return;
        }

        stick.shake(player);
    }

    /*
     * fire_bolt:
     *	Fire a bolt in a given direction from a specific starting place
     */
    public static void fire_bolt(Coordinate start, Coordinate dir, String name) {
        List<Coordinate> spotPosition = new ArrayList<>();
        Weapon bolt = new Weapon(WeaponEnum.FLAME, 100);
        bolt._o_hurldmg = "6x6";
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
            if (spotPosition.size() <= i) {
                c1 = new Coordinate();
                spotPosition.add(c1);
            } else {
                c1 = spotPosition.get(i);
            }

            pos = dir.add(pos);
            c1 = pos;
            ObjectType ch = Util.winat(pos);
            ThingImp tp;
            switch (ch) {
                case DOOR:
                    /*
                     * this code is necessary if the hero is on a door
                     * and he fires at the wall the door is in, it would
                     * otherwise loop infinitely
                     */
                    if (Global.player._t_pos.equals(pos)) {
                        // defaultと同じ
                        if (!hit_hero && (tp = Util.getPlace(pos).p_monst) != null) {
                            hit_hero = true;
                            changed = !changed;
                            tp._t_oldch = Util.getPlace(pos).p_ch.getValue();
                            if (!Monst.save_throw(Const.VS_MAGIC, tp)) {
                                bolt._o_pos = pos;
                                used = true;
                                if (tp.getType() == 'D' && name.equals("flame")) {
                                    IOUtil.addmsg("the flame bounces");
                                    if (!Global.terse) {
                                        IOUtil.addmsg(" off the dragon");
                                    }
                                    IOUtil.endmsg();
                                } else
                                    WeaponMethod.hit_monster(pos, bolt);
                            } else if (ch.getValue() != 'M' || tp._t_disguise == 'M') {
                                if (Global.player._t_pos.equals(start))
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
                                Human.instance.deleteHp(Dice.roll(6, 6));
                                if (Human.instance.getHp() <= 0) {
                                    if (Global.player._t_pos.equals(start)) {
                                        Rip.death('b');
                                    } else {
                                        Rip.death(Util.getPlace(start).p_monst.getType());
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
                        Display.mvaddch(pos, (char) dirch);
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
                    if (!hit_hero && (tp = Util.getPlace(pos).p_monst) != null) {
                        hit_hero = true;
                        changed = !changed;
                        tp._t_oldch = Util.getPlace(pos).p_ch.getValue();
                        if (!Monst.save_throw(Const.VS_MAGIC, tp)) {
                            bolt._o_pos = pos;
                            used = true;
                            if (tp.getType() == 'D' && name.equals("flame")) {
                                IOUtil.addmsg("the flame bounces");
                                if (!Global.terse) {
                                    IOUtil.addmsg(" off the dragon");
                                }
                                IOUtil.endmsg();
                            } else
                                WeaponMethod.hit_monster(pos, bolt);
                        } else if (ch.getValue() != 'M' || tp._t_disguise == 'M') {
                            if (Global.player._t_pos.equals(start))
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
                            Human.instance.deleteHp(Dice.roll(6, 6));
                            if (Human.instance.getHp() <= 0) {
                                if (Global.player._t_pos.equals(start)) {
                                    Rip.death('b');
                                } else {
                                    Rip.death(Util.getPlace(start).p_monst.getType());
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
                    Display.mvaddch(pos, (char) dirch);
                    Display.refresh();
            }
        }
        for (int j = 0; j < i; j++) {
            Coordinate c2 = spotPosition.get(j);
            Display.mvaddch(c2, Util.getPlace(c2).p_ch.getValue());
        }
    }


}
