package org.hiro.things.potiontype;

import org.hiro.Daemons;
import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Misc;
import org.hiro.Util;
import org.hiro.character.Player;
import org.hiro.things.Potion;
import org.hiro.things.PotionEnum;
import org.hiro.things.ringtype.SustainStrengthRing;

public class PoisonPotion extends Potion {

    @Override
    public void quaff(Player player){
        Global.pot_info[PotionEnum.Poison.getValue()].know();
        if (SustainStrengthRing.isInclude(player.getRings())) {
            IOUtil.msg("you feel momentarily sick");
        } else {
            Misc.chg_str(-(Util.rnd(3) + 1));
            IOUtil.msg("you feel very sick now");
            Daemons.come_down(player);
        }

    }
}
