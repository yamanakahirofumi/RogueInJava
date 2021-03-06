package org.hiro.things.scrolltype;

import org.hiro.Global;
import org.hiro.Room;
import org.hiro.Wizard;
import org.hiro.character.Player;
import org.hiro.things.Scroll;
import org.hiro.things.ScrollEnum;

public class Teleportation extends Scroll {
    public Teleportation() {
        super();
    }

    @Override
    public void read(Player player) {
        /*
         * Scroll of teleportation:
         * Make him dissapear and reappear
         */
        Room cur_room = player.getRoom();
        Wizard.teleport(player);
        if (!cur_room.equals(player.getRoom())) {
            Global.scr_info[ScrollEnum.Teleportation.getValue()].know();
        }

    }
}
