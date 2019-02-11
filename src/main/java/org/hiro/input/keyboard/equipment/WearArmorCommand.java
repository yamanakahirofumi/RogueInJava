package org.hiro.input.keyboard.equipment;

import org.hiro.ArmorMethod;
import org.hiro.input.keyboard.KeyboardCommand;

public class WearArmorCommand implements KeyboardCommand {

    @Override
    public void execute() {
        ArmorMethod.wear();
    }
}
