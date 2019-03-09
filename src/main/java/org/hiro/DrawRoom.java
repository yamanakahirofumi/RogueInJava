package org.hiro;


import org.hiro.character.Human;
import org.hiro.map.AbstractCoordinate;
import org.hiro.map.Coordinate;
import org.hiro.map.RoomInfoEnum;
import org.hiro.things.Gold;
import org.hiro.things.ObjectType;
import org.hiro.things.OriginalMonster;
import org.hiro.things.ThingImp;

public class DrawRoom {

    static Spot[][] maze = new Spot[Const.NUMLINES / 3 + 1][Const.NUMCOLS / 3 + 1];
    static int Maxy, Maxx, Starty, Startx;
    static int GOLDGRP = 1;


    static void do_rooms() {
        int left_out;
        AbstractCoordinate bsze = new Coordinate(Const.NUMCOLS / 3, Const.NUMLINES / 3);                /* maximum room size */
        /*
         * Clear things for a new level
         */
        for (int i = 0; i < Const.MAXROOMS; i++) {
            Room r = Global.rooms.get(i);
            r.r_goldval = 0;
            r.r_nexits = 0;
            r.clearInfo();
        }

        /*
         * Put the gone rooms, if any, on the level
         * 幾つかgoneな部屋を置く
         */
        left_out = Util.rnd(4);
        for (int i = 0; i < left_out; i++) {
            Room r = Global.rooms.get(rnd_room());
            r.addInfo(RoomInfoEnum.ISGONE);
        }
        AbstractCoordinate top;
        /*
         * dig and populate all the rooms on the level
         */
        for (int i = 0; i < Const.MAXROOMS; i++) {
            Room rp = Global.rooms.get(i);
            /*
             * Find upper left corner of box that this room goes in
             */
            top = new Coordinate((i % 3) * bsze.getX() + 1, (i / 3) * bsze.getY());
            if (rp.containInfo(RoomInfoEnum.ISGONE)) {
                /*
                 * Place a gone room.  Make certain that there is a blank line
                 * for passage drawing.
                 */
                do {
                    rp.r_pos = top.add(new Coordinate(Util.rnd(bsze.getX() - 2) + 1, Util.rnd(bsze.getY() - 2) + 1));
                    rp.r_max.setX(-Const.NUMCOLS);
                    rp.r_max.setY(-Const.NUMLINES);
                } while (!(rp.r_pos.getY() > 0 && rp.r_pos.getY() < Const.NUMLINES - 1));
                continue;
            }
            /*
             * set room type
             */
            if (Util.rnd(10) < Human.instance.getLevel() - 1) {
                rp.addInfo(RoomInfoEnum.ISDARK);        /* dark room */
                if (Util.rnd(15) == 0) {
                    rp.setInfo(RoomInfoEnum.ISMAZE);        /* maze room */
                }
            }
            /*
             * Find a place and size for a random room
             */
            if (rp.containInfo(RoomInfoEnum.ISMAZE)) {
                rp.r_max = bsze.add(new Coordinate(-1, -1));
                if (top.getX() == 1) {
                    rp.r_pos.setX(0);
                } else {
                    rp.r_pos.setX(top.getX());
                }

                if (top.getY() == 0) {
                    rp.r_pos.setY(rp.r_pos.getY() + 1);
                    rp.r_max.setY(rp.r_max.getY() - 1);
                } else {
                    rp.r_pos.setY(top.getY());
                }

            } else {
                do {
                    rp.r_max = new Coordinate(Util.rnd(bsze.getX() - 4) + 4, Util.rnd(bsze.getY() - 4) + 4);
                    rp.r_pos = top.add(new Coordinate(Util.rnd(bsze.getX() - rp.r_max.getX()), Util.rnd(bsze.getY() - rp.r_max.getY())));
                } while (rp.r_pos.getY() == 0);
            }
            draw_room(rp);
            /*
             * Put the gold in
             */
            Game game = Game.getInstance();
            if (Util.rnd(2) == 0 && (!game.isGoal() || Human.instance.getLevel() >= Global.max_level)) {
                Gold gold;

                gold = new Gold(Human.instance.getLevel());
                rp.r_goldval = gold.getGold();
                find_floor(rp, rp.r_gold, false, false);
                gold._o_pos = rp.r_gold;
                Util.getPlace(rp.r_gold).p_ch = ObjectType.GOLD;

                Global.lvl_obj.add(gold);
            }
            AbstractCoordinate mp = new Coordinate();
            /*
             * Put the monster in
             */
            if (Util.rnd(100) < (rp.r_goldval > 0 ? 80 : 25)) {
                OriginalMonster tp = new ThingImp();
                find_floor(rp, mp, false, true);
                Monst.new_monster(tp, Monst.randmonster(false), mp);
                Monst.give_pack(tp);
            }
        }

    }

