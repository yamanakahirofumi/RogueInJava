package org.hiro.input.keyboard;

import org.hiro.Global;
import org.hiro.Options;

public class SetOptionsCommand implements KeyboardCommand {

    @Override
    public void execute() {
        Options.option();
        Global.after = false;
    }
}
