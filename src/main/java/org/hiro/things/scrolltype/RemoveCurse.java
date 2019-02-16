package org.hiro.things.scrolltype;

import org.hiro.Const;
import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Misc;
import org.hiro.character.Player;
import org.hiro.things.Scroll;
import org.hiro.things.Thing;

public class RemoveCurse extends Scroll {
    public RemoveCurse(){
        super();
    }

    @Override
    public void read(Player player) {
        uncurse(player.getArmor());
        uncurse(player.getWeapons().size() > 0 ? player.getWeapons().get(0) : null);
        uncurse(Global.cur_ring[Const.LEFT]);
        uncurse(Global.cur_ring[Const.RIGHT]);
        IOUtil.msg(Misc.choose_str("you feel in touch with the Universal Onenes",
                "you feel as if somebody is watching over you"));
    }

    /*
     * uncurse:
     *	Uncurse an item
     */
    private void uncurse(Thing obj) {
        if (obj != null)
            obj.delete_o_flags(Const.ISCURSED);
    }

}
