package org.hiro;

import org.hiro.character.Player;
import org.hiro.character.StateEnum;
import org.hiro.map.AbstractCoordinate;
import org.hiro.map.Coordinate;
import org.hiro.map.RoomInfoEnum;
import org.hiro.output.Display;
import org.hiro.things.ObjectType;
import org.hiro.things.OriginalMonster;
import org.hiro.things.Thing;
import org.hiro.things.Weapon;
import org.hiro.things.WeaponEnum;
import org.hiro.things.ringtype.SustainStrengthRing;
import org.hiro.things.scrolltype.Scare;

/**
 * hero movement commands
 */
public class Move {

    /*
     * door_open:
     *	Called to illuminate a room.  If it is dark, remove anything
     *	that might move.
     */

    static void door_open(Room rp) {
        if (!rp.containInfo(RoomInfoEnum.ISGONE)) {
            for (int y = rp.r_pos.getY(); y < rp.r_pos.getY() + rp.r_max.getY(); y++) {
                for (int x = rp.r_pos.getX(); x < rp.r_pos.getX() + rp.r_max.getX(); x++) {
                    if (Character.isUpperCase(Util.winat(new Coordinate(x, y)).getValue())) {
                        Monst.wake_monster(y, x);
                    }
                }
            }
        }
    }

    /*
     * do_move:
     *	Check to see that a move is legal.  If it is handle the
     * consequences (fighting, picking up, etc.)
     */
    public static void do_move(Player player, int dy, int dx) {

        Global.firstmove = false;
        if (Global.no_move != 0) {
            Global.no_move--;
            IOUtil.msg("you are still stuck in the bear trap");
            return;
        }
        AbstractCoordinate nh = new Coordinate();
        /*
         * Do a confused move (maybe)
         */
        if (player.containsState(StateEnum.ISHUH) && Util.rnd(5) != 0) {
            nh = rndmove(Global.player);
            if (nh.equals(player.getPosition())) {
                Global.after = false;
                Global.running = false;
                Global.to_death = false;
                return;
            }
        } else {
            over:
            nh = player.getPosition().add(new Coordinate(dx, dy));
        }

        /*
         * Check if he tried to move off the screen or make an illegal
         * diagonal move, and stop him if he did.
         */
        if (nh.getX() < 0 || nh.getX() >= Const.NUMCOLS || nh.getY() <= 0 || nh.getY() >= Const.NUMLINES - 1) {
            // goto hit_bound;
        }
        if (!Chase.diag_ok(player.getPosition(), nh)) {
            Global.after = false;
            Global.running = false;
            return;
        }
        if (Global.running && player.getPosition().equals(nh)) {
            Global.after = Global.running = false;
        }
        ObjectType ch;
        char fl;
        fl = (char) Util.flat(nh);
        ch = Util.winat(nh);
        if ((fl & Const.F_REAL) == 0 && ch == ObjectType.FLOOR) {
            if (!player.containsState(StateEnum.ISLEVIT)) {
                Util.getPlace(nh).p_ch = ch = ObjectType.TRAP;
                Util.getPlace(nh).p_flags |= Const.F_REAL;
            }
        } else if (player.containsState(StateEnum.ISHELD) && ch.getValue() != 'F') {
            IOUtil.msg("you are being held");
            return;
        }
        switch (ch) {
            case Blank:
            case Vert:
            case Horizon:
                hit_bound:
                if (Global.passgo && Global.running && player.getRoom().containInfo(RoomInfoEnum.ISGONE)
                        && !player.containsState(StateEnum.ISBLIND)) {
                    boolean b1, b2;
                    switch (Global.runch) {
                        case 'h':
                        case 'l':
                            b1 = (player.getPositionY() != 1 && turn_ok(player.getPositionY() - 1, player.getPositionX()));
                            b2 = (player.getPositionY() != Const.NUMLINES - 2 && turn_ok(player.getPositionY() + 1, player.getPositionX()));
                            if (b1 == b2) {
                                break;
                            }
                            if (b1) {
                                Global.runch = 'k';
                                dy = -1;
                            } else {
                                Global.runch = 'j';
                                dy = 1;
                            }
                            dx = 0;
                            turnref();
                        case 'j':
                        case 'k':
                            b1 = (player.getPositionX() != 0 && turn_ok(player.getPositionY(), player.getPositionX() - 1));
                            b2 = (player.getPositionX() != Const.NUMCOLS - 1 && turn_ok(player.getPositionY(), player.getPositionX() + 1));
                            if (b1 == b2) {
                                break;
                            }
                            if (b1) {
                                Global.runch = 'h';
                                dx = -1;
                            } else {
                                Global.runch = 'l';
                                dx = 1;
                            }
                            dy = 0;
                            turnref();
                    }
                    // goto over;
                }
                Global.running = false;
                Global.after = false;
                break;
            case DOOR:
                Global.running = false;
                if ((Util.flat(player.getPosition()) & Const.F_PASS) != 0) {
                    Rooms.enter_room(nh);
                }
                move_stuff(fl, nh);
            case TRAP:
                ch = ObjectType.get((char) be_trapped(player, nh));
                if (ch.getValue() == TrapEnum.T_DOOR.getValue() || ch.getValue() == TrapEnum.T_TELEP.getValue()) {
                    return;
                }
                move_stuff(fl, nh);
            case PASSAGE:
                /*
                 * when you're in a corridor, you don't know if you're in
                 * a maze room or not, and there ain't no way to find out
                 * if you're leaving a maze room, so it is necessary to
                 * always recalculate proom.
                 */
                player.setRoom(Chase.roomin(player.getPosition()));
                move_stuff(fl, nh);
            case FLOOR:
                if ((fl & Const.F_REAL) == 0) {
                    be_trapped(player, player.getPosition());
                }
                move_stuff(fl, nh);
            case STAIRS:
                Global.seenstairs = true;
                /* FALLTHROUGH */
            default:
                Global.running = false;
                if (Character.isUpperCase(ch.getValue()) || Util.getPlace(nh).p_monst != null) {
                    Fight.fight(nh, player.getWeapons().size() > 0 ? player.getWeapons().get(0) : null, false);
                } else {
                    if (ch != ObjectType.STAIRS) {
                        Global.take = ch;
                    }
                    move_stuff(fl, nh);
                }
        }
    }

