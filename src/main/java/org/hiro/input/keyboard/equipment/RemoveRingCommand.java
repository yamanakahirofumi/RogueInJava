package org.hiro.input.keyboard.equipment;

import org.hiro.RingMethod;
import org.hiro.input.keyboard.KeyboardCommand;

public class RemoveRingCommand implements KeyboardCommand {

    @Override
    public void execute() {
        RingMethod.ring_off();
    }
}
