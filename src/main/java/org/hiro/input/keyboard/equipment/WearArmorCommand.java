package org.hiro.input.keyboard.equipment;

import org.hiro.ArmorMethod;
import org.hiro.Pack;
import org.hiro.character.Player;
import org.hiro.input.keyboard.KeyboardCommand;
import org.hiro.things.ObjectType;
import org.hiro.things.Thing;

public class WearArmorCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        Thing obj = Pack.get_item("wear", ObjectType.ARMOR);
        ArmorMethod.wear(player, obj);
    }
}
