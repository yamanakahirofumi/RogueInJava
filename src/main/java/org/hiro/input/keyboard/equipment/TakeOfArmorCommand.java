package org.hiro.input.keyboard.equipment;

import org.hiro.ArmorMethod;
import org.hiro.character.Human;
import org.hiro.input.keyboard.KeyboardCommand;

public class TakeOfArmorCommand implements KeyboardCommand {

    @Override
    public void execute() {
        ArmorMethod.take_off(Human.instance);
    }
}
