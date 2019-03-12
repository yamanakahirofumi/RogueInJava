package org.hiro.things.scrolltype;

import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Wizard;
import org.hiro.character.Player;
import org.hiro.things.ObjectType;
import org.hiro.things.Scroll;
import org.hiro.things.ScrollEnum;

public class IdentifyWeapon extends Scroll {
    public IdentifyWeapon() {
        super();
    }

    @Override
    public void read(Player player) {
        Global.scr_info[ScrollEnum.IdentifyWeapon.getValue()].know();
        IOUtil.msg("this scroll is an %s scroll", Global.scr_info[ScrollEnum.IdentifyWeapon.getValue()].getName());
        Wizard.whatIs(player, true, ObjectType.WEAPON.getValue());

    }
}
