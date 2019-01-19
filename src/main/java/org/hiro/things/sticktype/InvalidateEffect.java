package org.hiro.things.sticktype;

import org.hiro.Chase;
import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Util;
import org.hiro.character.Human;
import org.hiro.character.StateEnum;
import org.hiro.map.Coordinate;
import org.hiro.output.Display;
import org.hiro.things.Stick;
import org.hiro.things.ThingImp;

public class InvalidateEffect extends Stick {
    public InvalidateEffect() {
        super();
    }

    @Override
    public void shake() {
        Coordinate tmp = new Coordinate();
        tmp.y = Global.player._t_pos.y;
        tmp.x = Global.player._t_pos.x;
        while (IOUtil.step_ok(Util.winat(tmp))) {
            tmp.y += Global.delta.y;
            tmp.x += Global.delta.x;
        }
        ThingImp tp = Util.getPlace(tmp).p_monst;
        if (tp != null) {
            int monster = tp._t_type;
            if (monster == 'F') {
                Human.instance.removeState(StateEnum.ISHELD);
            }
            tp.addState(StateEnum.ISCANC);
            tp.removeState(StateEnum.ISINVIS);
            tp.removeState(StateEnum.CANHUH);
            tp._t_disguise = tp._t_type;
            if (Chase.see_monst(tp)) {
                Display.mvaddch(tmp.y, tmp.x, (char) tp._t_disguise);
            }
        }
        this.use();
    }


}
