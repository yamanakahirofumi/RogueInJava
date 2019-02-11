package org.hiro.input.keyboard;

import org.hiro.Command;
import org.hiro.Global;

public class HelpCommand implements KeyboardCommand {

    @Override
    public void execute() {
        Global.after = false;
        Command.help();
    }
}
