package org.hiro.input.keyboard;

import org.hiro.Global;
import org.hiro.Main2;
import org.hiro.character.Player;

public class QuitCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        Global.after = false;
        Global.q_comm = true;
        Main2.quit(player,0);
        Global.q_comm = false;
    }
}
