package org.hiro.things.scrolltype;

import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Misc;
import org.hiro.Monst;
import org.hiro.Util;
import org.hiro.character.Player;
import org.hiro.map.Coordinate;
import org.hiro.things.ObjectType;
import org.hiro.things.Scroll;
import org.hiro.things.ScrollEnum;
import org.hiro.things.ThingImp;

public class CreateMonster extends Scroll {
    public CreateMonster(){
        super();
    }

    @Override
    public void read(Player player) {
        /*
         * Create a monster:
         * First look in a circle around him, next try his room
         * otherwise give up
         */
        int i = 0;
        Coordinate mp = new Coordinate();
        ObjectType cho;
        for (int y = Global.player._t_pos.y - 1; y <= Global.player._t_pos.y + 1; y++) {
            for (int x = Global.player._t_pos.x - 1; x <= Global.player._t_pos.x + 1; x++) {
                Coordinate tmp = new Coordinate(x, y);
                /*
                 * Don't put a monster in top of the player.
                 */
                if (Global.player._t_pos.equals(tmp)) {
                    continue;
                }
                /*
                 * Or anything else nasty
                 * Also avoid a xeroc which is disguised as scroll
                 */
                else if (Util.getPlace(tmp).p_monst == null && IOUtil.step_ok(cho = Util.winat(tmp))) {
                    if (cho == ObjectType.SCROLL
                            && Misc.find_obj(tmp)._o_which == ScrollEnum.Scare.getValue()) {
                        continue;
                    } else if (Util.rnd(++i) == 0) {
                        mp = tmp;
                    }
                }
            }
        }
        if (i == 0) {
            IOUtil.msg("you hear a faint cry of anguish in the distance");
        } else {
            ThingImp obj2 = new ThingImp();
            Monst.new_monster(obj2, Monst.randmonster(false), mp);
        }

    }
}
