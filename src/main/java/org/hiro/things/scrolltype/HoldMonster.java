package org.hiro.things.scrolltype;

import org.hiro.Const;
import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Util;
import org.hiro.character.Player;
import org.hiro.character.StateEnum;
import org.hiro.things.OriginalMonster;
import org.hiro.things.Scroll;
import org.hiro.things.ScrollEnum;

public class HoldMonster extends Scroll {
    public HoldMonster(){
        super();
    }

    @Override
    public void read(Player player) {
        /*
         * Hold monster scroll.  Stop all monsters within two spaces
         * from chasing after the hero.
         */

        int ch = 0;
        for (int x = Global.player._t_pos.x - 2; x <= Global.player._t_pos.x + 2; x++) {
            if (x >= 0 && x < Const.NUMCOLS) {
                for (int y = Global.player._t_pos.y - 2; y <= Global.player._t_pos.y + 2; y++) {
                    if (y >= 0 && y <= Const.NUMLINES - 1) {
                        OriginalMonster obj2 = Util.INDEX(y, x).p_monst;
                        if (obj2  != null && obj2.containsState(StateEnum.ISRUN)) {
                            obj2.removeState(StateEnum.ISRUN);
                            obj2.addState(StateEnum.ISHELD);
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
            Global.scr_info[ScrollEnum.HoldMonster.getValue()].know();
        } else
            IOUtil.msg("you feel a strange sense of loss");

    }
}
