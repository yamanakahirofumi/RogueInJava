package org.hiro.input.keyboard;

import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Potions;

public class ChangeWizardModeCommand implements KeyboardCommand {

    @Override
    public void execute() {
        Global.after = false;
        if (Global.wizard) {
            Global.wizard = false;
            Potions.turn_see(true);
            IOUtil.msg("not wizard any more");
        } else {
            // Global.wizard = passwd(); //後で
            if (Global.wizard) {
                Global.noscore = true;
                Potions.turn_see(false);
                // IOUtil.msg("you are suddenly as smart as Ken Arnold in dungeon #%d", dnum);
            } else {
                IOUtil.msg("sorry");
            }
        }
    }
}
