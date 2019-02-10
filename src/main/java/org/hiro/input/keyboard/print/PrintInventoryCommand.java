package org.hiro.input.keyboard.print;

import org.hiro.Global;
import org.hiro.Pack;
import org.hiro.input.keyboard.KeyboardCommand;
import org.hiro.things.ObjectType;

public class PrintInventoryCommand implements KeyboardCommand {

    @Override
    public void execute() {
        Global.after = false;
        Pack.inventory(Global.player.getBaggage(), ObjectType.Initial);    }
}
