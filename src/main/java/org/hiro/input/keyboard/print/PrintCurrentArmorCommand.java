package org.hiro.input.keyboard.print;

import org.hiro.Command;
import org.hiro.character.Player;
import org.hiro.input.keyboard.KeyboardCommand;

public class PrintCurrentArmorCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        Command.current(player.getArmor(), "wearing", null);
    }
}
