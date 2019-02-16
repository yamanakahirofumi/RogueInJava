package org.hiro.input.keyboard;

import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.ThingMethod;
import org.hiro.character.Human;

public class DropCommand implements KeyboardCommand {

    @Override
    public void execute() {
        if(ThingMethod.dropCheck()){
            ThingMethod.drop(Human.instance);
        }else {
            Global.after = false;
            IOUtil.msg("there is something there already");
        }
    }
}
