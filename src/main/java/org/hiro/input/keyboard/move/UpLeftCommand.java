package org.hiro.input.keyboard.move;

import org.hiro.Move;
import org.hiro.character.Human;
import org.hiro.input.keyboard.KeyboardCommand;

public class UpLeftCommand implements KeyboardCommand {

    @Override
    public void execute() {
        Move.do_move(Human.instance, -1, -1);
    }
}
