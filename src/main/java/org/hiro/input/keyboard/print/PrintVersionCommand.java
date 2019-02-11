package org.hiro.input.keyboard.print;

import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.input.keyboard.KeyboardCommand;

public class PrintVersionCommand implements KeyboardCommand {

    @Override
    public void execute() {
        Global.after = false;
        IOUtil.msg("version %s. (mctesq was here)", Global.release);
    }
}
