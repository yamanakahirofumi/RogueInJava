package org.hiro.input.keyboard.print;

import org.hiro.Command;
import org.hiro.Const;
import org.hiro.Global;
import org.hiro.character.Player;
import org.hiro.input.keyboard.KeyboardCommand;

public class PrintCurrentRingsCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        Command.current(Global.cur_ring[Const.LEFT], "wearing",
                Global.terse ? "(L)" : "on left hand");
        Command.current(Global.cur_ring[Const.RIGHT], "wearing",
                Global.terse ? "(R)" : "on right hand");

    }
}
