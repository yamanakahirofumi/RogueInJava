package org.hiro.input.keyboard;

import org.hiro.Global;
import org.hiro.character.Player;

public class IllegalCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        /* "Legal" illegal command */
        Global.after = false;
    }
}
