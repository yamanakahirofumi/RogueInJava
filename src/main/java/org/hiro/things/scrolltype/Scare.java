package org.hiro.things.scrolltype;

import org.hiro.IOUtil;
import org.hiro.character.Player;
import org.hiro.things.Scroll;

public class Scare extends Scroll {
    public Scare(){
        super();
    }

    @Override
    public void read(Player player) {
        /*
         * Reading it is a mistake and produces laughter at her
         * poor boo boo.
         */
        IOUtil.msg("you hear maniacal laughter in the distance");
    }
}
