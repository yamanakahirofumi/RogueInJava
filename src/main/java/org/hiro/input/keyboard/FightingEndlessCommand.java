package org.hiro.input.keyboard;

import org.hiro.Global;
import org.hiro.character.Player;

public class FightingEndlessCommand extends FightingCommand {

    @Override
    public void execute(Player player) {
        Global.kamikaze = true;
        super.execute(player);
    }
}
