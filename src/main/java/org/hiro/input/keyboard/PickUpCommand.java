package org.hiro.input.keyboard;

import org.hiro.Command;
import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Pack;
import org.hiro.character.Human;
import org.hiro.things.ThingImp;

public class PickUpCommand implements KeyboardCommand {

    @Override
    public void execute() {
        int found = 0;
        for (ThingImp obj : Global.lvl_obj) {
            if (Global.player._t_pos.equals(obj._o_pos)) {
                found = 1;
                break;
            }
        }

        if (found != 0) {
            if (Command.levit_check(Human.instance)) {
                ;
            } else {
                Pack.pick_up();
            }
        } else {
            if (!Global.terse) {
                IOUtil.addmsg("there is ");
            }
            IOUtil.addmsg("nothing here");
            if (!Global.terse) {
                IOUtil.addmsg(" to pick up");
            }
            IOUtil.endmsg();
        }

    }
}
