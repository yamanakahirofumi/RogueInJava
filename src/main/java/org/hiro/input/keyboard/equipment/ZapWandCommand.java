package org.hiro.input.keyboard.equipment;

import org.hiro.Global;
import org.hiro.Misc;
import org.hiro.StickMethod;
import org.hiro.input.keyboard.KeyboardCommand;

public class ZapWandCommand implements KeyboardCommand {

    @Override
    public void execute() {
        if (Misc.get_dir()) {
            StickMethod.do_zap();
        } else {
            Global.after = false;
        }
    }
}
