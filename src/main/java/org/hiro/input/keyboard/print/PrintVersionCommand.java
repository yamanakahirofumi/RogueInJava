package org.hiro.input.keyboard.print;

import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.character.Player;
import org.hiro.input.keyboard.KeyboardCommand;

public class PrintVersionCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        Global.after = false;
        IOUtil.msg("version %s. (mctesq was here)", Global.release);
    }
}
