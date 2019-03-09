package org.hiro.things.sticktype;

import org.hiro.Global;
import org.hiro.StickMethod;
import org.hiro.character.Player;
import org.hiro.things.Stick;
import org.hiro.things.StickEnum;

public class FireBolt extends Stick {
    public FireBolt(){
        super();
    }

    @Override
    public void shake(Player player){
        StickMethod.fire_bolt(player.getPosition(), Global.delta, "flame");
        Global.ws_info[StickEnum.FireBolt.getValue()].know();
        this.use();
    }
}
