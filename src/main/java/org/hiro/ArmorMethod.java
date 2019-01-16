package org.hiro;

import org.hiro.things.Armor;
import org.hiro.things.ObjectType;
import org.hiro.things.Thing;

public class ArmorMethod {

    /*
     * wear:
     *	The player wants to wear something, so let him/her put it on.
     */
    static void wear() {
        Thing obj;
        if ((obj = Pack.get_item("wear", ObjectType.ARMOR)) == null) {
            return;
        }
        if (Global.cur_armor != null) {
            IOUtil.addmsg("you are already wearing some");
            if (!Global.terse) {
                IOUtil.addmsg(".  You'll have to take it off first");
            }
            IOUtil.endmsg();
            Global.after = false;
            return;
        }
        if (!(obj instanceof Armor)) {
            IOUtil.msg("you can't wear that");
            return;
        }

        Armor armor = (Armor) obj;
        waste_time();
        obj.add_o_flags(Const.ISKNOW);
        String sp = ThingMethod.inv_name(armor, true);
        Global.cur_armor = armor;
        if (!Global.terse) {
            IOUtil.addmsg("you are now ");
        }
        IOUtil.msg("wearing %s", sp);
    }

    /*
     * waste_time:
     *	Do nothing but let other things happen
     */
    static void waste_time() {
        Daemon.do_daemons(Const.BEFORE);
        Daemon.do_fuses(Const.BEFORE);
        Daemon.do_daemons(Const.AFTER);
        Daemon.do_fuses(Const.AFTER);
    }

    /*
     * take_off:
     *	Get the armor off of the players back
     */
    static void take_off() {
        Armor obj;

        if ((obj = Global.cur_armor) == null) {
            Global.after = false;
            if (Global.terse) {
                IOUtil.msg("not wearing armor");
            } else {
                IOUtil.msg("you aren't wearing any armor");
            }
            return;
        }
        if (!ThingMethod.dropcheck(Global.cur_armor)) {
            return;
        }
        Global.cur_armor = null;
        if (Global.terse) {
            IOUtil.addmsg("was");
        } else {
            IOUtil.addmsg("you used to be");
        }
        IOUtil.msg(" wearing %c) %s", String.valueOf(obj._o_packch), ThingMethod.inv_name(obj, true));
    }


}
