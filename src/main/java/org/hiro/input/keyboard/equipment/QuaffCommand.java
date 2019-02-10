package org.hiro.input.keyboard.equipment;

import org.hiro.Potions;
import org.hiro.input.keyboard.KeyboardCommand;

public class QuaffCommand implements KeyboardCommand {

    @Override
    public void execute() {
        Potions.quaff();
    }
}
