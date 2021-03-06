package org.hiro.things.scrolltype;

import org.hiro.IOUtil;
import org.hiro.Init;
import org.hiro.character.Player;
import org.hiro.things.Scroll;

public class EnchantArmor extends Scroll {
    public EnchantArmor(){
        super();
    }

    @Override
    public void read(Player player) {
        if (player.isEquippedArmor()) {
            player.getArmor().enchant();
            IOUtil.msg("your armor glows %s for a moment", Init.pick_color("silver"));
        }
    }
}
