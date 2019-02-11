package org.hiro.input.keyboard.equipment;

import org.hiro.ScrollMethod;
import org.hiro.input.keyboard.KeyboardCommand;

public class ReadScrollCommand implements KeyboardCommand {

    @Override
    public void execute() {
        ScrollMethod.read_scroll();
    }
}
