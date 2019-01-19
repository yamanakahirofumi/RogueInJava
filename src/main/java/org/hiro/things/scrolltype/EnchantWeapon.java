package org.hiro.things.scrolltype;

import org.hiro.Const;
import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Init;
import org.hiro.Util;
import org.hiro.character.Human;
import org.hiro.things.Scroll;
import org.hiro.things.Weapon;

public class EnchantWeapon extends Scroll {
    public EnchantWeapon() {
        super();
    }

    @Override
    public void read() {
        if (Human.instance.getWeapons().size() < 1 || Human.instance.getWeapons().get(0) instanceof Weapon) {
            IOUtil.msg("you feel a strange sense of loss");
        } else {
            Weapon w = Human.instance.getWeapons().get(0);
            w.delete_o_flags(Const.ISCURSED);
            if (Util.rnd(2) == 0) {
                w._o_hplus++;
            } else {
                w._o_dplus++;
            }
            IOUtil.msg("your %s glows %s for a moment",
                    Global.weap_info[w._o_which].getName(), Init.pick_color("blue"));
        }
    }
}
