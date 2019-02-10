package org.hiro.input.keyboard.print;

import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.input.keyboard.KeyboardCommand;

public class RepeatLastMessageCommand implements KeyboardCommand {

    @Override
    public void execute() {
        Global.after = false;
        IOUtil.msg(Global.huh);
    }
}
