package org.hiro;

import org.hiro.character.Human;
import org.hiro.character.StateEnum;

public class Init {

    /*
     * pick_color:
     *	If he is halucinating, pick a random color name and return it,
     *	otherwise return the given color.
     */
    public static String pick_color(String col) {
        return (Human.instance.containsState(StateEnum.ISHALU) ? Global.rainbow[Util.rnd(Global.rainbow.length)] : col);
    }

}
