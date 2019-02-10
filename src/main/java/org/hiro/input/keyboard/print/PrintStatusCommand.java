package org.hiro.input.keyboard.print;

import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.input.keyboard.KeyboardCommand;

public class PrintStatusCommand implements KeyboardCommand {

    @Override
    public void execute() {
        Global.stat_msg = true;
        IOUtil.status();
        Global.stat_msg = false;
        Global.after = false;

    }
}
