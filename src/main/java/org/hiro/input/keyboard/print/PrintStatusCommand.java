package org.hiro.input.keyboard.print;

import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.character.Human;
import org.hiro.character.Player;
import org.hiro.input.keyboard.KeyboardCommand;

public class PrintStatusCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        Global.stat_msg = true;
        IOUtil.status(Human.instance);
        Global.stat_msg = false;
        Global.after = false;

    }
}
