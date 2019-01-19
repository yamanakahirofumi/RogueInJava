package org.hiro.things.sticktype;

import org.hiro.Chase;
import org.hiro.Const;
import org.hiro.Fight;
import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Room;
import org.hiro.Util;
import org.hiro.character.Human;
import org.hiro.map.RoomInfoEnum;
import org.hiro.things.ObjectType;
import org.hiro.things.Stick;
import org.hiro.things.ThingImp;

import java.util.ArrayList;
import java.util.List;

public class Drain extends Stick {
    public Drain(){
        super();
    }

    @Override
    public void shake() {
        /*
         * take away 1/2 of hero's hit points, then take it away
         * evenly from the monsters in the room (or next to hero
         * if he is in a passage)
         */
        if (Human.instance.getHp() < 2) {
            IOUtil.msg("you are too weak to use it");
        } else {
            drain();
        }

    }

    /*
     * drain:
     *	Do drain hit points from player shtick
     */
    private void drain() {/*
         * First cnt how many things we need to spread the hit points among
         */
        Room corp;
        if (Util.getPlace(Global.player._t_pos).p_ch == ObjectType.DOOR) {
            corp = Global.passages[Util.flat(Global.player._t_pos) & Const.F_PNUM];
        } else {
            corp = null;
        }
        boolean pass = Global.player.t_room.containInfo(RoomInfoEnum.ISGONE);
        List<ThingImp> drainList = new ArrayList<>();
        for (ThingImp mp : Global.mlist) {
            if (mp.t_room == Global.player.t_room || mp.t_room == corp ||
                    (pass && Util.getPlace(mp._t_pos).p_ch == ObjectType.DOOR &&
                            Global.passages[Util.flat(mp._t_pos) & Const.F_PNUM] == Global.player.t_room)) {
                drainList.add(mp);
            }
        }
        if (drainList.size() == 0) {
            IOUtil.msg("you have a tingling feeling");
            return;
        }
        Global.player._t_stats.s_hpt /= 2;
        int cnt = Human.instance.getHp() / drainList.size();
        /*
         * Now zot all of the monsters
         */
        for (ThingImp dp : drainList) {
            if ((dp._t_stats.s_hpt -= cnt) <= 0) {
                Fight.killed(dp, Chase.see_monst(dp));
            } else {
                Chase.runto(dp._t_pos);
            }
        }
        this.use();
    }



}
