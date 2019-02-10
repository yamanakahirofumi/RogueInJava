package org.hiro.input.keyboard.print;

import org.hiro.Command;
import org.hiro.character.Human;
import org.hiro.input.keyboard.KeyboardCommand;

public class PrintCurrentWeaponCommand implements KeyboardCommand {

    @Override
    public void execute() {
        Command.current(Human.instance.getWeapons().size() == 0 ? null : Human.instance.getWeapons().get(0), "wielding", null);
    }
}
