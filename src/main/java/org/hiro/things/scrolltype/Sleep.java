package org.hiro.things.scrolltype;

import org.hiro.Const;
import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Util;
import org.hiro.character.Human;
import org.hiro.character.StateEnum;
import org.hiro.things.Scroll;
import org.hiro.things.ScrollEnum;

public class Sleep extends Scroll {
    public Sleep(){
        super();
    }

    @Override
    public void read() {
        /*
         * Scroll which makes you fall asleep
         */
        Global.scr_info[ScrollEnum.Sleep.getValue()].know();
        Global.no_command += Util.rnd(Const.SLEEPTIME) + 4;
        Human.instance.removeState(StateEnum.ISRUN);
        IOUtil.msg("you fall asleep");

    }
}
