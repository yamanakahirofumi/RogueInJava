package org.hiro.input.keyboard.print;

import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.character.Player;
import org.hiro.input.keyboard.KeyboardCommand;

public class RepeatLastMessageCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        Global.after = false;
        IOUtil.msg(Global.huh);
    }
}
