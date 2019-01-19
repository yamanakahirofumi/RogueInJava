package org.hiro.things.scrolltype;

import org.hiro.IOUtil;
import org.hiro.Init;
import org.hiro.character.Human;
import org.hiro.character.StateEnum;
import org.hiro.things.Scroll;

public class Confuse extends Scroll {
    public Confuse(){
        super();
    }

    @Override
    public void read() {
        /*
         * Scroll of monster confusion.  Give him that power.
         */
        Human.instance.addState(StateEnum.CANHUH);
        IOUtil.msg("your hands begin to glow %s", Init.pick_color("red"));

    }
}
