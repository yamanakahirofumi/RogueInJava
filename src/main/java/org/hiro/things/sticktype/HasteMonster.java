package org.hiro.things.sticktype;

import org.hiro.Chase;
import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Util;
import org.hiro.character.StateEnum;
import org.hiro.map.Coordinate;
import org.hiro.things.Stick;
import org.hiro.things.ThingImp;

public class HasteMonster extends Stick {
    public HasteMonster() {
        super();
    }

    @Override
    public void shake() {
        Coordinate tmp2 = new Coordinate();
        tmp2.y = Global.player._t_pos.y;
        tmp2.x = Global.player._t_pos.x;
        while (IOUtil.step_ok(Util.winat(tmp2))) {
            tmp2.y += Global.delta.y;
            tmp2.x += Global.delta.x;
        }
        ThingImp tp = Util.getPlace(tmp2).p_monst;
        if (tp != null) {
            if (tp.containsState(StateEnum.ISSLOW)) {
                tp.removeState(StateEnum.ISSLOW);
            } else {
                tp.addState(StateEnum.ISHASTE);
            }
            Global.delta.y = tmp2.y;
            Global.delta.x = tmp2.x;
            Chase.runto(Global.delta);
        }
        this.use();
    }
}
