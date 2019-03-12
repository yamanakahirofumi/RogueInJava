package org.hiro.things.potiontype;

import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Potions;
import org.hiro.character.Player;
import org.hiro.things.Potion;
import org.hiro.things.PotionEnum;

public class RaisePotion extends Potion {
    @Override
    public void quaff(Player player) {
        Global.pot_info[PotionEnum.P_RAISE.getValue()].know();
        IOUtil.msg("you suddenly feel much more skillful");
        Potions.raise_level();
    }
}
