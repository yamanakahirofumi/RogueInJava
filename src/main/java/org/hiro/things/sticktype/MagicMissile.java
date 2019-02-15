package org.hiro.things.sticktype;

import org.hiro.Const;
import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Monst;
import org.hiro.Util;
import org.hiro.WeaponMethod;
import org.hiro.character.Player;
import org.hiro.things.Magic;
import org.hiro.things.Stick;
import org.hiro.things.StickEnum;
import org.hiro.things.ThingImp;

public class MagicMissile extends Stick {
    public MagicMissile(){
        super();
    }

    @Override
    public void shake(Player player) {
        Global.ws_info[StickEnum.MagicMissile.getValue()].know();
        Magic bolt = new Magic();
        if (player.getWeapons().size() > 0) {
            bolt._o_launch = player.getWeapons().get(0)._o_which;
        }
        ThingImp tp = Util.getPlace(bolt._o_pos).p_monst;
        WeaponMethod.do_motion(bolt, Global.delta);
        if (tp != null && !Monst.save_throw(Const.VS_MAGIC, tp)) {
            WeaponMethod.hit_monster(bolt._o_pos, bolt);
        } else if (Global.terse) {
            IOUtil.msg("missile vanishes");
        } else {
            IOUtil.msg("the missile vanishes with a puff of smoke");
        }
        this.use();
    }
}
