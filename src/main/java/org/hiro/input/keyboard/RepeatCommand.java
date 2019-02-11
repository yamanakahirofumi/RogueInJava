package org.hiro.input.keyboard;

import org.hiro.Global;
import org.hiro.IOUtil;

public class RepeatCommand implements KeyboardCommand {

    @Override
    public void execute() {
        int ch; // TODO
        if (Global.last_comm == '\0') {
            IOUtil.msg("you haven't typed a command yet");
            Global.after = false;
        } else {
            ch = Global.last_comm;
            Global.again = true;
            // goto over;
        }
    }
}
