package org.hiro.input.keyboard.print;

import org.hiro.Global;
import org.hiro.input.keyboard.KeyboardCommand;

public class RedrawScreenCommand implements KeyboardCommand {

    @Override
    public void execute() {
        Global.after = false;
        // clearok(curscr, true);
        // wrefresh(curscr);

    }
}
