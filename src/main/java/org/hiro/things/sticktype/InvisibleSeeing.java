package org.hiro.things.sticktype;

import org.hiro.Chase;
import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Util;
import org.hiro.character.Player;
import org.hiro.character.StateEnum;
import org.hiro.map.AbstractCoordinate;
import org.hiro.output.Display;
import org.hiro.things.OriginalMonster;
import org.hiro.things.Stick;

public class InvisibleSeeing extends Stick {
    public InvisibleSeeing(){
    }

    @Override
    public void shake(Player player) {
        AbstractCoordinate tmp = player.getPosition();
        while (IOUtil.step_ok(Util.winat(tmp))) {
            tmp = Global.delta.add(tmp);
        }
        OriginalMonster tp = Util.getPlace(tmp).p_monst;
        if (tp != null) {
            int monster = tp.getType();
            if (monster == 'F') {
                player.removeState(StateEnum.ISHELD);
            }
            tp.addState(StateEnum.ISINVIS);
            if (Chase.isSee(player, tmp)) {
                Display.mvaddch(tmp, (char) tp.getFloorTile());
            }

        }
        this.use();
    }
}
