package org.hiro.input.keyboard.move;

import org.hiro.Move;
import org.hiro.input.keyboard.KeyboardCommand;

public class RightCommand implements KeyboardCommand {

    @Override
    public void execute() {
        Move.do_move(0, 1);
    }
}
