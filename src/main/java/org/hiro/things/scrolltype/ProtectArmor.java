package org.hiro.things.scrolltype;

import org.hiro.Const;
import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Init;
import org.hiro.things.Scroll;

public class ProtectArmor extends Scroll {
    public ProtectArmor() {
        super();
    }

    @Override
    public void read() {
        if (Global.cur_armor != null) {
            Global.cur_armor.add_o_flags(Const.ISPROT);
            IOUtil.msg("your armor is covered by a shimmering %s shield",
                    Init.pick_color("gold"));
        } else {
            IOUtil.msg("you feel a strange sense of loss");
        }
    }
}
