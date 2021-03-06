package org.hiro;

import org.hiro.character.Human;
import org.hiro.character.Player;
import org.hiro.character.StateEnum;
import org.hiro.input.keyboard.ChangeWizardModeCommand;
import org.hiro.input.keyboard.DefaultCommand;
import org.hiro.input.keyboard.DiscoverCommand;
import org.hiro.input.keyboard.DropCommand;
import org.hiro.input.keyboard.EscapeCommand;
import org.hiro.input.keyboard.FightingCommand;
import org.hiro.input.keyboard.FightingEndlessCommand;
import org.hiro.input.keyboard.FireItemCommand;
import org.hiro.input.keyboard.HelpCommand;
import org.hiro.input.keyboard.IdentifyCommand;
import org.hiro.input.keyboard.IdentifyTrapTypeCommand;
import org.hiro.input.keyboard.IllegalCommand;
import org.hiro.input.keyboard.KeyboardCommand;
import org.hiro.input.keyboard.PickUpCommand;
import org.hiro.input.keyboard.QuitCommand;
import org.hiro.input.keyboard.RepeatCommand;
import org.hiro.input.keyboard.RestCommand;
import org.hiro.input.keyboard.SaveCommand;
import org.hiro.input.keyboard.SearchCommand;
import org.hiro.input.keyboard.SetOptionsCommand;
import org.hiro.input.keyboard.ShellCommand;
import org.hiro.input.keyboard.equipment.CallObjectCommand;
import org.hiro.input.keyboard.equipment.EatCommand;
import org.hiro.input.keyboard.equipment.PutOnRingCommand;
import org.hiro.input.keyboard.equipment.QuaffCommand;
import org.hiro.input.keyboard.equipment.ReadScrollCommand;
import org.hiro.input.keyboard.equipment.RemoveRingCommand;
import org.hiro.input.keyboard.equipment.TakeOfArmorCommand;
import org.hiro.input.keyboard.equipment.WearArmorCommand;
import org.hiro.input.keyboard.equipment.WieldWeaponCommand;
import org.hiro.input.keyboard.equipment.ZapWandCommand;
import org.hiro.input.keyboard.move.ContinuedMoveCommand;
import org.hiro.input.keyboard.move.DownCommand;
import org.hiro.input.keyboard.move.DownFloorCommand;
import org.hiro.input.keyboard.move.DownLeftCommand;
import org.hiro.input.keyboard.move.DownLeftRunCommand;
import org.hiro.input.keyboard.move.DownRightCommand;
import org.hiro.input.keyboard.move.DownRightRunCommand;
import org.hiro.input.keyboard.move.DownRunCommand;
import org.hiro.input.keyboard.move.LeftCommand;
import org.hiro.input.keyboard.move.LeftRunCommand;
import org.hiro.input.keyboard.move.MoveOnWithoutPickUpCommand;
import org.hiro.input.keyboard.move.RightCommand;
import org.hiro.input.keyboard.move.RightRunCommand;
import org.hiro.input.keyboard.move.UpCommand;
import org.hiro.input.keyboard.move.UpFloorCommand;
import org.hiro.input.keyboard.move.UpLeftCommand;
import org.hiro.input.keyboard.move.UpLeftRunCommand;
import org.hiro.input.keyboard.move.UpRightCommand;
import org.hiro.input.keyboard.move.UpRightRunCommand;
import org.hiro.input.keyboard.move.UpRunCommand;
import org.hiro.input.keyboard.print.PrintCurrentArmorCommand;
import org.hiro.input.keyboard.print.PrintCurrentRingsCommand;
import org.hiro.input.keyboard.print.PrintCurrentWeaponCommand;
import org.hiro.input.keyboard.print.PrintInventoryCommand;
import org.hiro.input.keyboard.print.PrintInventorySingleCommand;
import org.hiro.input.keyboard.print.PrintStatusCommand;
import org.hiro.input.keyboard.print.PrintVersionCommand;
import org.hiro.input.keyboard.print.RedrawScreenCommand;
import org.hiro.input.keyboard.print.RepeatLastMessageCommand;
import org.hiro.map.AbstractCoordinate;
import org.hiro.output.Display;
import org.hiro.things.ObjectType;
import org.hiro.things.Thing;
import org.hiro.things.ringtype.SearchingRing;
import org.hiro.things.ringtype.TeleportationRing;
import org.hiro.trap.TrapEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Command {

    private static final int CTRL = 037;

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
            IOUtil.status(Human.instance);
            Global.lastscore = Global.purse;
            Display.move(Human.instance.getPosition());
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
                Map<Integer, KeyboardCommand> commander = new HashMap<>();
                putCommands(commander);

                // overで繰り返したい
                if (commander.containsKey(ch)) {
                    commander.get(ch).execute(Human.instance);
                } else {
                    // デフォルト処理
                    DefaultCommand defaultCommand = new DefaultCommand();
                    defaultCommand.execute(Human.instance, ch);
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
        if (SearchingRing.isInclude(Human.instance.getRings())) {
            search(Human.instance);
        } else if (TeleportationRing.isInclude(Human.instance.getRings()) && Util.rnd(50) == 0) {
            Wizard.teleport(Human.instance);
        }
        // rightもね
    }

    private static void putCommands(Map<Integer, KeyboardCommand> commands) {
        commands.put((int) 'h', new LeftCommand());
        commands.put((int) 'l', new RightCommand());
        commands.put((int) 'k', new UpCommand());
        commands.put((int) 'j', new DownCommand());
        commands.put((int) 'y', new UpLeftCommand());
        commands.put((int) 'u', new UpRightCommand());
        commands.put((int) 'b', new DownLeftCommand());
        commands.put((int) 'n', new DownRightCommand());
        commands.put((int) 'H', new LeftRunCommand());
        commands.put((int) 'L', new RightRunCommand());
        commands.put((int) 'K', new UpRunCommand());
        commands.put((int) 'J', new DownRunCommand());
        commands.put((int) 'Y', new UpLeftRunCommand());
        commands.put((int) 'U', new UpRightRunCommand());
        commands.put((int) 'B', new DownLeftRunCommand());
        commands.put((int) 'N', new DownRightRunCommand());
        KeyboardCommand c = new ContinuedMoveCommand();
        commands.put('H' & CTRL, c);
        commands.put('J' & CTRL, c);
        commands.put('K' & CTRL, c);
        commands.put('L' & CTRL, c);
        commands.put('Y' & CTRL, c);
        commands.put('U' & CTRL, c);
        commands.put('B' & CTRL, c);
        commands.put('N' & CTRL, c);

        commands.put((int) 'm', new MoveOnWithoutPickUpCommand());
        commands.put((int) '>', new DownFloorCommand());
        commands.put((int) '<', new UpFloorCommand());
        commands.put((int) '!', new ShellCommand());
        commands.put((int) '?', new HelpCommand());
        commands.put((int) 'Q', new QuitCommand());
        commands.put((int) 'i', new PrintInventoryCommand());
        commands.put((int) 'I', new PrintInventorySingleCommand());
        commands.put((int) 'v', new PrintVersionCommand());
        commands.put((int) '@', new PrintStatusCommand());
        commands.put((int) '=', new PrintCurrentRingsCommand());
        commands.put((int) ']', new PrintCurrentArmorCommand());
        commands.put((int) ')', new PrintCurrentWeaponCommand());
        commands.put((int) 'R' & CTRL, new RedrawScreenCommand());
        commands.put((int) 'P' & CTRL, new RepeatLastMessageCommand());
        commands.put((int) 's', new SearchCommand());
        commands.put((int) 'D', new DiscoverCommand());
        commands.put((int) ',', new PickUpCommand());
        commands.put((int) 'f', new FightingCommand());
        commands.put((int) 'F', new FightingEndlessCommand());
        commands.put((int) '/', new IdentifyCommand());
        commands.put((int) '^', new IdentifyTrapTypeCommand());
        commands.put((int) ' ', new IllegalCommand());
        commands.put((int) 'a', new RepeatCommand());
        commands.put((int) 'o', new SetOptionsCommand());
        commands.put(Const.ESCAPE, new EscapeCommand());
        commands.put((int) '.', new RestCommand());
        commands.put((int) 'S', new SaveCommand());
        commands.put((int) 'c', new CallObjectCommand());
        commands.put((int) 'w', new WieldWeaponCommand());
        commands.put((int) 'W', new WearArmorCommand());
        commands.put((int) 'T', new TakeOfArmorCommand());
        commands.put((int) 'e', new EatCommand());
        commands.put((int) 't', new FireItemCommand());
        commands.put((int) 'z', new ZapWandCommand());
        commands.put((int) 'q', new QuaffCommand());
        commands.put((int) 'd', new DropCommand());
        commands.put((int) 'r', new ReadScrollCommand());
        commands.put((int) 'P', new PutOnRingCommand());
        commands.put((int) 'R', new RemoveRingCommand());

        commands.put((int) '+', new ChangeWizardModeCommand());
    }

    /*
     * search:
     *	player gropes about him to find hidden things.
     */
    public static void search(Player player) {

        int probinc = (player.containsState(StateEnum.ISHALU) ? 3 : 0);
        probinc += (player.containsState(StateEnum.ISBLIND) ? 2 : 0);
        boolean found = false;
        for (AbstractCoordinate c : player.getPosition().near()) {
            int fp = Util.flat(c);
            if ((fp & Const.F_REAL) == 0) {
                switch (Util.getPlace(c).p_ch) {
                    case Vert:
                    case Horizon:
                        if (Util.rnd(5 + probinc) != 0) {
                            break;
                        }
                        Util.getPlace(c).p_ch = ObjectType.DOOR;
                        IOUtil.msg("a secret door");
                        found = foundone(c);
                        break;
                    case FLOOR:
                        if (Util.rnd(2 + probinc) != 0) {
                            break;
                        }
                        Util.getPlace(c).p_ch = ObjectType.TRAP;
                        if (!Global.terse) {
                            IOUtil.addmsg("you found ");
                        }
                        if (player.containsState(StateEnum.ISHALU)) {
                            IOUtil.msg(Global.tr_name[Util.rnd(TrapEnum.count())]);
                        } else {
                            IOUtil.msg(Global.tr_name[fp & Const.F_TMASK]);
                            Util.getPlace(c).p_flags |= Const.F_SEEN;
                        }
                        found = foundone(c);
                        break;
                    case Blank:
                        if (Util.rnd(3 + probinc) != 0) {
                            break;
                        }
                        Util.getPlace(c).p_ch = ObjectType.PASSAGE;
                        found = foundone(c);
                }
            }
        }
        if (found) {
            Misc.look(false);
        }
    }

    static private boolean foundone(AbstractCoordinate coordinate) {
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
     * current:
     *	Print the current weapon/armor
     */
    public static void current(Thing cur, String how, String where) {
        Global.after = false;
        if (cur != null) {
            if (!Global.terse) {
                IOUtil.addmsg("you are %s (", how);
            }
            Global.inv_describe = false;
            IOUtil.addmsg("%c) %s", Human.instance.getPositionOfContent(cur), ThingMethod.inventoryName(cur, true));
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
     * levit_check:
     *	Check to see if she's levitating, and if she is, print an
     *	appropriate message.
     */
    public static boolean levit_check(Player player) {
        if (!player.containsState(StateEnum.ISLEVIT)) {
            return false;
        }
        IOUtil.msg("You can't.  You're floating off the ground!");
        return true;
    }


    /*
     * help:
     *	Give single character help, or the whole mess if he wants it
     */
    public static void help() {
        int helpch;
        int numprint;
        int cnt;
        IOUtil.msg("character you want help for (* for all): ");
        helpch = IOUtil.readchar();
        Global.mpos = 0;
        /*
         * If its not a *, print the right help string
         * or an error if he typed a funny character.
         */
        if (helpch != '*') {
            Display.move(0, 0);
            for (Help_list strp : Global.helpstr) {
                if (strp.h_ch == helpch) {
                    Global.lower_msg = true;
                    IOUtil.msg("%s%s", Display.unctrl(strp.h_ch), strp.h_desc);
                    Global.lower_msg = false;
                    return;
                }
            }
            IOUtil.msg("unknown character '%s'", Display.unctrl(helpch));
            return;
        }
        /*
         * Here we print help for everything.
         * Then wait before we return to command mode
         */
        numprint = 0;
        for (Help_list strp : Global.helpstr) {
            if (strp.h_print) {
                numprint++;
            }
        }
        if ((numprint & 01) != 0) {        /* round odd numbers up */
            numprint++;
        }
        numprint /= 2;
        if (numprint > Display.LINES - 1) {
            numprint = Display.LINES - 1;
        }
        // wclear(hw);
        cnt = 0;
        for (Help_list strp : Global.helpstr) {
            if (strp.h_print) {
                // wmove(hw, cnt % numprint, cnt >= numprint ? COLS / 2 : 0);
                if (strp.h_ch != 0) {
                    // waddstr(hw, Display.unctrl(strp . h_ch));
                }
                // waddstr(hw, strp . h_desc);
                if (++cnt >= numprint * 2) {
                    break;
                }
            }
        }
        // wmove(hw, LINES - 1, 0);
        // waddstr(hw, "--Press space to continue--");
        // wrefresh(hw);
        // wait_for(hw, ' ');
        // clearok(stdscr, TRUE);
/*
    refresh();
*/
        IOUtil.msg("");
        // touchwin(stdscr);
        // wrefresh(stdscr);
    }


}
