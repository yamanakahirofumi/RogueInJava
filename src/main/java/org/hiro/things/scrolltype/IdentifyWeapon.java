package org.hiro.things.scrolltype;

import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Wizard;
import org.hiro.things.ObjectType;
import org.hiro.things.Scroll;
import org.hiro.things.ScrollEnum;

public class IdentifyWeapon extends Scroll {
    public IdentifyWeapon(){
        super();
    }

    @Override
    public void read() {
        Global.scr_info[ScrollEnum.IdentifyWeapon.getValue()].know();
        IOUtil.msg("this scroll is an %s scroll", Global.scr_info[ScrollEnum.IdentifyWeapon.getValue()].getName());
        Wizard.whatis(true, ObjectType.WEAPON.getValue());

    }
}
