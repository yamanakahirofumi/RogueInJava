package org.hiro.input.keyboard.move;

import org.hiro.Global;
import org.hiro.Misc;
import org.hiro.character.Player;
import org.hiro.input.keyboard.KeyboardCommand;

public class MoveOnWithoutPickUpCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        int ch;
        int countch;
        Global.move_on = true;
        if (!Misc.get_dir()) {
            Global.after = false;
        } else {
            ch = Global.dir_ch;
            countch = Global.dir_ch;
            // goto over;
        }
    }
}
