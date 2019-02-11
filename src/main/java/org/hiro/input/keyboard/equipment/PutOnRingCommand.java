package org.hiro.input.keyboard.equipment;

import org.hiro.RingMethod;
import org.hiro.input.keyboard.KeyboardCommand;

public class PutOnRingCommand implements KeyboardCommand {

    @Override
    public void execute() {
        RingMethod.ring_on();
    }
}
