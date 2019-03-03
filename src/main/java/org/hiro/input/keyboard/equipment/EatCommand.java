package org.hiro.input.keyboard.equipment;

import org.hiro.Misc;
import org.hiro.Pack;
import org.hiro.character.Human;
import org.hiro.character.Player;
import org.hiro.input.keyboard.KeyboardCommand;
import org.hiro.things.ObjectType;
import org.hiro.things.ThingImp;

public class EatCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        ThingImp food = Pack.get_item("eat", ObjectType.FOOD);
        Misc.eat(Human.instance, food);
    }
}
