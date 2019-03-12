package org.hiro.input.keyboard;

import org.hiro.Const;
import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Misc;
import org.hiro.TrapEnum;
import org.hiro.Util;
import org.hiro.character.Player;
import org.hiro.character.StateEnum;
import org.hiro.things.ObjectType;

public class IdentifyTrapTypeCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        Global.after = false;
        if (Misc.get_dir()) {
            Global.delta = player.getPosition().add(Global.delta);
            int fp = Util.flat(Global.delta);
            if (!Global.terse) {
                IOUtil.addmsg("You have found ");
            }
            if (Util.getPlace(Global.delta).p_ch != ObjectType.TRAP) {
                IOUtil.msg("no trap there");
            } else if (player.containsState(StateEnum.ISHALU)) {
                IOUtil.msg(Global.tr_name[Util.rnd(TrapEnum.count())]);
            } else {
                IOUtil.msg(Global.tr_name[fp & Const.F_TMASK]);
                fp |= Const.F_SEEN;
            }
        }
    }
}
