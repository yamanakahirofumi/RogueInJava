package org.hiro;

import org.hiro.character.Human;
import org.hiro.things.ObjectType;
import org.hiro.things.Scroll;
import org.hiro.things.Thing;

public class ScrollMethod {
    /*
     * read_scroll:
     *	Read a scroll from the pack and do the appropriate thing
     */
    public static void read_scroll() {
        boolean MASTER = false;

        Thing obj = Pack.get_item("read", ObjectType.SCROLL);
        if (obj == null)
            return;
        if (!(obj instanceof Scroll)) {
            if (!Global.terse) {
                IOUtil.msg("there is nothing on it to read");
            } else {
                IOUtil.msg("nothing to read");
            }
            return;
        }
        Scroll scroll = (Scroll) obj;
        /*
         * Calculate the effect it has on the poor guy.
         */
        Human.instance.removeWeapon(scroll);
        /*
         * Get rid of the thing
         */
        Pack.leave_pack(scroll, false, false);
        scroll.read();
        Misc.look(true);    /* put the result of the scroll on the screen */
        IOUtil.status();


    }

}