    /*
     * rnd_room:
     *	Pick a room that is really there
     */
    @Deprecated
    static int rnd_room() {
        int rm;

        do {
            rm = Util.rnd(Const.MAXROOMS);
        } while (Global.rooms.get(rm).containInfo(RoomInfoEnum.ISGONE));
        return rm;
    }

    /*
     * draw_room:
     *	Draw a box around a room and lay down the floor for normal
     *	rooms; for maze rooms, draw maze.
     */
    static void draw_room(Room rp) {

        if (rp.containInfo(RoomInfoEnum.ISMAZE)) {
            do_maze(rp);
        } else {
            vert(rp, rp.r_pos.getX());                /* Draw left side */
            vert(rp, rp.r_pos.getX() + rp.r_max.getX() - 1);    /* Draw right side */
            horiz(rp, rp.r_pos.getY());                /* Draw top */
            horiz(rp, rp.r_pos.getY() + rp.r_max.getY() - 1);    /* Draw bottom */

            /*
             * Put the floor down
             */
            for (int y = rp.r_pos.getY() + 1; y < rp.r_pos.getY() + rp.r_max.getY() - 1; y++) {
                for (int x = rp.r_pos.getX() + 1; x < rp.r_pos.getX() + rp.r_max.getX() - 1; x++) {
                    Util.INDEX(y, x).p_ch = ObjectType.FLOOR;
                }
            }
        }
    }


    /*
     * do_maze:
     *	Dig a maze
     */
    static void do_maze(Room rp) {
        AbstractCoordinate pos = new Coordinate();

        for (int i = 0; i <= Const.NUMLINES / 3; i++) {
            for (int j = 0; j <= Const.NUMCOLS / 3; j++) {
                maze[i][j].used = false;
                maze[i][j].nexits = 0;
            }
        }

        Maxy = rp.r_max.getY();
        Maxx = rp.r_max.getX();
        Starty = rp.r_pos.getY();
        Startx = rp.r_pos.getX();
        int starty, startx;
        starty = (Util.rnd(rp.r_max.getY()) / 2) * 2;
        startx = (Util.rnd(rp.r_max.getX()) / 2) * 2;
        pos.setY(starty + Starty);
        pos.setX(startx + Startx);
        Passage.putpass(pos);
        dig(starty, startx);
    }


    /*
     * dig:
     *	Dig out from around where we are now, if possible
     */

