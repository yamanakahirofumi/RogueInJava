package org.hiro.input.keyboard.move;

import org.hiro.Move;
import org.hiro.character.Player;
import org.hiro.input.keyboard.KeyboardCommand;

public class DownCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        Move.do_move(player, 1, 0);
    }
}
