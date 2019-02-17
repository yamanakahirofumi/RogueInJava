package org.hiro.things.scrolltype;

import org.hiro.Const;
import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Init;
import org.hiro.Util;
import org.hiro.character.Player;
import org.hiro.things.Scroll;
import org.hiro.things.Weapon;

public class EnchantWeapon extends Scroll {
    public EnchantWeapon() {
        super();
    }

    @Override
    public void read(Player player) {
        if (player.getWeapons().size() < 1 || !(player.getWeapons().get(0) instanceof Weapon)) {
            IOUtil.msg("you feel a strange sense of loss");
        } else {
            Weapon w = player.getWeapons().get(0);
            w.delete_o_flags(Const.ISCURSED);
            if (Util.rnd(2) == 0) {
                w.addHitPlus();
            } else {
                w.addDamagePlus();
            }
            IOUtil.msg("your %s glows %s for a moment",
                    Global.weap_info[w._o_which].getName(), Init.pick_color("blue"));
        }
    }
}
