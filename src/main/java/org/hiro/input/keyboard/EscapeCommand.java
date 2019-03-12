package org.hiro.input.keyboard;

import org.hiro.Global;
import org.hiro.character.Player;

public class EscapeCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        Global.door_stop = false;
        Global.count = 0;
        Global.after = false;
        Global.again = false;
    }
}
