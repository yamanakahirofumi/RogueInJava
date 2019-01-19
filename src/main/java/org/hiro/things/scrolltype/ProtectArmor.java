package org.hiro.things.scrolltype;

import org.hiro.Const;
import org.hiro.IOUtil;
import org.hiro.Init;
import org.hiro.character.Human;
import org.hiro.things.Scroll;

public class ProtectArmor extends Scroll {
    public ProtectArmor() {
        super();
    }

    @Override
    public void read() {
        if (Human.instance.isEquippedArmor()) {
            Human.instance.getArmor().add_o_flags(Const.ISPROT);
            IOUtil.msg("your armor is covered by a shimmering %s shield",
                    Init.pick_color("gold"));
        } else {
            IOUtil.msg("you feel a strange sense of loss");
        }
    }
}
