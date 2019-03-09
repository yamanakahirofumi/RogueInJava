package org.hiro.things.potiontype;

import org.hiro.Const;
import org.hiro.Daemon;
import org.hiro.IOUtil;
import org.hiro.Misc;
import org.hiro.Potions;
import org.hiro.character.Player;
import org.hiro.character.StateEnum;
import org.hiro.things.Potion;

import java.lang.reflect.Method;

public class MonsterFindPotion extends Potion {

    @Override
    public void quaff(Player player) {
        player.addState(StateEnum.SEEMONST);
        try {
            Method m = Potions.class.getMethod("turn_see_off");
            Daemon.fuse(m, 1, Const.HUHDURATION, Const.AFTER);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        if (Potions.turn_see(false) == 0) {
            IOUtil.msg("you have a %s feeling for a moment, then it passes",
                    Misc.choose_str("normal", "strange"));
        }
    }
}
