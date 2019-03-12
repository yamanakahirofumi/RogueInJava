package org.hiro.input.keyboard;

import org.hiro.Global;
import org.hiro.character.Player;

public class SaveCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        Global.after = false;
        // save_game(); // TODO: あとで
    }
}
