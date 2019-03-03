package org.hiro.input.keyboard;

import org.hiro.Command;
import org.hiro.Global;
import org.hiro.character.Player;

public class HelpCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        Global.after = false;
        Command.help();
    }
}
