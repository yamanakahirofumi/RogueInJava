package org.hiro.input.keyboard.equipment;

import org.hiro.Pack;
import org.hiro.character.Player;
import org.hiro.input.keyboard.KeyboardCommand;
import org.hiro.things.ObjectType;
import org.hiro.things.Thing;

public class EatCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        Thing food = Pack.get_item("eat", ObjectType.FOOD);
        player.eat(food);
    }
}
