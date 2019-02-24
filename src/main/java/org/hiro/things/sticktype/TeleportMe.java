package org.hiro.things.sticktype;

import org.hiro.Chase;
import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Util;
import org.hiro.character.Player;
import org.hiro.character.StateEnum;
import org.hiro.map.AbstractCoordinate;
import org.hiro.map.Coordinate;
import org.hiro.things.OriginalMonster;
import org.hiro.things.Stick;

public class TeleportMe extends Stick {
    public TeleportMe() {
        super();
    }

    @Override
    public void shake(Player player) {
        AbstractCoordinate tmp = new Coordinate(Global.player._t_pos);
        while (IOUtil.step_ok(Util.winat(tmp))) {
            tmp = tmp.add(Global.delta);
        }
        OriginalMonster tp = Util.getPlace(tmp).p_monst;
        if (tp != null) {
            int monster = tp.getType();
            if (monster == 'F') {
                player.removeState(StateEnum.ISHELD);
            }
            Coordinate new_pos = Global.player._t_pos.add(Global.delta);
            tp.setRunPosition(Global.player._t_pos);
            tp.addState(StateEnum.ISRUN);
            Chase.relocate(tp, new_pos);
        }
        this.use();
    }
}
