package org.hiro.things.scrolltype;

import org.hiro.Const;
import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Wizard;
import org.hiro.character.Player;
import org.hiro.things.Scroll;
import org.hiro.things.ScrollEnum;

public class IdentifyRingOrStick extends Scroll {
    public IdentifyRingOrStick(){
        super();
    }

    @Override
    public void read(Player player) {
        Global.scr_info[ScrollEnum.IdentifyRingOrStick.getValue()].know();
        IOUtil.msg("this scroll is an %s scroll", Global.scr_info[ScrollEnum.IdentifyRingOrStick.getValue()].getName());
        Wizard.whatis(true, Const.R_OR_S);

    }
}
