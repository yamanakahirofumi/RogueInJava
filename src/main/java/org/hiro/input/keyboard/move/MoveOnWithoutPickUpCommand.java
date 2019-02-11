package org.hiro.input.keyboard.move;

import org.hiro.Global;
import org.hiro.Misc;
import org.hiro.input.keyboard.KeyboardCommand;

public class MoveOnWithoutPickUpCommand implements KeyboardCommand {

    @Override
    public void execute() {
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
