package org.hiro.input.keyboard;

import org.hiro.Global;

public class IdentifyCommand implements KeyboardCommand {

    @Override
    public void execute() {
        Global.after = false;
        // identify();  // TODO: 後で
    }
}
