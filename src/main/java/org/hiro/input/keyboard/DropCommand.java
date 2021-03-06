package org.hiro.input.keyboard;

import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.ThingMethod;
import org.hiro.character.Player;

public class DropCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        if(ThingMethod.dropCheck()){
            ThingMethod.drop(player);
        }else {
            Global.after = false;
            IOUtil.msg("there is something there already");
        }
    }
}
