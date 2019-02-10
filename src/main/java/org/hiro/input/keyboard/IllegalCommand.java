package org.hiro.input.keyboard;

import org.hiro.Global;

public class IllegalCommand implements KeyboardCommand {

    @Override
    public void execute() {
        /* "Legal" illegal command */
        Global.after = false;
    }
}
