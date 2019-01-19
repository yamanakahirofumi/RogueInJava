package org.hiro;

import org.hiro.character.Human;
import org.hiro.character.StateEnum;
import org.hiro.map.Coordinate;
import org.hiro.output.Display;
import org.hiro.things.Armor;
import org.hiro.things.ArmorEnum;
import org.hiro.things.ObjectType;
import org.hiro.things.RingEnum;
import org.hiro.things.ThingImp;
import org.hiro.things.Weapon;
import org.hiro.things.WeaponEnum;

import java.util.concurrent.TimeUnit;

public class Command {

    /*
     * command:
     *	Process the user commands
     */
    static void command() {
        int ntimes = 1;            /* Number of player moves */
        // void endmsg2 ();

        boolean MASTER = false;

        if (Human.instance.containsState(StateEnum.ISHASTE)) {
            ntimes++;
        }
        /*
         * Let the daemons start up
         */
        Daemon.do_daemons(Const.BEFORE);
        Daemon.do_fuses(Const.BEFORE);
        int direction = 0;
        int countch = 0;
        boolean newcount = false;
        while (ntimes-- != 0) {
            Global.again = false;
            if (Global.has_hit) {
                IOUtil.endmsg();
                Global.has_hit = false;
            }
            /*
             * these are illegal things for the player to be, so if any are
             * set, someone's been poking in memeory
             */
            if (Human.instance.containsState(StateEnum.ISSLOW) || Human.instance.containsState(StateEnum.ISGREED)
                    || Human.instance.containsState(StateEnum.ISINVIS)
                    || Human.instance.containsState(StateEnum.ISREGEN)
                    || Human.instance.containsState(StateEnum.ISTARGET)) {
                System.exit(1);
            }

            Misc.look(true);
            if (!Global.running) {
                Global.door_stop = false;
            }
            IOUtil.status();
            Global.lastscore = Global.purse;
            Display.move(Global.player._t_pos);
            if (!((Global.running || Global.count != 0) && Global.jump)) {
                Display.refresh();            /* Draw screen */
            }
            Global.take = ObjectType.Initial;
            Global.after = true;
            /*
             * Read command or continue run
             */
            if (MASTER) {
                if (Global.wizard) {
                    Global.noscore = true;
                }
            }
            int ch;
            if (Global.no_command == 0) {
                if (Global.running || Global.to_death) {
                    ch = Global.runch;
                } else if (Global.count != 0) {
                    ch = countch;
                } else {
                    ch = IOUtil.readchar();
                    Global.move_on = false;
                    if (Global.mpos != 0) {       /* Erase message if its there */
                        if (ch != '.') {
                            IOUtil.endmsg2(); /* msg(""); */
                        }
                    }
                }
            } else
                ch = '.';
            if (Global.no_command != 0) {
                if (--Global.no_command == 0) {
                    IOUtil.msg("you can move again");
                }
            } else {
                /*
                 * check for prefixes
                 */
                newcount = false;
                if (Character.isDigit(ch)) {
                    Global.count = 0;
                    newcount = true;
                    while (Character.isDigit(ch)) {
                        Global.count = Global.count * 10 + (ch - '0');
                        if (Global.count > 255) {
                            Global.count = 255;
                        }
                        ch = IOUtil.readchar();
                        if (Global.mpos != 0) {
                            IOUtil.endmsg2();
                        }
                    }
                    countch = ch;
                    /*
                     * turn off count for commands which don't make sense
                     * to repeat
                     */
                    switch (ch) {
                        case ('B' & 037):
                        case ('H' & 037):
                        case ('J' & 037):
                        case ('K' & 037):
                        case ('L' & 037):
                        case ('N' & 037):
                        case ('U' & 037):
                        case ('Y' & 037):
                        case '.':
                        case 'a':
                        case 'b':
                        case 'h':
                        case 'j':
                        case 'k':
                        case 'l':
                        case 'm':
                        case 'n':
                        case 'q':
                        case 'r':
                        case 's':
                        case 't':
                        case 'u':
                        case 'y':
                        case 'z':
                        case 'B':
                        case 'C':
                        case 'H':
                        case 'I':
                        case 'J':
                        case 'K':
                        case 'L':
                        case 'N':
                        case 'U':
                        case 'Y':
// #ifdef MASTER
                        case ('D' & 037):
                        case ('A' & 037):
// #endif
                            break;
                        default:
                            Global.count = 0;
                    }
                }
                /*
                 * execute a command
                 */
                if ((Global.count != 0) && !Global.running) {
                    Global.count--;
                }
                if (ch != 'a' && ch != Const.ESCAPE &&
                        !(Global.running || Global.count != 0 || Global.to_death)) {
                    Global.l_last_comm = Global.last_comm;
                    Global.l_last_dir = Global.last_dir;
                    Global.l_last_pick = Global.last_pick;
                    Global.last_comm = (char) ch;
                    Global.last_dir = '\0';
                    Global.last_pick = null;
                }
                ThingImp mp;
                over:
                switch (ch) {
                    case ',': {
                        ThingImp obj2 = null;
                        int found = 0;
                        for (ThingImp obj : Global.lvl_obj) {
                            obj2 = obj;
                            if (Global.player._t_pos.equals(obj._o_pos)) {
                                found = 1;
                                break;
                            }
                        }

                        if (found != 0) {
                            if (levit_check()) {
                                ;
                            } else {
                                Pack.pick_up();
                            }
                        } else {
                            if (!Global.terse) {
                                IOUtil.addmsg("there is ");
                            }
                            IOUtil.addmsg("nothing here");
                            if (!Global.terse) {
                                IOUtil.addmsg(" to pick up");
                            }
                            IOUtil.endmsg();
                        }
                    }
                    break;
                    case '!':
                        // shell(); // TODO: 後で
                        break;
                    case 'h':
                        Move.do_move(0, -1);
                        break;
                    case 'j':
                        Move.do_move(1, 0);
                        break;
                    case 'k':
                        Move.do_move(-1, 0);
                        break;
                    case 'l':
                        Move.do_move(0, 1);
                        break;
                    case 'y':
                        Move.do_move(-1, -1);
                        break;
                    case 'u':
                        Move.do_move(-1, 1);
                        break;
                    case 'b':
                        Move.do_move(1, -1);
                        break;
                    case 'n':
                        Move.do_move(1, 1);
                        break;
                    case 'H':
                        Move.do_run('h');
                        break;
                    case 'J':
                        Move.do_run('j');
                        break;
                    case 'K':
                        Move.do_run('k');
                        break;
                    case 'L':
                        Move.do_run('l');
                        break;
                    case 'Y':
                        Move.do_run('y');
                        break;
                    case 'U':
                        Move.do_run('u');
                        break;
                    case 'B':
                        Move.do_run('b');
                        break;
                    case 'N':
                        Move.do_run('n');
                        break;
                    case ('H' & 037):
                    case ('J' & 037):
                    case ('K' & 037):
                    case ('L' & 037):
                    case ('Y' & 037):
                    case ('U' & 037):
                    case ('B' & 037):
                    case ('N' & 037): {
                        if (!Human.instance.containsState(StateEnum.ISBLIND)) {
                            Global.door_stop = true;
                            Global.firstmove = true;
                        }
                        if (Global.count != 0 && !newcount)
                            ch = direction;
                        else {
                            ch += ('A' - Util.CTRL('A'));
                            direction = ch;
                        }
                        // goto over;
                    }
                    break;
                    case 'F':
                        Global.kamikaze = true;
                        /* FALLTHROUGH */
                    case 'f':
                        if (!Misc.get_dir()) {
                            Global.after = false;
                            break;
                        }
                        Global.delta = Global.player._t_pos.add(Global.delta);
                        if (((mp = Util.getPlace(Global.delta).p_monst) == null)
                                || ((!Chase.see_monst(mp)) && !Human.instance.containsState(StateEnum.SEEMONST))) {
                            if (!Global.terse) {
                                IOUtil.addmsg("I see ");
                            }
                            IOUtil.msg("no monster there");
                            Global.after = false;
                        } else if (Chase.diag_ok(Global.player._t_pos, Global.delta)) {
                            Global.to_death = true;
                            Global.max_hit = 0;
                            mp.addState(StateEnum.ISTARGET);
                            Global.runch = ch = Global.dir_ch;
                            // goto over;
                        }
                        break;
                    case 't':
                        if (!Misc.get_dir()) {
                            Global.after = false;
                        } else {
                            WeaponMethod.missile(Global.delta);
                        }
                        break;
                    case 'a':
                        if (Global.last_comm == '\0') {
                            IOUtil.msg("you haven't typed a command yet");
                            Global.after = false;
                        } else {
                            ch = Global.last_comm;
                            Global.again = true;
                            // goto over;
                        }
                        break;
                    case 'q':
                        Potions.quaff();
                        break;
                    case 'Q':
                        Global.after = false;
                        Global.q_comm = true;
                        Main2.quit(0);
                        Global.q_comm = false;
                        break;
                    case 'i':
                        Global.after = false;
                        Pack.inventory(Global.player.getBaggage(), ObjectType.Initial);
                        break;
                    case 'I':
                        Global.after = false;
                        Pack.picky_inven();
                        break;
                    case 'd':
                        ThingMethod.drop();
                        break;
                    case 'r':
                        ScrollMethod.read_scroll();
                        break;
                    case 'e':
                        ThingImp food = Pack.get_item("eat", ObjectType.FOOD);
                        Misc.eat(food);
                        break;
                    case 'w':
                        WeaponMethod.wield();
                        break;
                    case 'W':
                        ArmorMethod.wear();
                        break;
                    case 'T':
                        ArmorMethod.take_off();
                        break;
                    case 'P':
                        RingMethod.ring_on();
                        break;
                    case 'R':
                        RingMethod.ring_off();
                        break;
                    case 'o':
                        Options.option();
                        Global.after = false;
                        break;
                    case 'c':
                        // call();  // TODO: 後で
                        Global.after = false;
                        break;
                    case '>':
                        Global.after = false;
                        d_level();
                        break;
                    case '<':
                        Global.after = false;
                        u_level();
                        break;
                    case '?':
                        Global.after = false;
                        // help(); // TODO: 後で
                        break;
                    case '/':
                        Global.after = false;
                        // identify();  // TODO: 後で
                        break;
                    case 's':
                        search();
                        break;
                    case 'z':
                        if (Misc.get_dir()) {
                            StickMethod.do_zap();
                        } else {
                            Global.after = false;
                        }
                        break;
                    case 'D':
                        Global.after = false;
                        // discovered(); // TODO: 後で
                        break;
                    case ('P' & 037):
                        Global.after = false;
                        IOUtil.msg(Global.huh);
                        break;
                    case ('R' & 037):
                        Global.after = false;
                        // clearok(curscr, true);
                        // wrefresh(curscr);
                        break;
                    case 'v':
                        Global.after = false;
                        IOUtil.msg("version %s. (mctesq was here)", Global.release);
                        break;
                    case 'S':
                        Global.after = false;
                        // save_game(); // TODO: あとで
                        break;
                    case '.':
                        ;            /* Rest command */
                        break;
                    case ' ':
                        Global.after = false;    /* "Legal" illegal command */
                        break;
                    case '^':
                        Global.after = false;
                        if (Misc.get_dir()) {
                            Global.delta = Global.player._t_pos.add(Global.delta);
                            int fp = Util.flat(Global.delta);
                            if (!Global.terse) {
                                IOUtil.addmsg("You have found ");
                            }
                            if (Util.getPlace(Global.delta).p_ch != ObjectType.TRAP) {
                                IOUtil.msg("no trap there");
                            } else if (Human.instance.containsState(StateEnum.ISHALU)) {
                                IOUtil.msg(Global.tr_name[Util.rnd(Const.NTRAPS)]);
                            } else {
                                IOUtil.msg(Global.tr_name[fp & Const.F_TMASK]);
                                fp |= Const.F_SEEN;
                            }
                        }
// #ifdef MASTER
                        break;
                    case '+':
                        Global.after = false;
                        if (Global.wizard) {
                            Global.wizard = false;
                            Potions.turn_see(true);
                            IOUtil.msg("not wizard any more");
                        } else {
                            // Global.wizard = passwd(); //後で
                            if (Global.wizard) {
                                Global.noscore = true;
                                Potions.turn_see(false);
                                // IOUtil.msg("you are suddenly as smart as Ken Arnold in dungeon #%d", dnum);
                            } else {
                                IOUtil.msg("sorry");
                            }
                        }
// #endif
                        break;
                    case Const.ESCAPE:    /* Escape */
                        Global.door_stop = false;
                        Global.count = 0;
                        Global.after = false;
                        Global.again = false;
                        break;
                    case 'm':
                        Global.move_on = true;
                        if (!Misc.get_dir()) {
                            Global.after = false;
                        } else {
                            ch = Global.dir_ch;
                            countch = Global.dir_ch;
                            // goto over;
                        }
                        break;
                    case ')':
                        current(Human.instance.getWeapons().size() == 0 ? null : Human.instance.getWeapons().get(0), "wielding", null);
                        break;
                    case ']':
                        current(Global.cur_armor, "wearing", null);
                        break;
                    case '=':
                        current(Global.cur_ring[Const.LEFT], "wearing",
                                Global.terse ? "(L)" : "on left hand");
                        current(Global.cur_ring[Const.RIGHT], "wearing",
                                Global.terse ? "(R)" : "on right hand");
                        break;
                    case '@':
                        Global.stat_msg = true;
                        IOUtil.status();
                        Global.stat_msg = false;
                        Global.after = false;
                        break;
                    default:
                        Global.after = false;
                        // #ifdef MASTER
                        if (Global.wizard) {
                            switch (ch) {
                                case '|':
                                    IOUtil.msg("@ %d,%d", String.valueOf(Global.player._t_pos.y), String.valueOf(Global.player._t_pos.x));
                                    break;
                                case 'C':
                                    // create_obj(); //TODO: 後で
                                    break;
                                case '$':
                                    IOUtil.msg("inpack = %d", Global.inpack);
                                    break;
                                case ('G' & 037):
                                    Pack.inventory(Global.lvl_obj, ObjectType.Initial);
                                    break;
                                case ('W' & 037):
                                    Wizard.whatis(false, 0);
                                    break;
                                case ('D' & 037):
                                    Human.instance.upstairs();
                                    New_Level.new_level();
                                    break;
                                case ('A' & 037):
                                    Human.instance.downstairs();
                                    New_Level.new_level();
                                    break;
                                case ('F' & 037):
                                    // show_map(); // TODO: 後で
                                    break;
                                case ('T' & 037):
                                    Wizard.teleport();
                                    break;
                                case ('E' & 037):
                                    IOUtil.msg("food left: %d", Global.food_left);
                                    break;
                                case ('Q' & 037):
                                    // add_pass();  // TODO: 後で
                                    break;
                                case ('X' & 037):
                                    Potions.turn_see(Human.instance.containsState(StateEnum.SEEMONST));
                                    break;
                                case '~': {
                                    ThingImp item;
                                    if ((item = Pack.get_item("charge", ObjectType.STICK)) != null) {
                                        item._o_arm = 10000;
                                    }
                                }
                                break;
                                case ('I' & 037): {
                                    int i;
                                    ThingImp obj;

                                    for (i = 0; i < 9; i++) {
                                        Potions.raise_level();
                                    }
                                    /*
                                     * Give him a sword (+1,+1)
                                     */
                                    obj = new Weapon(WeaponEnum.TWOSWORD, 1);
                                    obj._o_dplus = 1;
                                    Pack.add_pack(obj, true);
                                    Human.instance.putOnWeapon((Weapon) obj);
                                    /*
                                     * And his suit of armor
                                     */
                                    Armor armor = new Armor(ArmorEnum.PLATE_MAIL, -5, Const.ISKNOW);
                                    Global.cur_armor = armor;
                                    Pack.add_pack(armor, true);
                                }
                                break;
                                case '*':
                                    // pr_list(); // 後で
                                    break;
                                default:
                                    illcom(ch);
                            }
                        } else {
                            // #endif
                            illcom(ch);
                        }
                }
                /*
                 * turn off flags if no longer needed
                 */
                if (!Global.running) {
                    Global.door_stop = false;
                }
            }
            /*
             * If he ran into something to take, let him pick it up.
             */
            if (Global.take != ObjectType.Initial) {
                Pack.pick_up();
            }
            if (!Global.running) {
                Global.door_stop = false;
            }
            if (!Global.after) {
                ntimes++;
            }
            if (!Global.jump) {
                msleep(5L);
            }
        }
        Daemon.do_daemons(Const.AFTER);
        Daemon.do_fuses(Const.AFTER);
        if (Util.ISRING(Const.LEFT, RingEnum.R_SEARCH)) {
            search();
        } else if (Util.ISRING(Const.LEFT, RingEnum.R_TELEPORT) && Util.rnd(50) == 0) {
            Wizard.teleport();
        }
        if (Util.ISRING(Const.RIGHT, RingEnum.R_SEARCH)) {
            search();
        } else if (Util.ISRING(Const.RIGHT, RingEnum.R_TELEPORT) && Util.rnd(50) == 0) {
            Wizard.teleport();
        }
    }

