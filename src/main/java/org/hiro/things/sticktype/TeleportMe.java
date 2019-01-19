package org.hiro.things.sticktype;

import org.hiro.Chase;
import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Util;
import org.hiro.character.Human;
import org.hiro.character.StateEnum;
import org.hiro.map.Coordinate;
import org.hiro.things.Stick;
import org.hiro.things.ThingImp;

public class TeleportMe extends Stick {
    public TeleportMe() {
        super();
    }

    @Override
    public void shake() {
        Coordinate tmp = new Coordinate(Global.player._t_pos);
        while (IOUtil.step_ok(Util.winat(tmp))) {
            tmp = tmp.add(Global.delta);
        }
        ThingImp tp = Util.getPlace(tmp).p_monst;
        if (tp != null) {
            int monster = tp._t_type;
            if (monster == 'F') {
                Human.instance.removeState(StateEnum.ISHELD);
            }
            Coordinate new_pos = Global.player._t_pos.add(Global.delta);
            tp._t_dest = Global.player._t_pos;
            tp.addState(StateEnum.ISRUN);
            Chase.relocate(tp, new_pos);
        }
        this.use();
    }
}
