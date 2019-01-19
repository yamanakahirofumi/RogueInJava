package org.hiro.things.sticktype;

import org.hiro.Chase;
import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Monst;
import org.hiro.Util;
import org.hiro.character.Human;
import org.hiro.character.StateEnum;
import org.hiro.map.Coordinate;
import org.hiro.output.Display;
import org.hiro.things.Stick;
import org.hiro.things.StickEnum;
import org.hiro.things.ThingImp;

import java.util.List;

public class ChangeMonster extends Stick {
    public ChangeMonster(){
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
            List<ThingImp> pp = tp.getBaggage();
            Global.mlist.remove(tp);
            if (Chase.see_monst(tp)) {
                Display.mvaddch(tmp.y, tmp.x, Util.getPlace(tmp).p_ch.getValue());
            }
            int oldValue = tp._t_oldch;
            Global.delta.y = tmp.y;
            Global.delta.x = tmp.x;
            Monst.new_monster(tp, monster = Util.rnd(26) + 'A', Global.delta);
            if (Chase.see_monst(tp)) {
                Display.mvaddch(tmp.y, tmp.x, (char) monster);
            }
            tp._t_oldch = oldValue;
            tp.setBaggage(pp);
            Global.ws_info[StickEnum.ChangeMoster.getValue()].addKnown(Chase.see_monst(tp));

        }
        this.use();
    }
}
