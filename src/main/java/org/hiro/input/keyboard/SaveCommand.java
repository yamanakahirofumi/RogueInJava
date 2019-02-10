package org.hiro.input.keyboard;

import org.hiro.Global;

public class SaveCommand implements KeyboardCommand {

    @Override
    public void execute() {
        Global.after = false;
        // save_game(); // TODO: あとで
    }
}
