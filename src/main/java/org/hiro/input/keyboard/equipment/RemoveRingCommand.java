package org.hiro.input.keyboard.equipment;

import org.hiro.RingMethod;
import org.hiro.character.Player;
import org.hiro.input.keyboard.KeyboardCommand;

public class RemoveRingCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        RingMethod.ring_off(player);
    }
}
