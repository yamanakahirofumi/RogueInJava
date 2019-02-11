package org.hiro.input.keyboard;

import org.hiro.ThingMethod;

public class DropCommand implements KeyboardCommand {

    @Override
    public void execute() {
        ThingMethod.drop();
    }
}