    /*
     * search:
     *	player gropes about him to find hidden things.
     */
    static void search() {

        int ey = Global.player._t_pos.y + 1;
        int ex = Global.player._t_pos.x + 1;
        int probinc = (Human.instance.containsState(StateEnum.ISHALU) ? 3 : 0);
        probinc += (Human.instance.containsState(StateEnum.ISBLIND) ? 2 : 0);
        boolean found = false;
        for (int y = Global.player._t_pos.y - 1; y <= ey; y++)
            for (int x = Global.player._t_pos.x - 1; x <= ex; x++) {
                Coordinate target = new Coordinate(x, y);
                if (Global.player._t_pos.equals(target)) {
                    continue;
                }
                int fp = Util.flat(target);
                if ((fp & Const.F_REAL) == 0) {
                    switch (Util.getPlace(target).p_ch) {
                        case Vert:
                        case Horizon:
                            if (Util.rnd(5 + probinc) != 0) {
                                break;
                            }
                            Util.getPlace(target).p_ch = ObjectType.DOOR;
                            IOUtil.msg("a secret door");
                            found = foundone(target);
                            break;
                        case FLOOR:
                            if (Util.rnd(2 + probinc) != 0) {
                                break;
                            }
                            Util.getPlace(target).p_ch = ObjectType.TRAP;
                            if (!Global.terse) {
                                IOUtil.addmsg("you found ");
                            }
                            if (Human.instance.containsState(StateEnum.ISHALU)) {
                                IOUtil.msg(Global.tr_name[Util.rnd(Const.NTRAPS)]);
                            } else {
                                IOUtil.msg(Global.tr_name[fp & Const.F_TMASK]);
                                Util.getPlace(target).p_flags |= Const.F_SEEN;
                            }
                            found = foundone(target);
                            break;
                        case Blank:
                            if (Util.rnd(3 + probinc) != 0) {
                                break;
                            }
                            Util.getPlace(target).p_ch = ObjectType.PASSAGE;
                            found = foundone(target);
                    }
                }
            }
        if (found) {
            Misc.look(false);
        }
    }

