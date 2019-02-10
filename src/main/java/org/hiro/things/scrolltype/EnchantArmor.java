package org.hiro.things.scrolltype;

import org.hiro.Const;
import org.hiro.IOUtil;
import org.hiro.Init;
import org.hiro.character.Human;
import org.hiro.things.Scroll;

public class EnchantArmor extends Scroll {
    public EnchantArmor(){
        super();
    }

    @Override
    public void read() {
        if (Human.instance.isEquippedArmor()) {
            Human.instance.getArmor()._o_arm--;
            Human.instance.getArmor().delete_o_flags(Const.ISCURSED);
            IOUtil.msg("your armor glows %s for a moment", Init.pick_color("silver"));
        }
    }
}