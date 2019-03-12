package org.hiro.input.keyboard.print;

import org.hiro.Global;
import org.hiro.character.Player;
import org.hiro.input.keyboard.KeyboardCommand;

public class RedrawScreenCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        Global.after = false;
        // clearok(curscr, true);
        // wrefresh(curscr);

    }
}
