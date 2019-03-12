package org.hiro.things.scrolltype;

import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.character.Player;
import org.hiro.things.Food;
import org.hiro.things.Scroll;
import org.hiro.things.ScrollEnum;
import org.hiro.things.Thing;

public class FoodDetection extends Scroll {
    public FoodDetection(){
        super();
    }

    @Override
    public void read(Player player) {
        /*
         * Potion of gold detection
         */
        boolean chb = false;
        // Display.wclear(hw);
        for (Thing obj2 : Global.lvl_obj) {
            if (obj2 instanceof Food) {
                chb = true;
                // Display.wmove(hw, obj2._o_pos.y, obj2._o_pos.x);
                // Display.waddch(hw, ObjectType.FOOD);
            }
        }
        if (chb) {
            Global.scr_info[ScrollEnum.FoodDetection.getValue()].know();
            IOUtil.show_win("Your nose tingles and you smell food.--More--");
        } else
            IOUtil.msg("your nose tingles");

    }
}
