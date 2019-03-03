package org.hiro.input.keyboard;

import org.hiro.Chase;
import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Misc;
import org.hiro.Util;
import org.hiro.character.Human;
import org.hiro.character.Player;
import org.hiro.character.StateEnum;
import org.hiro.things.OriginalMonster;

public class FightingCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        int ch;
        if (!Misc.get_dir()) {
            Global.after = false;
            return;
        }
        Global.delta = Global.player._t_pos.add(Global.delta);
        OriginalMonster mp = Util.getPlace(Global.delta).p_monst;
        if (mp == null || (!Chase.see_monst(mp) && !Human.instance.containsState(StateEnum.SEEMONST))) {
            if (!Global.terse) {
                IOUtil.addmsg("I see ");
            }
            IOUtil.msg("no monster there");
            Global.after = false;
        } else if (Chase.diag_ok(Global.player._t_pos, Global.delta)) {
            Global.to_death = true;
            Global.max_hit = 0;
            mp.addState(StateEnum.ISTARGET);
            Global.runch = ch = Global.dir_ch;
            // goto over;
        }

    }
}
