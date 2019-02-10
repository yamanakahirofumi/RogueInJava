package org.hiro.input.keyboard;

import org.hiro.Global;

public class FightingEndlessCommand extends FightingCommand {

    @Override
    public void execute() {
        Global.kamikaze = true;
        super.execute();
    }
}
