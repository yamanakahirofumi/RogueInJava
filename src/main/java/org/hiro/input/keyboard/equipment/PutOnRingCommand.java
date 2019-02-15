package org.hiro.input.keyboard.equipment;

import org.hiro.Pack;
import org.hiro.RingMethod;
import org.hiro.character.Human;
import org.hiro.input.keyboard.KeyboardCommand;
import org.hiro.things.ObjectType;
import org.hiro.things.Thing;

public class PutOnRingCommand implements KeyboardCommand {

    @Override
    public void execute() {
        Thing obj = Pack.get_item("put on", ObjectType.RING);
        RingMethod.ring_on(Human.instance, obj);
    }
}
