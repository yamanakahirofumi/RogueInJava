package org.hiro.input.keyboard;

import org.hiro.Global;
import org.hiro.ThingMethod;

public class EscapeCommand implements KeyboardCommand {

    @Override
    public void execute() {
        Global.door_stop = false;
        Global.count = 0;
        Global.after = false;
        Global.again = false;
    }
}
