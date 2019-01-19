package org.hiro.things.scrolltype;

import org.hiro.Const;
import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Init;
import org.hiro.things.Scroll;

public class EnchantArmor extends Scroll {
    public EnchantArmor(){
        super();
    }

    @Override
    public void read() {
        if (Global.cur_armor != null) {
            Global.cur_armor._o_arm--;
            Global.cur_armor.delete_o_flags(Const.ISCURSED);
            IOUtil.msg("your armor glows %s for a moment", Init.pick_color("silver"));
        }
    }
}
