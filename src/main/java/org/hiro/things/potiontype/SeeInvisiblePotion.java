package org.hiro.things.potiontype;

import org.hiro.Daemons;
import org.hiro.Global;
import org.hiro.Potions;
import org.hiro.character.Player;
import org.hiro.character.StateEnum;
import org.hiro.things.Potion;
import org.hiro.things.PotionEnum;

public class SeeInvisiblePotion extends Potion {
    @Override
    public void quaff(Player player) {
        Global.prbuf = "this potion tastes like " + Global.fruit + " juice";
        boolean show = player.containsState(StateEnum.CANSEE);
        Potions.do_pot(PotionEnum.SeeInvisible, false);
        if (!show) {
            Potions.invis_on();
        }
        Daemons.sight();

    }
}
