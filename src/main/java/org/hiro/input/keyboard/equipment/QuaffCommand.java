package org.hiro.input.keyboard.equipment;

import org.hiro.Pack;
import org.hiro.Potions;
import org.hiro.character.Human;
import org.hiro.character.Player;
import org.hiro.input.keyboard.KeyboardCommand;
import org.hiro.things.ObjectType;
import org.hiro.things.Thing;

public class QuaffCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        Thing obj = Pack.get_item("quaff", ObjectType.POTION);
        Potions.quaff(Human.instance, obj);
    }
}
