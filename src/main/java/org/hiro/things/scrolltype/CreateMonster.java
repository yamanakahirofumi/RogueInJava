package org.hiro.things.scrolltype;

import org.hiro.IOUtil;
import org.hiro.Misc;
import org.hiro.Monst;
import org.hiro.Util;
import org.hiro.character.Player;
import org.hiro.map.AbstractCoordinate;
import org.hiro.map.Coordinate;
import org.hiro.things.ObjectType;
import org.hiro.things.OriginalMonster;
import org.hiro.things.Scroll;
import org.hiro.things.ThingImp;

public class CreateMonster extends Scroll {
    public CreateMonster() {
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
        AbstractCoordinate mp = new Coordinate();
        ObjectType cho;
        for (int y = player.getPositionY() - 1; y <= player.getPositionY() + 1; y++) {
            for (int x = player.getPositionX() - 1; x <= player.getPositionX() + 1; x++) {
                AbstractCoordinate tmp = new Coordinate(x, y);
                /*
                 * Don't put a monster in top of the player.
                 */
                if (player.getPosition().equals(tmp)) {
                    continue;
                }
                /*
                 * Or anything else nasty
                 * Also avoid a xeroc which is disguised as scroll
                 */
                else if (Util.getPlace(tmp).p_monst == null && IOUtil.step_ok(cho = Util.winat(tmp))) {
                    if (cho == ObjectType.SCROLL
                            && Misc.find_obj(tmp) instanceof Scare) {
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
            OriginalMonster obj2 = new ThingImp();
            Monst.new_monster(obj2, Monst.randmonster(false), mp);
        }

    }
}
