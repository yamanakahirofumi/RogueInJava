package org.hiro.things.potiontype;

import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Misc;
import org.hiro.character.Player;
import org.hiro.things.Potion;
import org.hiro.things.PotionEnum;

public class HastePotion extends Potion {
    @Override
    public void quaff(Player player) {
    Global.pot_info[PotionEnum.P_HASTE.getValue()].know();
        Global.after = false;
        if (Misc.add_haste(true))
            IOUtil.msg("you feel yourself moving much faster");
    }
}