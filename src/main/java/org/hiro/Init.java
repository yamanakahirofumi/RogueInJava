package org.hiro;

import org.hiro.character.StateEnum;

public class Init {

    /*
     * pick_color:
     *	If he is halucinating, pick a random color name and return it,
     *	otherwise return the given color.
     */
static String pick_color(String col)
    {
        return (Global.player.containsState(StateEnum.ISHALU) ? Global.rainbow[Util.rnd(Global.rainbow.length)] : col);
    }

}
