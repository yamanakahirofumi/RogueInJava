package org.hiro.input.keyboard;

import org.hiro.Global;

public class HelpCommand implements KeyboardCommand {

    @Override
    public void execute() {
        Global.after = false;
        // help(); // TODO: 後で
    }
}
