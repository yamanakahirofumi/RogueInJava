package org.hiro.things.scrolltype;

import org.hiro.Const;
import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.Place;
import org.hiro.character.Human;
import org.hiro.character.StateEnum;
import org.hiro.things.ObjectType;
import org.hiro.things.Scroll;
import org.hiro.things.ScrollEnum;
import org.hiro.things.ThingImp;

public class MapScroll extends Scroll {
    public MapScroll() {
        super();
    }

    @Override
    public void read() {
        /*
         * Scroll of magic mapping.
         */
        Global.scr_info[ScrollEnum.MapScroll.getValue()].know();
        IOUtil.msg("oh, now this scroll has a map on it");
        /*
         * take all the things we want to keep hidden out of the window
         */
        ObjectType chp;
        for (Place pp : Global.places) {
            switch (chp = pp.p_ch) {
                case DOOR:
                case STAIRS:
                    break;

                case Horizon:
                case Vert:
                    if ((pp.p_flags & Const.F_REAL) == 0) {
                        chp = pp.p_ch = ObjectType.DOOR;
                        pp.p_flags |= Const.F_REAL;
                    }
                    break;
                case Blank:
                    if ((pp.p_flags & Const.F_REAL) != 0) {
                        if ((pp.p_flags & Const.F_PASS) != 0) {
                            if ((pp.p_flags & Const.F_REAL) == 0) {
                                pp.p_ch = ObjectType.PASSAGE;
                            }
                            pp.p_flags |= (Const.F_SEEN | Const.F_REAL);
                            chp = ObjectType.PASSAGE;
                        } else {
                            chp = ObjectType.Blank;
                        }
                        break;
                    }
                    pp.p_flags |= Const.F_REAL;
                    pp.p_ch = ObjectType.PASSAGE;
                    /* FALLTHROUGH */

                case PASSAGE:
                    if ((pp.p_flags & Const.F_REAL) == 0) {
                        pp.p_ch = ObjectType.PASSAGE;
                    }
                    pp.p_flags |= (Const.F_SEEN | Const.F_REAL);
                    chp = ObjectType.PASSAGE;
                    break;

                case FLOOR:
                    if ((pp.p_flags & Const.F_REAL) != 0) {
                        chp = ObjectType.Blank;
                    } else {
                        chp = ObjectType.TRAP;
                        pp.p_ch = ObjectType.TRAP;
                        pp.p_flags |= (Const.F_SEEN | Const.F_REAL);
                    }
                    break;

                default:
                    if ((pp.p_flags & Const.F_PASS) != 0) {
                        if ((pp.p_flags & Const.F_REAL) == 0) {
                            pp.p_ch = ObjectType.PASSAGE;
                        }
                        pp.p_flags |= (Const.F_SEEN | Const.F_REAL);
                        chp = ObjectType.PASSAGE;
                    } else {
                        chp = ObjectType.Blank;
                    }
                    break;
            }
            if (chp != ObjectType.Blank) {
                ThingImp monst = pp.p_monst;
                if (monst != null) {
                    monst._t_oldch = chp.getValue();
                }
                if (monst == null || !Human.instance.containsState(StateEnum.SEEMONST)) {
                    // Display.mvaddch(y,x, chp.getValue());
                }
            }
        }
    }
}
