package org.hiro.input.keyboard;

import org.hiro.Command;
import org.hiro.character.Player;

public class SearchCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        Command.search(player);
    }
}
