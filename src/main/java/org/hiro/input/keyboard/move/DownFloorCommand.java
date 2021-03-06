package org.hiro.input.keyboard.move;

import org.hiro.Command;
import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.New_Level;
import org.hiro.Util;
import org.hiro.character.Player;
import org.hiro.input.keyboard.KeyboardCommand;
import org.hiro.things.ObjectType;

public class DownFloorCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        Global.after = false;
        if (Command.levit_check(player)) {
            return;
        }
        if (Util.getPlace(player.getPosition()).p_ch != ObjectType.STAIRS) {
            IOUtil.msg("I see no way down");
        } else {
            player.upstairs();
            Global.seenstairs = false;
            New_Level.new_level(player);
        }
    }
}
