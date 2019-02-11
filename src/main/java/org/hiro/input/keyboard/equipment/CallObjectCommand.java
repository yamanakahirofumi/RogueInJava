package org.hiro.input.keyboard.equipment;

import org.hiro.Global;
import org.hiro.input.keyboard.KeyboardCommand;

public class CallObjectCommand implements KeyboardCommand {

    @Override
    public void execute() {
        // call();  // TODO: 後で
        Global.after = false;
    }
}
