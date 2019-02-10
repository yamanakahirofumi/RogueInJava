package org.hiro.input.keyboard;

import org.hiro.Global;
import org.hiro.Main2;

public class QuitCommand implements KeyboardCommand {

    @Override
    public void execute() {
        Global.after = false;
        Global.q_comm = true;
        Main2.quit(0);
        Global.q_comm = false;
    }
}
