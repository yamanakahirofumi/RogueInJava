package org.hiro.input.keyboard;

import org.hiro.Command;
import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Pack;
import org.hiro.character.Player;
import org.hiro.things.Thing;

public class PickUpCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        int found = 0;
        for (Thing obj : Global.lvl_obj) {
            if (player.getPosition().equals(obj.getOPos())) {
                found = 1;
                break;
            }
        }

        if (found != 0) {
            if (Command.levit_check(player)) {
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
