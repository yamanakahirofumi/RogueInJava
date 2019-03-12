package org.hiro.things.potiontype;

import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Misc;
import org.hiro.character.Player;
import org.hiro.things.Potion;
import org.hiro.things.PotionEnum;

public class StrengthPotion extends Potion {

    @Override
    public void quaff(Player player) {
        Global.pot_info[PotionEnum.Strength.getValue()].know();
        Misc.chg_str(1);
        IOUtil.msg("you feel stronger, now.  What bulging muscles!");
    }
}
