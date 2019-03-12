package org.hiro.input.keyboard;

import org.hiro.Global;
import org.hiro.character.Player;

public class DiscoverCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        Global.after = false;
        // discovered(); // TODO: 後で
    }
}
