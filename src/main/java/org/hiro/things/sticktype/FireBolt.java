package org.hiro.things.sticktype;

import org.hiro.Global;
import org.hiro.StickMethod;
import org.hiro.things.Stick;
import org.hiro.things.StickEnum;

public class FireBolt extends Stick {
    public FireBolt(){
        super();
    }

    @Override
    public void shake(){
        StickMethod.fire_bolt(Global.player._t_pos, Global.delta, "flame");
        Global.ws_info[StickEnum.FireBolt.getValue()].know();
        this.use();
    }
}