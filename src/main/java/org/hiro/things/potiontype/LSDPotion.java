package org.hiro.things.potiontype;

import org.hiro.Const;
import org.hiro.Daemon;
import org.hiro.Daemons;
import org.hiro.Global;
import org.hiro.Potions;
import org.hiro.character.Player;
import org.hiro.character.StateEnum;
import org.hiro.things.Potion;
import org.hiro.things.PotionEnum;

import java.lang.reflect.Method;

public class LSDPotion extends Potion {
    @Override
    public void quaff(Player player) {
        boolean trip = player.containsState(StateEnum.ISHALU);
        if (!trip) {
            if (player.containsState(StateEnum.SEEMONST)) {
                Potions.turn_see(false);
            }
            try {
                Method m = Daemons.class.getMethod("visuals", Player.class);
                Daemon.start_daemon(m, 0, Const.BEFORE);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            Global.seenstairs = Potions.seen_stairs();
        }
        Potions.do_pot(PotionEnum.LSD, true);
    }
}
