package org.hiro.input.keyboard;

import org.hiro.Global;
import org.hiro.Options;
import org.hiro.character.Player;

public class SetOptionsCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        Options.option();
        Global.after = false;
    }
}
