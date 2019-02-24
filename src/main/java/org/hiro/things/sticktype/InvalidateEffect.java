package org.hiro.things.sticktype;

import org.hiro.Chase;
import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Util;
import org.hiro.character.Player;
import org.hiro.character.StateEnum;
import org.hiro.map.Coordinate;
import org.hiro.output.Display;
import org.hiro.things.OriginalMonster;
import org.hiro.things.Stick;

public class InvalidateEffect extends Stick {
    public InvalidateEffect() {
        super();
    }

    @Override
    public void shake(Player player) {
        Coordinate tmp = new Coordinate(Global.player._t_pos);
        while (IOUtil.step_ok(Util.winat(tmp))) {
            tmp = Global.delta.add(tmp);
        }
        OriginalMonster tp = Util.getPlace(tmp).p_monst;
        if (tp != null) {
            int monster = tp.getType();
            if (monster == 'F') {
                player.removeState(StateEnum.ISHELD);
            }
            tp.addState(StateEnum.ISCANC);
            tp.removeState(StateEnum.ISINVIS);
            tp.removeState(StateEnum.CANHUH);
            tp.setDisplayTile(tp.getType());
            if (Chase.see_monst(tp)) {
                Display.mvaddch(tmp, (char) tp.getDisplayTile());
            }
        }
        this.use();
    }


}
