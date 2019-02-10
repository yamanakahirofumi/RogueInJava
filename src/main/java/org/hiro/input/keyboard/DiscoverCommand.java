package org.hiro.input.keyboard;

import org.hiro.Global;

public class DiscoverCommand implements KeyboardCommand {

    @Override
    public void execute() {
        Global.after = false;
        // discovered(); // TODO: 後で
    }
}
