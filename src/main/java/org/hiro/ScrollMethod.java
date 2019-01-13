package org.hiro;

import org.hiro.character.Human;
import org.hiro.character.StateEnum;
import org.hiro.map.Coordinate;
import org.hiro.output.Display;
import org.hiro.things.ObjectType;
import org.hiro.things.ScrollEnum;
import org.hiro.things.Thing;
import org.hiro.things.ThingImp;

public class ScrollMethod {
    /*
     * read_scroll:
     *	Read a scroll from the pack and do the appropriate thing
     */
    static void read_scroll() {
        boolean MASTER = false;

        ThingImp obj = Pack.get_item("read", ObjectType.SCROLL);
        if (obj == null)
            return;
        if (obj._o_type != ObjectType.SCROLL) {
            if (!Global.terse) {
                IOUtil.msg("there is nothing on it to read");
            } else {
                IOUtil.msg("nothing to read");
            }
            return;
        }
        /*
         * Calculate the effect it has on the poor guy.
         */
        if (obj == Global.cur_weapon) {
            Global.cur_weapon = null;
        }
        /*
         * Get rid of the thing
         */
        Pack.leave_pack(obj, false, false);
        ThingImp orig_obj = obj;

        ScrollEnum s = ScrollEnum.get(obj._o_which);
        switch (s) {
            case S_CONFUSE:
                /*
                 * Scroll of monster confusion.  Give him that power.
                 */
                Human.instance.addState(StateEnum.CANHUH);
                IOUtil.msg("your hands begin to glow %s", Init.pick_color("red"));
                break;
            case S_ARMOR:
                if (Global.cur_armor != null) {
                    Global.cur_armor._o_arm--;
                    Global.cur_armor.delete_o_flags(Const.ISCURSED);
                    IOUtil.msg("your armor glows %s for a moment", Init.pick_color("silver"));
                }
                break;
            case S_HOLD:
                /*
                 * Hold monster scroll.  Stop all monsters within two spaces
                 * from chasing after the hero.
                 */

                int ch = 0;
                for (int x = Global.player._t_pos.x - 2; x <= Global.player._t_pos.x + 2; x++) {
                    if (x >= 0 && x < Const.NUMCOLS) {
                        for (int y = Global.player._t_pos.y - 2; y <= Global.player._t_pos.y + 2; y++) {
                            if (y >= 0 && y <= Const.NUMLINES - 1) {
                                if ((obj = Util.INDEX(y, x).p_monst) != null && obj.containsState(StateEnum.ISRUN)) {
                                    obj.removeState(StateEnum.ISRUN);
                                    obj.addState(StateEnum.ISHELD);
                                    ch++;
                                }
                            }
                        }
                    }
                }
                if (ch != 0) {
                    IOUtil.addmsg("the monster");
                    if (ch > 1) {
                        IOUtil.addmsg("s around you");
                    }
                    IOUtil.addmsg(" freeze");
                    if (ch == 1) {
                        IOUtil.addmsg("s");
                    }
                    IOUtil.endmsg();
                    Global.scr_info[ScrollEnum.S_HOLD.getValue()].know();
                } else
                    IOUtil.msg("you feel a strange sense of loss");
                break;
            case S_SLEEP:
                /*
                 * Scroll which makes you fall asleep
                 */
                Global.scr_info[ScrollEnum.S_SLEEP.getValue()].know();
                Global.no_command += Util.rnd(Const.SLEEPTIME) + 4;
                Human.instance.removeState(StateEnum.ISRUN);
                IOUtil.msg("you fall asleep");
                break;
            case S_CREATE:
                /*
                 * Create a monster:
                 * First look in a circle around him, next try his room
                 * otherwise give up
                 */
                int i = 0;
                Coordinate mp = new Coordinate();
                ObjectType cho;
                for (int y = Global.player._t_pos.y - 1; y <= Global.player._t_pos.y + 1; y++) {
                    for (int x = Global.player._t_pos.x - 1; x <= Global.player._t_pos.x + 1; x++) {
                        Coordinate tmp = new Coordinate(x, y);
                        /*
                         * Don't put a monster in top of the player.
                         */
                        if (Global.player._t_pos.equals(tmp)) {
                            continue;
                        }
                        /*
                         * Or anything else nasty
                         * Also avoid a xeroc which is disguised as scroll
                         */
                        else if (Util.getPlace(tmp).p_monst == null && IOUtil.step_ok(cho = Util.winat(tmp))) {
                            if (cho == ObjectType.SCROLL
                                    && Misc.find_obj(tmp)._o_which == ScrollEnum.S_SCARE.getValue()) {
                                continue;
                            } else if (Util.rnd(++i) == 0) {
                                mp = tmp;
                            }
                        }
                    }
                }
                if (i == 0)
                    IOUtil.msg("you hear a faint cry of anguish in the distance");
                else {
                    obj = new ThingImp();
                    Monst.new_monster(obj, Monst.randmonster(false), mp);
                }
                break;
            case S_ID_POTION:
            case S_ID_SCROLL:
            case S_ID_WEAPON:
            case S_ID_ARMOR:
            case S_ID_R_OR_S: {
                int id_type[] = {0, 0, 0, 0, 0, ObjectType.POTION.getValue(), ObjectType.SCROLL.getValue(), ObjectType.WEAPON.getValue(), ObjectType.ARMOR.getValue(), Const.R_OR_S};
                /*
                 * Identify, let him figure something out
                 */
                Global.scr_info[obj._o_which].know();
                IOUtil.msg("this scroll is an %s scroll", Global.scr_info[obj._o_which].getName());
                Wizard.whatis(true, id_type[obj._o_which]);
            }
            break;
            case S_MAP:
                /*
                 * Scroll of magic mapping.
                 */
                Global.scr_info[ScrollEnum.S_MAP.getValue()].know();
                IOUtil.msg("oh, now this scroll has a map on it");
                /*
                 * take all the things we want to keep hidden out of the window
                 */
                ObjectType chp;
                for (int y = 1; y < Const.NUMLINES - 1; y++)
                    for (int x = 0; x < Const.NUMCOLS; x++) {
                        Place pp = Util.INDEX(y, x);
                        switch (chp = pp.p_ch) {
                            case DOOR:
                            case STAIRS:
                                break;

                            case Horizon:
                            case Vert:
                                if ((pp.p_flags & Const.F_REAL) == 0) {
                                    chp = pp.p_ch = ObjectType.DOOR;
                                    pp.p_flags |= Const.F_REAL;
                                }
                                break;

                            case Blank:
                                if ((pp.p_flags & Const.F_REAL) != 0) {
                                    if ((pp.p_flags & Const.F_PASS) != 0) {
                                        if ((pp.p_flags & Const.F_REAL) == 0) {
                                            pp.p_ch = ObjectType.PASSAGE;
                                        }
                                        pp.p_flags |= (Const.F_SEEN | Const.F_REAL);
                                        chp = ObjectType.PASSAGE;
                                    } else {
                                        chp = ObjectType.Blank;
                                    }
                                    break;
                                }
                                pp.p_flags |= Const.F_REAL;
                                chp = pp.p_ch = ObjectType.PASSAGE;
                                /* FALLTHROUGH */

                            case PASSAGE:
                                if ((pp.p_flags & Const.F_REAL) == 0) {
                                    pp.p_ch = ObjectType.PASSAGE;
                                }
                                pp.p_flags |= (Const.F_SEEN | Const.F_REAL);
                                chp = ObjectType.PASSAGE;
                                break;

                            case FLOOR:
                                if ((pp.p_flags & Const.F_REAL) != 0) {
                                    chp = ObjectType.Blank;
                                } else {
                                    chp = ObjectType.TRAP;
                                    pp.p_ch = ObjectType.TRAP;
                                    pp.p_flags |= (Const.F_SEEN | Const.F_REAL);
                                }
                                break;

                            default:
                                if ((pp.p_flags & Const.F_PASS) != 0) {
                                    if ((pp.p_flags & Const.F_REAL) == 0) {
                                        pp.p_ch = ObjectType.PASSAGE;
                                    }
                                    pp.p_flags |= (Const.F_SEEN | Const.F_REAL);
                                    chp = ObjectType.PASSAGE;
                                } else {
                                    chp = ObjectType.Blank;
                                }
                                break;
                        }
                        if (chp != ObjectType.Blank) {
                            if ((obj = pp.p_monst) != null) {
                                obj._t_oldch = chp.getValue();
                            }
                            if (obj == null || !Human.instance.containsState(StateEnum.SEEMONST)) {
                                Display.mvaddch(y, x, chp.getValue());
                            }
                        }
                    }
                break;
            case S_FDET:
                /*
                 * Potion of gold detection
                 */
                boolean chb = false;
                // Display.wclear(hw);
                for (ThingImp obj2 : Global.lvl_obj) {
                    if (obj2._o_type == ObjectType.FOOD) {
                        chb = true;
                        // Display.wmove(hw, obj2._o_pos.y, obj2._o_pos.x);
                        // Display.waddch(hw, ObjectType.FOOD);
                    }
                }
                if (chb) {
                    Global.scr_info[ScrollEnum.S_FDET.getValue()].know();
                    IOUtil.show_win("Your nose tingles and you smell food.--More--");
                } else
                    IOUtil.msg("your nose tingles");
                break;
            case S_TELEP:
                /*
                 * Scroll of teleportation:
                 * Make him dissapear and reappear
                 */
            {
                Room cur_room = Global.player.t_room;
                Wizard.teleport();
                if (cur_room != Global.player.t_room)
                    Global.scr_info[ScrollEnum.S_TELEP.getValue()].know();
            }
            break;
            case S_ENCH:
                if (Global.cur_weapon == null || Global.cur_weapon._o_type != ObjectType.WEAPON) {
                    IOUtil.msg("you feel a strange sense of loss");
                } else {
                    Global.cur_weapon.delete_o_flags(Const.ISCURSED);
                    if (Util.rnd(2) == 0) {
                        Global.cur_weapon._o_hplus++;
                    } else {
                        Global.cur_weapon._o_dplus++;
                    }
                    IOUtil.msg("your %s glows %s for a moment",
                            Global.weap_info[Global.cur_weapon._o_which].getName(), Init.pick_color("blue"));
                }
                break;
            case S_SCARE:
                /*
                 * Reading it is a mistake and produces laughter at her
                 * poor boo boo.
                 */
                IOUtil.msg("you hear maniacal laughter in the distance");
                break;
            case S_REMOVE:
                uncurse(Global.cur_armor);
                uncurse(Global.cur_weapon);
                uncurse(Global.cur_ring[Const.LEFT]);
                uncurse(Global.cur_ring[Const.RIGHT]);
                IOUtil.msg(Misc.choose_str("you feel in touch with the Universal Onenes",
                        "you feel as if somebody is watching over you"));
                break;
            case S_AGGR:
                /*
                 * This scroll aggravates all the monsters on the current
                 * level and sets them running towards the hero
                 */
                Misc.aggravate();
                IOUtil.msg("you hear a high pitched humming noise");
                break;
            case S_PROTECT:
                if (Global.cur_armor != null) {
                    Global.cur_armor.add_o_flags(Const.ISPROT);
                    IOUtil.msg("your armor is covered by a shimmering %s shield",
                            Init.pick_color("gold"));
                } else
                    IOUtil.msg("you feel a strange sense of loss");
                break;
            default:
                if (MASTER) {
                    IOUtil.msg("what a puzzling scroll!");
                    return;
                }
        }
        obj = orig_obj;
        Misc.look(true);    /* put the result of the scroll on the screen */
        IOUtil.status();


    }

    /*
     * uncurse:
     *	Uncurse an item
     */
    static void uncurse(Thing obj) {
        if (obj != null)
            obj.delete_o_flags(Const.ISCURSED);
    }

}
