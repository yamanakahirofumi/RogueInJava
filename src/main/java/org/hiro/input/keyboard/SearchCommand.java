package org.hiro.input.keyboard;

import org.hiro.Command;

public class SearchCommand implements KeyboardCommand {

    @Override
    public void execute() {
        Command.search();
    }
}
