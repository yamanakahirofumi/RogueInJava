package org.hiro.things.potiontype;

import org.hiro.Potions;
import org.hiro.character.Player;
import org.hiro.things.Potion;
import org.hiro.things.PotionEnum;

public class LevitPotion extends Potion {
    @Override
    public void quaff(Player player) {
        Potions.do_pot(PotionEnum.P_LEVIT, true);
    }
}
