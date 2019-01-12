package org.hiro;

import org.hiro.character.StateEnum;
import org.hiro.map.Coordinate;
import org.hiro.map.RoomInfoEnum;
import org.hiro.output.Display;
import org.hiro.things.ObjectType;
import org.hiro.things.ThingImp;

public class Rooms {
    /*
     * enter_room:
     *	Code that is executed whenver you appear in a room
     */
    static void enter_room(Coordinate cp) {

        Room rp = Global.player.t_room = Chase.roomin(cp);
        Move.door_open(rp);
        if (!rp.containInfo(RoomInfoEnum.ISDARK) && !Global.player.containsState(StateEnum.ISBLIND))
            for (int y = rp.r_pos.y; y < rp.r_max.y + rp.r_pos.y; y++) {
                Display.move(y, rp.r_pos.x);
                for (int x = rp.r_pos.x; x < rp.r_max.x + rp.r_pos.x; x++) {
                    ThingImp tp = Global.places.get((x << 5) + y).p_monst;
                    // chtype ch;
                    ObjectType ch = Global.places.get((x << 5) + y).p_ch;
                    if (tp == null) {
                        if (Util.CCHAR(Display.inch()) != ch.getValue()) {
                            Display.addch(ch.getValue());
                        } else {
                            Display.move(y, x + 1);
                        }
                    } else {
                        tp._t_oldch = ch.getValue();
                        if (!Chase.see_monst(tp)) {
                            if (Global.player.containsState(StateEnum.SEEMONST)) {
                                Display.standout();
                                Display.addch((char) tp._t_disguise);
                                Display.standend();
                            } else {
                                Display.addch(ch.getValue());
                            }
                        } else {
                            Display.addch((char) tp._t_disguise);
                        }
                    }
                }
            }
    }


    /*
     * leave_room:
     *	Code for when we exit a room
     */
    static void leave_room(Coordinate cp) {
        Room rp = Global.player.t_room;

        if (rp.containInfo(RoomInfoEnum.ISMAZE)) {
            return;
        }

        ObjectType floor;
        if (rp.containInfo(RoomInfoEnum.ISGONE)) {
            floor = ObjectType.PASSAGE;
        } else if (!rp.containInfo(RoomInfoEnum.ISDARK) || Global.player.containsState(StateEnum.ISBLIND)) {
            floor = ObjectType.FLOOR;
        } else {
            floor = ObjectType.Blank;
        }

        Global.player.t_room = Global.passages[Util.flat(cp.y, cp.x) & Const.F_PNUM];
        for (int y = rp.r_pos.y; y < rp.r_max.y + rp.r_pos.y; y++)
            for (int x = rp.r_pos.x; x < rp.r_max.x + rp.r_pos.x; x++) {
                Display.move(y, x);
                int ch = Util.CCHAR(Display.inch());
                if (ch == ObjectType.FLOOR.getValue()) {
                    if (floor.getValue() == ObjectType.Blank.getValue() && ch != ' ') {
                        Display.addch(' ');
                    }
                } else {
                    /*
                     * to check for monster, we have to strip out
                     * standout bit
                     */
                    if (Character.isUpperCase(ch)) {
                        if (Global.player.containsState(StateEnum.SEEMONST)) {
                            Display.standout();
                            Display.addch((char) ch);
                            Display.standend();
                            break;
                        }
                        Place pp = Util.INDEX(y, x);
                        Display.addch(pp.p_ch == ObjectType.DOOR ? ObjectType.DOOR.getValue() : floor.getValue());
                    }
                }
            }
        Move.door_open(rp);
    }

}
