package org.hiro.things.sticktype;

import org.hiro.Global;
import org.hiro.StickMethod;
import org.hiro.things.Stick;
import org.hiro.things.StickEnum;

public class ElectricBolt extends Stick {
    public ElectricBolt(){
        super();
    }

    @Override
    public void shake(){
        StickMethod.fire_bolt(Global.player._t_pos, Global.delta, "bolt");
        Global.ws_info[StickEnum.ElectricBolt.getValue()].know();
        this.use();
    }
}
