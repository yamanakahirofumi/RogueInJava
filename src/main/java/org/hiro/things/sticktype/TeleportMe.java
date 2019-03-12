package org.hiro.things.sticktype;

import org.hiro.Chase;
import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Util;
import org.hiro.character.Player;
import org.hiro.character.StateEnum;
import org.hiro.map.AbstractCoordinate;
import org.hiro.things.OriginalMonster;
import org.hiro.things.Stick;

public class TeleportMe extends Stick {
    public TeleportMe() {
        super();
    }

    @Override
    public void shake(Player player) {
        AbstractCoordinate tmp = player.getPosition();
        while (IOUtil.step_ok(Util.winat(tmp))) {
            tmp = tmp.add(Global.delta);
        }
        OriginalMonster tp = Util.getPlace(tmp).p_monst;
        if (tp != null) {
            int monster = tp.getType();
            if (monster == 'F') {
                player.removeState(StateEnum.ISHELD);
            }
            AbstractCoordinate new_pos = player.getPosition().add(Global.delta);
            tp.setRunPosition(player.getPosition());
            tp.addState(StateEnum.ISRUN);
            Chase.relocate(tp, new_pos);
        }
        this.use();
    }
}
