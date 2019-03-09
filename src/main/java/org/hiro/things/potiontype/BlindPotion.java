package org.hiro.things.potiontype;

import org.hiro.Potions;
import org.hiro.character.Player;
import org.hiro.things.Potion;
import org.hiro.things.PotionEnum;

public class BlindPotion extends Potion {
    @Override
    public void quaff(Player player) {
        Potions.do_pot(PotionEnum.Blind, true);
    }
}
