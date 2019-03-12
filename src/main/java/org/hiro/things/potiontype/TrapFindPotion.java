package org.hiro.things.potiontype;

import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Misc;
import org.hiro.character.Player;
import org.hiro.things.OriginalMonster;
import org.hiro.things.Potion;
import org.hiro.things.PotionEnum;
import org.hiro.things.Thing;

public class TrapFindPotion extends Potion {
    @Override
    public void quaff(Player player) {
        /*
         * Potion of magic detection.  Show the potions and scrolls
         */
        boolean show = false;
        if (Global.lvl_obj.size() != 0) {
            // wclear(hw);
            for (Thing tp : Global.lvl_obj) {
                if (tp.isMagic()) {
                    show = true;
                    // wmove(hw, tp._o_pos.y, tp._o_pos.x);
                    // waddch(hw, ObjectType.MAGIC);
                    Global.pot_info[PotionEnum.TrapFind.getValue()].know();
                }
            }
            for (OriginalMonster mp : Global.mlist) {
                for (Thing tp : mp.getBaggage()) {
                    if (tp.isMagic()) {
                        show = true;
                        // wmove(hw, mp._t_pos.y, mp._t_pos.x);
                        // waddch(hw, ObjectType.MAGIC);
                    }
                }
            }
        }
        if (show) {
            Global.pot_info[PotionEnum.TrapFind.getValue()].know();
            IOUtil.show_win("You sense the presence of magic on this level.--More--");
        } else {
            IOUtil.msg("you have a %s feeling for a moment, then it passes",
                    Misc.choose_str("normal", "strange"));
        }

    }
}
