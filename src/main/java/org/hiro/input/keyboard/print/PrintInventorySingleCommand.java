package org.hiro.input.keyboard.print;

import org.hiro.Global;
import org.hiro.Pack;
import org.hiro.character.Player;
import org.hiro.input.keyboard.KeyboardCommand;

public class PrintInventorySingleCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        Global.after = false;
        Pack.picky_inven();
    }
}
