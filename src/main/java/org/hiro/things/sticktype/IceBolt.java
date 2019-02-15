package org.hiro.things.sticktype;

import org.hiro.Global;
import org.hiro.StickMethod;
import org.hiro.character.Player;
import org.hiro.things.Stick;
import org.hiro.things.StickEnum;

public class IceBolt extends Stick {
    public IceBolt(){
        super();
    }

    @Override
    public void shake(Player player) {
        StickMethod.fire_bolt(Global.player._t_pos, Global.delta, "ice");
        Global.ws_info[StickEnum.IceBolt.getValue()].know();
        this.use();
    }
}
