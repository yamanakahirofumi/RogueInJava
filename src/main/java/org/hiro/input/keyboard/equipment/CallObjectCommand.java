package org.hiro.input.keyboard.equipment;

import org.hiro.Global;
import org.hiro.character.Player;
import org.hiro.input.keyboard.KeyboardCommand;

public class CallObjectCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        // call();  // TODO: 後で
        Global.after = false;
    }
}
