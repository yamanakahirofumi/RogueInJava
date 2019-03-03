package org.hiro.input.keyboard.move;

import org.hiro.Command;
import org.hiro.Game;
import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.New_Level;
import org.hiro.Rip;
import org.hiro.Util;
import org.hiro.character.Human;
import org.hiro.character.Player;
import org.hiro.input.keyboard.KeyboardCommand;
import org.hiro.things.ObjectType;

public class UpFloorCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
        Player player = Human.instance;
        Global.after = false;
        if (Command.levit_check(player)) {
            return;
        }
        if (Util.getPlace(Global.player._t_pos).p_ch == ObjectType.STAIRS) {
            if (Game.getInstance().isGoal()) {
                Human.instance.downstairs();
                if (Human.instance.getLevel() == 0) {
                    Rip.total_winner();
                }
                New_Level.new_level(player);
                IOUtil.msg("you feel a wrenching sensation in your gut");
            } else {
                IOUtil.msg("your way is magically blocked");
            }
        } else {
            IOUtil.msg("I see no way up");
        }
    }
}
