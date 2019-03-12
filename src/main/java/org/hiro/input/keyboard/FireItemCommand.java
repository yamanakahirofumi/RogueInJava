package org.hiro.input.keyboard;

import org.hiro.Global;
import org.hiro.Misc;
import org.hiro.WeaponMethod;
import org.hiro.character.Player;

public class FireItemCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        if (!Misc.get_dir()) {
            Global.after = false;
        } else {
            WeaponMethod.missile(Global.delta);
        }
    }
}
