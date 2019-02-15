package org.hiro.things.sticktype;

import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Init;
import org.hiro.Rooms;
import org.hiro.Util;
import org.hiro.character.Player;
import org.hiro.map.RoomInfoEnum;
import org.hiro.things.Stick;
import org.hiro.things.StickEnum;

public class LightUp extends Stick {
    public LightUp(){
        super();
        this._o_arm = Util.rnd(10) + 10;
    }

    @Override
    public void shake(Player player){
        /*
         * Reddy Kilowat wand.  Light up the room
         */
        Global.ws_info[StickEnum.LightUp.getValue()].know();
        if (Global.player.t_room.containInfo(RoomInfoEnum.ISGONE)) {
            IOUtil.msg("the corridor glows and then fades");
        } else {
            Global.player.t_room.removeInfo(RoomInfoEnum.ISDARK);
            /*
             * Light the room and put the player back up
             */
            Rooms.enter_room(Global.player._t_pos);
            IOUtil.addmsg("the room is lit");
            if (!Global.terse) {
                IOUtil.addmsg(" by a shimmering %s light", Init.pick_color("blue"));
            }
            IOUtil.endmsg();
        }
        this.use();
    }


}