    static void dig(int y, int x) {
        AbstractCoordinate[] del = new Coordinate[4];
        // もしかしたらx,y逆かも
        del[0].setX(2);
        del[0].setY(0);
        del[1].setX(-2);
        del[1].setY(0);
        del[2].setX(0);
        del[2].setY(2);
        del[3].setX(0);
        del[3].setY(-2);

        int nextx = 0;
        for (int nexty = 0; ; ) {
            int cnt = 0;
            for (int i = 0; i < 4; i++) {
                AbstractCoordinate cp = del[i];
                int newy = y + cp.getY();
                int newx = x + cp.getX();
                if (newy < 0 || newy > Maxy || newx < 0 || newx > Maxx) {
                    continue;
                }
                if ((Util.flat(new Coordinate(newx + Startx, newy + Starty)) & Const.F_PASS) != 0) {
                    continue;
                }
                if (Util.rnd(++cnt) == 0) {
                    nexty = newy;
                    nextx = newx;
                }
            }
            if (cnt == 0) {
                return;
            }
            accnt_maze(y, x, nexty, nextx);
            accnt_maze(nexty, nextx, y, x);
            AbstractCoordinate pos = new Coordinate();
            if (nexty == y) {
                pos.setY(y + Starty);
                if (nextx - x < 0) {
                    pos.setX(nextx + Startx + 1);
                } else {
                    pos.setX(nextx + Startx - 1);
                }
            } else {
                pos.setX(x + Startx);
                if (nexty - y < 0) {
                    pos.setY(nexty + Starty + 1);
                } else {
                    pos.setY(nexty + Starty - 1);
                }
            }
            Passage.putpass(pos);
            pos.setY(nexty + Starty);
            pos.setX(nextx + Startx);
            Passage.putpass(pos);
            dig(nexty, nextx);
        }
    }


    /*
     * accnt_maze:
     *	Account for maze exits
     */
    static void accnt_maze(int y, int x, int ny, int nx) {
        Spot sp;
        AbstractCoordinate cp;

        sp = maze[y][x];
        for (int i = 0; i < sp.nexits; i++) {
            cp = sp.exits[i];
            if (cp.equals(new Coordinate(nx, ny))) {
                return;
            }
        }
        sp.exits[sp.nexits - 1] = new Coordinate(nx, ny);
    }


    /*
     * vert:
     *	Draw a vertical line
     */
    static private void vert(Room rp, int startx) {
        int y;

        for (y = rp.r_pos.getY() + 1; y <= rp.r_max.getY() + rp.r_pos.getY() - 1; y++) {
            Util.INDEX(y, startx).p_ch = ObjectType.Vert;
        }
    }

    /*
     * horiz:
     *	Draw a horizontal line
     */
    static private void horiz(Room rp, int starty) {
        int x;

        for (x = rp.r_pos.getX(); x <= rp.r_pos.getX() + rp.r_max.getX() - 1; x++) {
            Util.INDEX(starty, x).p_ch = ObjectType.Horizon;
        }
    }


    /*
     * find_floor:
     *	Find a valid floor spot in this room.  If rp is NULL, then
     *	pick a new room each time around the loop.
     */
    public static boolean find_floor(Room rp, AbstractCoordinate cp, boolean limit, boolean monst) {

        boolean pickroom = (rp == null);

        ObjectType compchar = ObjectType.Initial;
        if (!pickroom) {
            compchar = (rp.containInfo(RoomInfoEnum.ISMAZE) ? ObjectType.PASSAGE : ObjectType.FLOOR);
        }
        for (; ; ) {
            if (limit) {
                return false;
            }
            if (pickroom) {
                rp = Global.rooms.get(rnd_room());
                compchar = (rp.containInfo(RoomInfoEnum.ISMAZE) ? ObjectType.PASSAGE : ObjectType.FLOOR);
            }
            cp = rnd_pos(rp);
            Place pp = Util.getPlace(cp);
            if (monst) {
                // 足元に何もない　かつ 歩けるとこ
                if (pp.p_monst == null && IOUtil.step_ok(pp.p_ch)) {
                    return true;
                }
            } else if (pp.p_ch == compchar) {
                // 歩けるとこ
                return false;
            }
        }
    }


    /*
     * rnd_pos:
     *	Pick a random spot in a room
     */
    static AbstractCoordinate rnd_pos(Room rp) {
        return rp.r_pos.add(new Coordinate(Util.rnd(rp.r_max.getX() - 2) + 1, Util.rnd(rp.r_max.getY() - 2) + 1));
    }


}
