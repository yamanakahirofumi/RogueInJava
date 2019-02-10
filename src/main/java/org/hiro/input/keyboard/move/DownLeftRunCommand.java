package org.hiro.input.keyboard.move;

import org.hiro.Move;
import org.hiro.input.keyboard.KeyboardCommand;

public class DownLeftRunCommand implements KeyboardCommand {

    @Override
    public void execute() {
        Move.do_run('b');
    }
}
