package org.hiro.things.sticktype;

import org.hiro.Global;
import org.hiro.StickMethod;
import org.hiro.character.Player;
import org.hiro.things.Stick;
import org.hiro.things.StickEnum;

public class ElectricBolt extends Stick {
    public ElectricBolt(){
        super();
    }

    @Override
    public void shake(Player player){
        StickMethod.fire_bolt(player.getPosition(), Global.delta, "bolt");
        Global.ws_info[StickEnum.ElectricBolt.getValue()].know();
        this.use();
    }
}
