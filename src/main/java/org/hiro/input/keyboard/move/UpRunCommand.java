package org.hiro.input.keyboard.move;

import org.hiro.Move;
import org.hiro.character.Player;
import org.hiro.input.keyboard.KeyboardCommand;

public class UpRunCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        Move.do_run('k');
    }
}
