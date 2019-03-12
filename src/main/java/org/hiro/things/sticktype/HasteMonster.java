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

public class HasteMonster extends Stick {
    public HasteMonster() {
        super();
    }

    @Override
    public void shake(Player player) {
        AbstractCoordinate tmp2 = player.getPosition();
        while (IOUtil.step_ok(Util.winat(tmp2))) {
            tmp2 = Global.delta.add(tmp2);
        }
        OriginalMonster tp = Util.getPlace(tmp2).p_monst;
        if (tp != null) {
            if (tp.containsState(StateEnum.ISSLOW)) {
                tp.removeState(StateEnum.ISSLOW);
            } else {
                tp.addState(StateEnum.ISHASTE);
            }
            Global.delta = new Coordinate(tmp2);
            Chase.runto(Global.delta);
        }
        this.use();
    }
}
