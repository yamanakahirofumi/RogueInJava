package org.hiro.input.keyboard.equipment;

import org.hiro.Pack;
import org.hiro.RingMethod;
import org.hiro.character.Player;
import org.hiro.input.keyboard.KeyboardCommand;
import org.hiro.things.ObjectType;
import org.hiro.things.Thing;

public class PutOnRingCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        Thing obj = Pack.get_item("put on", ObjectType.RING);
        RingMethod.ring_on(player, obj);
    }
}
