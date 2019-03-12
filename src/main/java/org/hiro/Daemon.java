package org.hiro;

import org.hiro.character.Human;
import org.hiro.character.StateEnum;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Contains functions for dealing with things that happen in the
 * future.
 *
 */
public class Daemon {

    static final private int DAEMON = -1;

    /*
     * lengthen:
     *	Increase the time until a fuse goes off
     */
    static void lengthen(Method func, int xtime) {
        // funcは関数
        Delayed_action wire = find_slot(func);

        if (wire == null) {
            return;
        }
        wire.d_time += xtime;
    }

    /*
     * find_slot:
     *	Find a particular slot in the table
     */
    static Delayed_action find_slot(Method func) {
        for (int i = 0; i < Global.d_list.length; i++) {
            Delayed_action dev = Global.d_list[i];
            if (dev.d_type != Const.EMPTY && func == dev.d_func) {
                return dev;
            }
        }
        return null;
    }

    /*
     * unconfuse:
     *	Release the poor player from his confusion
     */
    static void unconfuse() {
        Human.instance.removeState(StateEnum.ISHUH);
        IOUtil.msg("you feel less %s now", Misc.choose_str("trippy", "confused"));
    }

    /*
     * fuse:
     *	Start a fuse to go off in a certain number of turns
     */
    public static void fuse(Method func, int arg, int time, int type) {
        Delayed_action wire;

        wire = d_slot();
        wire.d_type = type;
        wire.d_func = func;
        wire.d_arg = arg;
        wire.d_time = time;
    }


    /*
     * d_slot:
     *	Find an empty slot in the daemon/fuse list
     */
    static Delayed_action d_slot() {
        for (int i = 0; i < Global.d_list.length; i++) {
            Delayed_action dev = Global.d_list[i];
            if (dev.d_type == Const.EMPTY) {
                return dev;
            }
        }

        boolean MASTER = false;
        if (MASTER) {
            // debug("Ran out of fuse slots");
        }
        return null;
    }

    /*
     * do_daemons:
     *	Run all the daemons that are active with the current flag,
     *	passing the argument to the function.
     */
    static void do_daemons(int flag) {

        /*
         * Loop through the devil list
         */
        for (int i = 0; i < Const.MAXDAEMONS; i++) {
            Delayed_action dev = Global.d_list[i];
            /*
             * Executing each one, giving it the proper arguments
             */
            if (dev.d_type == flag && dev.d_time == DAEMON) {
                try {
                    dev.d_func.invoke(dev.d_arg);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
     * do_fuses:
     *	Decrement counters and start needed fuses
     */
    static void do_fuses(int flag) {

        /*
         * Step though the list
         */
        for (int i = 0; i < Const.MAXDAEMONS; i++) {
            Delayed_action wire = Global.d_list[i];
            /*
             * Decrementing counters and starting things we want.  We also need
             * to remove the fuse from the list once it has gone off.
             */
            if (flag == wire.d_type && wire.d_time > 0 && --wire.d_time == 0) {
                wire.d_type = Const.EMPTY;
                try {
                    wire.d_func.invoke(wire.d_arg);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
     * extinguish:
     *	Put out a fuse
     */
    static void extinguish(Method func) {
        Delayed_action wire;

        if ((wire = find_slot(func)) == null) {
            return;
        }
        wire.d_type = Const.EMPTY;
    }

    /*
     * kill_daemon:
     *	Remove a daemon from the list
     */
    static void kill_daemon(Method func)
    {
        Delayed_action dev;

        if ((dev = find_slot(func)) == null) {
            return;
        }
        /*
         * Take it out of the list
         */
        dev.d_type = Const.EMPTY;
    }

    /*
     * start_daemon:
     *	Start a daemon, takes a function.
     */
    public static void start_daemon(Method func, int arg, int type)
    {
        Delayed_action dev;

        dev = d_slot();
        dev.d_type = type;
        dev.d_func = func;
        dev.d_arg = arg;
        dev.d_time = DAEMON;
    }


}