    static private boolean foundone(Coordinate coordinate) {
        Util.getPlace(coordinate).p_flags |= Const.F_REAL;
        Global.count = 0;
        Global.running = false;
        return true;
    }

    static void msleep(long msec) {
        try {
            TimeUnit.MILLISECONDS.sleep(msec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
     * illcom:
     *	What to do with an illegal command
     */
    static void illcom(int ch) {
        Global.save_msg = false;
        Global.count = 0;
        IOUtil.msg("illegal command '%s'", Display.unctrl(ch));
        Global.save_msg = true;
    }


    /*
     * current:
     *	Print the current weapon/armor
     */
    static void current(ThingImp cur, String how, String where) {
        Global.after = false;
        if (cur != null) {
            if (!Global.terse) {
                IOUtil.addmsg("you are %s (", how);
            }
            Global.inv_describe = false;
            IOUtil.addmsg("%c) %s", cur._o_packch, ThingMethod.inv_name(cur, true));
            Global.inv_describe = true;
            if (where != null) {
                IOUtil.addmsg(" %s", where);
            }
            IOUtil.endmsg();
        } else {
            if (!Global.terse) {
                IOUtil.addmsg("you are ");
            }
            IOUtil.addmsg("%s nothing", how);
            if (where != null) {
                IOUtil.addmsg(" %s", where);
            }
            IOUtil.endmsg();
        }
    }

    /*
     * d_level:
     *	He wants to go down a level
     */
    static void d_level() {
        if (levit_check()) {
            return;
        }
        if (Util.getPlace(Global.player._t_pos).p_ch != ObjectType.STAIRS) {
            IOUtil.msg("I see no way down");
        } else {
            Human.instance.upstairs();
            Global.seenstairs = false;
            New_Level.new_level();
        }
    }

    /*
     * u_level:
     *	He wants to go up a level
     */
    static void u_level() {
        if (levit_check()) {
            return;
        }
        if (Util.getPlace(Global.player._t_pos).p_ch == ObjectType.STAIRS) {
            if (Game.getInstance().isGoal()) {
                Human.instance.downstairs();
                if (Human.instance.getLevel() == 0) {
                    Rip.total_winner();
                }
                New_Level.new_level();
                IOUtil.msg("you feel a wrenching sensation in your gut");
            } else {
                IOUtil.msg("your way is magically blocked");
            }
        } else {
            IOUtil.msg("I see no way up");
        }
    }

    /*
     * levit_check:
     *	Check to see if she's levitating, and if she is, print an
     *	appropriate message.
     */
    static boolean levit_check() {
        if (!Human.instance.containsState(StateEnum.ISLEVIT)) {
            return false;
        }
        IOUtil.msg("You can't.  You're floating off the ground!");
        return true;
    }


}
