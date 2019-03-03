package org.hiro.input.keyboard;

import org.hiro.Global;
import org.hiro.character.Player;

public class IdentifyCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        Global.after = false;
        // identify();  // TODO: 後で
    }
}
