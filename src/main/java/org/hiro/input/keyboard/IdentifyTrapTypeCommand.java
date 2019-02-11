package org.hiro.input.keyboard;

import org.hiro.Const;
import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Misc;
import org.hiro.Util;
import org.hiro.character.Human;
import org.hiro.character.StateEnum;
import org.hiro.things.ObjectType;

public class IdentifyTrapTypeCommand implements KeyboardCommand {

    @Override
    public void execute() {
        Global.after = false;
        if (Misc.get_dir()) {
            Global.delta = Global.player._t_pos.add(Global.delta);
            int fp = Util.flat(Global.delta);
            if (!Global.terse) {
                IOUtil.addmsg("You have found ");
            }
            if (Util.getPlace(Global.delta).p_ch != ObjectType.TRAP) {
                IOUtil.msg("no trap there");
            } else if (Human.instance.containsState(StateEnum.ISHALU)) {
                IOUtil.msg(Global.tr_name[Util.rnd(Const.NTRAPS)]);
            } else {
                IOUtil.msg(Global.tr_name[fp & Const.F_TMASK]);
                fp |= Const.F_SEEN;
            }
        }
    }
}
