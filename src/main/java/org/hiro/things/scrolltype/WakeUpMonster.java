package org.hiro.things.scrolltype;

import org.hiro.IOUtil;
import org.hiro.Misc;
import org.hiro.character.Player;
import org.hiro.things.Scroll;

public class WakeUpMonster extends Scroll {
    public WakeUpMonster(){
        super();
    }

    @Override
    public void read(Player player) {
        /*
         * This scroll aggravates all the monsters on the current
         * level and sets them running towards the hero
         */
        Misc.aggravate();
        IOUtil.msg("you hear a high pitched humming noise");
    }
}
