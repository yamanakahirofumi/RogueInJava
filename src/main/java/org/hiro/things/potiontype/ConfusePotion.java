package org.hiro.things.potiontype;

import org.hiro.Potions;
import org.hiro.character.Player;
import org.hiro.character.StateEnum;
import org.hiro.things.Potion;
import org.hiro.things.PotionEnum;

public class ConfusePotion extends Potion {
    public ConfusePotion(){
        super();
    }

    @Override
    public void quaff(Player player){
        Potions.do_pot(PotionEnum.Confuse, !player.containsState(StateEnum.ISHALU));
    }
}
