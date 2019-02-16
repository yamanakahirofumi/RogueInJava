package org.hiro.input.keyboard;

import org.hiro.Command;
import org.hiro.character.Human;

public class SearchCommand implements KeyboardCommand {

    @Override
    public void execute() {
        Command.search(Human.instance);
    }
}
