package org.hiro.input.keyboard.print;

import org.hiro.Command;
import org.hiro.character.Player;
import org.hiro.input.keyboard.KeyboardCommand;

public class PrintCurrentWeaponCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        Command.current(player.getWeapons().size() == 0 ? null : player.getWeapons().get(0), "wielding", null);
    }
}
