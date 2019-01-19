package org.hiro.things.scrolltype;

import org.hiro.IOUtil;
import org.hiro.things.Scroll;

public class Scare extends Scroll {
    public Scare(){
        super();
    }

    @Override
    public void read() {
        /*
         * Reading it is a mistake and produces laughter at her
         * poor boo boo.
         */
        IOUtil.msg("you hear maniacal laughter in the distance");
    }
}
