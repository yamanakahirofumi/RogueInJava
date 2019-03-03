package org.hiro.input.keyboard.move;

import org.hiro.Global;
import org.hiro.Util;
import org.hiro.character.Human;
import org.hiro.character.Player;
import org.hiro.character.StateEnum;
import org.hiro.input.keyboard.KeyboardCommand;

public class ContinuedMoveCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        boolean newcount = false; // TODO: 初期値仮置き
        int direction = 0; // TODO: 初期値仮置き
        int ch = 0; // TODO: 初期値仮置き
        if (!Human.instance.containsState(StateEnum.ISBLIND)) {
            Global.door_stop = true;
            Global.firstmove = true;
        }
        if (Global.count != 0 && !newcount) {
            ch = direction;
        } else {
            ch += ('A' - Util.CTRL('A'));
            direction = ch;
        }
        // goto over;
    }
}