    static private void move_stuff(char fl, AbstractCoordinate nh) {
        Display.mvaddch(Global.player._t_pos, Pack.floor_at().getValue());
        if ((fl & Const.F_PASS) != 0 && Util.getPlace(Global.oldpos).p_ch == ObjectType.DOOR) {
            Rooms.leave_room(nh);
        }
        Global.player._t_pos = nh;
    }

    /*
     * rndmove:
     *	Move in a random direction if the monster/person is confused
     */
    static AbstractCoordinate rndmove(OriginalMonster who) {
        AbstractCoordinate ret = who.getPosition().add(new Coordinate(Util.rnd(3) - 1, Util.rnd(3) - 1));  /* what we will be returning */

        /*
         * Now check to see if that's a legal move.  If not, don't move.
         * (I.e., bump into the wall or whatever)
         */
        if (who.getPosition().equals(ret)) {
            return ret;
        }
        bad:
        {
            if (!Chase.diag_ok(who.getPosition(), ret)) {
                break bad;
            } else {
                ObjectType ch = Util.winat(ret);
                if (!IOUtil.step_ok(ch)) {
                    break bad;
                }
                if (ch.getValue() == ObjectType.SCROLL.getValue()) {
                    Thing obj2 = null;
                    for (Thing obj : Global.lvl_obj) {
                        obj2 = obj;
                        if (obj.getOPos().equals(ret)) {
                            break;
                        }
                    }
                    if (obj2 instanceof Scare) {
                        break bad;
                    }
                }
            }
            return ret;

        }
        ret = who.getPosition();
        return ret;
    }

    /*
     * turn_ok:
     *	Decide whether it is legal to turn onto the given space
     */
    static boolean turn_ok(int y, int x) {
        Place pp = Util.INDEX(y, x);
        return (pp.p_ch == ObjectType.DOOR
                || (pp.p_flags & (Const.F_REAL | Const.F_PASS)) == (Const.F_REAL | Const.F_PASS));
    }

    /*
     * turnref:
     *	Decide whether to refresh at a passage turning or not
     */
    static void turnref() {
        Place pp = Util.getPlace(Global.player._t_pos);
        if ((pp.p_flags & Const.F_SEEN) == 0) {
            if (Global.jump) {
                // leaveok(stdscr, true);
                Display.refresh();
                // leaveok(stdscr, false);
            }
            pp.p_flags |= Const.F_SEEN;
        }
    }

