package org.hiro;

import org.hiro.character.Human;
import org.hiro.character.StateEnum;
import org.hiro.map.AbstractCoordinate;
import org.hiro.map.RoomInfoEnum;
import org.hiro.output.Display;
import org.hiro.things.ObjectType;
import org.hiro.things.OriginalMonster;

public class Rooms {
    /*
     * enter_room:
     *	Code that is executed whenver you appear in a room
     */
    public static void enter_room(AbstractCoordinate cp) {

        Room rp = Chase.roomin(cp);
        Human.instance.setRoom(rp);
        Move.door_open(rp);
        if (!rp.containInfo(RoomInfoEnum.ISDARK) && !Human.instance.containsState(StateEnum.ISBLIND))
            for (int y = rp.r_pos.getY(); y < rp.r_max.getY() + rp.r_pos.getY(); y++) {
                Display.move(y, rp.r_pos.getX());
                for (int x = rp.r_pos.getX(); x < rp.r_max.getX() + rp.r_pos.getX(); x++) {
                    OriginalMonster tp = Util.INDEX(y, x).p_monst;
                    // chtype ch;
                    ObjectType ch = Util.INDEX(y, x).p_ch;
                    if (tp == null) {
                        if (Util.CCHAR(Display.inch()) != ch.getValue()) {
                            Display.addch(ch.getValue());
                        } else {
                            Display.move(y, x + 1);
                        }
                    } else {
                        tp.setFloorTile(ch.getValue());
                        if (!Chase.see_monst(tp)) {
                            if (Human.instance.containsState(StateEnum.SEEMONST)) {
                                Display.standout();
                                Display.addch((char) tp.getDisplayTile());
                                Display.standend();
                            } else {
                                Display.addch(ch.getValue());
                            }
                        } else {
                            Display.addch((char) tp.getDisplayTile());
                        }
                    }
                }
            }
    }


    /*
     * leave_room:
     *	Code for when we exit a room
     */
    static void leave_room(AbstractCoordinate cp) {
        Room rp = Human.instance.getRoom();

        if (rp.containInfo(RoomInfoEnum.ISMAZE)) {
            return;
        }

        ObjectType floor;
        if (rp.containInfo(RoomInfoEnum.ISGONE)) {
            floor = ObjectType.PASSAGE;
        } else if (!rp.containInfo(RoomInfoEnum.ISDARK) || Human.instance.containsState(StateEnum.ISBLIND)) {
            floor = ObjectType.FLOOR;
        } else {
            floor = ObjectType.Blank;
        }

        Human.instance.setRoom(Global.passages[Util.flat(cp) & Const.F_PNUM]);
        for (int y = rp.r_pos.getY(); y < rp.r_max.getY() + rp.r_pos.getY(); y++)
            for (int x = rp.r_pos.getX(); x < rp.r_max.getX() + rp.r_pos.getX(); x++) {
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
                        if (Human.instance.containsState(StateEnum.SEEMONST)) {
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
