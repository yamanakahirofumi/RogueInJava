package org.hiro.things.potiontype;

import org.hiro.Daemons;
import org.hiro.Dice;
import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.character.Player;
import org.hiro.things.Potion;
import org.hiro.things.PotionEnum;

public class XHealingPotion extends Potion {

    @Override
    public void quaff(Player player) {
        Global.pot_info[PotionEnum.P_XHEAL.getValue()].know();
        player.addHp(Dice.roll(Global.player.getStatus().s_lvl, 8));
        if (player.getHp() > player.getMaxHp()) {
            if (player.getHp() > player.getMaxHp() + Global.player.getStatus().s_lvl + 1) {
                ++Global.player.getStatus().s_maxhp;
            }
            Global.player.getStatus().s_hpt = ++Global.player.getStatus().s_maxhp;
        }
        Daemons.sight();
        Daemons.come_down(player);
        IOUtil.msg("you begin to feel much better");
    }
}
