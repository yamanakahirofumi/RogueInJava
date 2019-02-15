package org.hiro.things.sticktype;

import org.hiro.Chase;
import org.hiro.DrawRoom;
import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Util;
import org.hiro.character.Player;
import org.hiro.character.StateEnum;
import org.hiro.map.Coordinate;
import org.hiro.things.Stick;
import org.hiro.things.ThingImp;

public class TeleportAway extends Stick {
    public TeleportAway() {
        super();
    }

    @Override
    public void shake(Player player) {
        Coordinate tmp = new Coordinate(Global.player._t_pos);
        while (IOUtil.step_ok(Util.winat(tmp))) {
            tmp = Global.delta.add(tmp);
        }
        ThingImp tp = Util.getPlace(tmp).p_monst;
        if (tp != null) {
            int monster = tp._t_type;
            if (monster == 'F') {
                player.removeState(StateEnum.ISHELD);
            }
            Coordinate new_pos = new Coordinate();

            do {
                DrawRoom.find_floor(null, new_pos, false, true);
            } while (new_pos.equals(Global.player._t_pos));
            tp._t_dest = Global.player._t_pos;
            tp.addState(StateEnum.ISRUN);
            Chase.relocate(tp, new_pos);
        }
        this.use();

    }
}
