package org.hiro.input.keyboard.equipment;

import org.hiro.Pack;
import org.hiro.ScrollMethod;
import org.hiro.character.Player;
import org.hiro.input.keyboard.KeyboardCommand;
import org.hiro.things.ObjectType;
import org.hiro.things.Thing;

public class ReadScrollCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        Thing obj = Pack.get_item("read", ObjectType.SCROLL);
        ScrollMethod.read_scroll(player, obj);
    }
}
