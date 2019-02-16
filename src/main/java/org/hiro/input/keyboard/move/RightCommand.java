package org.hiro.input.keyboard.move;

import org.hiro.Move;
import org.hiro.character.Human;
import org.hiro.input.keyboard.KeyboardCommand;

public class RightCommand implements KeyboardCommand {

    @Override
    public void execute() {
        Move.do_move(Human.instance, 0, 1);
    }
}
