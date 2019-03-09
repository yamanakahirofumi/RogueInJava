package org.hiro.things.sticktype;

import org.hiro.Chase;
import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Monst;
import org.hiro.Util;
import org.hiro.character.Player;
import org.hiro.character.StateEnum;
import org.hiro.map.AbstractCoordinate;
import org.hiro.map.Coordinate;
import org.hiro.output.Display;
import org.hiro.things.OriginalMonster;
import org.hiro.things.Stick;
import org.hiro.things.StickEnum;
import org.hiro.things.Thing;

import java.util.List;

public class ChangeMonster extends Stick {
    public ChangeMonster() {
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
            List<Thing> pp = tp.getBaggage();
            Global.mlist.remove(tp);
            if (Chase.see_monst(tp)) {
                Display.mvaddch(tmp, Util.getPlace(tmp).p_ch.getValue());
            }
            int oldValue = tp.getFloorTile();
            Global.delta = new Coordinate(tmp);
            Monst.new_monster(tp, monster = Util.rnd(26) + 'A', Global.delta);
            if (Chase.see_monst(tp)) {
                Display.mvaddch(tmp, (char) monster);
            }
            tp.setFloorTile(oldValue);
            tp.setBaggage(pp);
            Global.ws_info[StickEnum.ChangeMoster.getValue()].addKnown(Chase.see_monst(tp));

        }
        this.use();
    }
}
