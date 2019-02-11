package org.hiro.input.keyboard.print;

import org.hiro.Command;
import org.hiro.character.Human;
import org.hiro.input.keyboard.KeyboardCommand;

public class PrintCurrentArmorCommand implements KeyboardCommand {

    @Override
    public void execute() {
        Command.current(Human.instance.getArmor(), "wearing", null);
    }
}