    /*
     * be_trapped:
     *	The guy stepped on a trap.... Make him pay.
     */
    static int be_trapped(Player player, AbstractCoordinate tc) {
        Place pp;
        Thing arrow;
        ObjectType tr;

        if (player.containsState(StateEnum.ISLEVIT)) {
            return TrapEnum.T_RUST.getValue();    /* anything that's not a door or teleport */
        }
        Global.running = false;
        Global.count = 0;
        pp = Util.getPlace(tc);
        pp.p_ch = ObjectType.TRAP;
        tr = ObjectType.get((char) (pp.p_flags & Const.F_TMASK));
        pp.p_flags |= Const.F_SEEN;
        TrapEnum e = TrapEnum.get(tr.getValue());
        if(e != null) {
            switch (e) {
                case T_DOOR:
                    player.upstairs();
                    New_Level.new_level(player);
                    IOUtil.msg("you fell into a trap!");
                    break;
                case T_BEAR:
                    Global.no_move += Const.BEARTIME;
                    IOUtil.msg("you are caught in a bear trap");
                    break;
                case T_MYST:
                    switch (Util.rnd(11)) {
                        case 0:
                            IOUtil.msg("you are suddenly in a parallel dimension");
                            break;
                        case 1:
                            // IOUtil.msg("the light in here suddenly seems %s", rainbow[rnd(cNCOLORS)]);
                            break;
                        case 2:
                            IOUtil.msg("you feel a sting in the side of your neck");
                            break;
                        case 3:
                            IOUtil.msg("multi-colored lines swirl around you, then fade");
                            break;
                        case 4:
                            // IOUtil.msg("a %s light flashes in your eyes", rainbow[rnd(cNCOLORS)]);
                            break;
                        case 5:
                            IOUtil.msg("a spike shoots past your ear!");
                            break;
                        case 6:
                            // IOUtil.msg("%s sparks dance across your armor", rainbow[rnd(cNCOLORS)]);
                            break;
                        case 7:
                            IOUtil.msg("you suddenly feel very thirsty");
                            break;
                        case 8:
                            IOUtil.msg("you feel time speed up suddenly");
                            break;
                        case 9:
                            IOUtil.msg("time now seems to be going slower");
                            break;
                        case 10:
                            // IOUtil.msg("you pack turns %s!", rainbow[rnd(cNCOLORS)]);
                        default:
                            break;
                    }
                    break;
                case T_SLEEP:
                    Global.no_command += Const.SLEEPTIME;
                    player.removeState(StateEnum.ISRUN);
                    IOUtil.msg("a strange white mist envelops you and you fall asleep");
                    break;
                case T_ARROW:
                    if (Fight.swing(Global.player.getStatus().s_lvl - 1, Global.player.getStatus().s_arm, 1)) {
                        player.deleteHp(Dice.roll(1, 6));
                        if (player.getHp() <= 0) {
                            IOUtil.msg("an arrow killed you");
                            Rip.death('a');
                        } else {
                            IOUtil.msg("oh no! An arrow shot you");
                        }
                    } else {
                        arrow = new Weapon(WeaponEnum.ARROW, 0);
                        arrow.addCount(1);
                        arrow.setOPos(player.getPosition());
                        WeaponMethod.fall(arrow, false);
                        IOUtil.msg("an arrow shoots past you");
                    }
                    break;
                case T_TELEP:
                    /*
                     * since the hero's leaving, look() won't put a TRAP
                     * down for us, so we have to do it ourself
                     */
                    Wizard.teleport(player);
                    Display.mvaddch(tc, ObjectType.TRAP.getValue());
                    break;
                case T_DART:
                    if (!Fight.swing(Global.player.getStatus().s_lvl + 1, Global.player.getStatus().s_arm, 1)) {
                        IOUtil.msg("a small dart whizzes by your ear and vanishes");
                    } else {
                        player.deleteHp(Dice.roll(1, 4));
                        if (player.getHp() <= 0) {
                            IOUtil.msg("a poisoned dart killed you");
                            Rip.death('d');
                        }
                        if (!SustainStrengthRing.isInclude(player.getRings()) && !Monst.save(Const.VS_POISON)) {
                            Misc.chg_str(-1);
                        }
                        IOUtil.msg("a small dart just hit you in the shoulder");
                    }
                    break;
                case T_RUST:
                    IOUtil.msg("a gush of water hits you on the head");
                    rust_armor(player);
            }
        }
        Mach_dep.flush_type();
        return tr.getValue();
    }


    /*
     * rust_armor:
     *	Rust the given armor, if it is a legal kind to rust, and we
     *	aren't wearing a magic ring.
     *  錆びた鎧
     */
    static void rust_armor(Player player) {
        boolean result = player.getArmor().rust(player);
        if (result && !Global.to_death) {
            IOUtil.msg("the rust vanishes instantly");
        } else {
            if (!Global.terse) {
                IOUtil.msg("your armor appears to be weaker now. Oh my!");
            } else {
                IOUtil.msg("your armor weakens");
            }
        }
    }

    /*
     * do_run:
     *	Start the hero running
     */
    public static void do_run(int ch) {
        Global.running = true;
        Global.after = false;
        Global.runch = ch;
    }


}
