package org.hiro.things.potiontype;

import org.hiro.Const;
import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Misc;
import org.hiro.character.Player;
import org.hiro.things.Potion;
import org.hiro.things.ringtype.AddStrengthRing;

public class RestorePotion extends Potion {
    @Override
    public void quaff(Player player) {
        if (Global.cur_ring[Const.LEFT] instanceof AddStrengthRing) {
            Global.player.getStatus().s_str = Misc.add_str(player.getCurrentStrength(), -((AddStrengthRing) Global.cur_ring[Const.LEFT]).getStrength());
        }
        if (Global.cur_ring[Const.RIGHT] instanceof AddStrengthRing) {
            Global.player.getStatus().s_str = Misc.add_str(player.getCurrentStrength(), -((AddStrengthRing) Global.cur_ring[Const.RIGHT]).getStrength());
        }
        if (player.getCurrentStrength() < player.getMaxStrength()) {
            Global.player.getStatus().s_str = player.getMaxStrength();
        }
        if (Global.cur_ring[Const.LEFT] instanceof AddStrengthRing) {
            Global.player.getStatus().s_str = Misc.add_str(player.getCurrentStrength(), ((AddStrengthRing) Global.cur_ring[Const.LEFT]).getStrength());
        }
        if (Global.cur_ring[Const.RIGHT] instanceof AddStrengthRing) {
            Global.player.getStatus().s_str = Misc.add_str(player.getCurrentStrength(), ((AddStrengthRing) Global.cur_ring[Const.RIGHT]).getStrength());
        }
        IOUtil.msg("hey, this tastes great.  It make you feel warm all over");

    }
}
