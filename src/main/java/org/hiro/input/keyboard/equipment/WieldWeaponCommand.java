package org.hiro.input.keyboard.equipment;

import org.hiro.WeaponMethod;
import org.hiro.input.keyboard.KeyboardCommand;

public class WieldWeaponCommand implements KeyboardCommand {

    @Override
    public void execute() {
        WeaponMethod.wield();
    }
}
