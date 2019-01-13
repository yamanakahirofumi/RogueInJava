package org.hiro;

import org.hiro.character.Human;
import org.hiro.character.StateEnum;
import org.hiro.map.Coordinate;
import org.hiro.output.Display;
import org.hiro.things.ObjectType;
import org.hiro.things.ThingFactory;
import org.hiro.things.ThingImp;

import java.util.ArrayList;

/**
 * Dig and draw a new level
 */
public class New_Level {

    static final int TREAS_ROOM = 20; /* one chance in TREAS_ROOM for a treasure room */


    static void new_level() {

        Human.instance.removeState(StateEnum.ISHELD);    /* unhold when you go down just in case */
        if (Human.instance.getLevel() > Global.max_level) {
            Global.max_level = Human.instance.getLevel();
        }
        /*
         * Clean things off from last level
         * 直近の階層のデータ初期化
         */
        for (int j = 0; j < Const.MAXCOLS * Const.MAXLINES; j++) {
            Place pp = Global.places.get(j);
            pp.p_ch = ObjectType.Blank;
            pp.p_flags = Const.F_REAL;
            pp.p_monst = null;
        }
        Display.clear();
        /*
         * Free up the monsters on the last level
         * 直近の階層のモンスター(とその持ち物なのだけども、こちらはjavaなのでgcまかせ)を削除
         */
        Global.mlist = new ArrayList<>();

        /*
         * Throw away stuff left on the previous level (if anything)
         */
        Global.lvl_obj = new ArrayList<>();
        DrawRoom.do_rooms();                /* Draw rooms */
        Passage.do_passages();            /* Draw passages */
        Global.no_food++;
        put_things();            /* Place objects (if any) */
        /*
         * Place the traps
         */
        if (Util.rnd(10) < Human.instance.getLevel()) {
            Global.ntraps = Util.rnd(Human.instance.getLevel() / 4) + 1;
            if (Global.ntraps > Const.MAXTRAPS) {
                Global.ntraps = Const.MAXTRAPS;
            }
            int i = Global.ntraps;
            while (i-- != 0) {
                /*
                 * not only wouldn't it be NICE to have traps in mazes
                 * (not that we care about being nice), since the trap
                 * number is stored where the passage number is, we
                 * can't actually do it.
                 */
                do {
                    DrawRoom.find_floor(null, Global.stairs, false, false);
                } while (Util.getPlace(Global.stairs).p_ch.getValue() != ObjectType.FLOOR.getValue() &&
                        (Util.flat(Global.stairs) & Const.F_REAL) != 0);

                int sp = Util.flat(Global.stairs);
                sp &= ~(Const.F_REAL | Const.F_TMASK);
                sp |= Util.rnd(Const.NTRAPS);
            }
        }
        /*
         * Place the staircase down.
         */
        DrawRoom.find_floor(null, Global.stairs, false, false);
        Util.getPlace(Global.stairs).p_ch = ObjectType.STAIRS;
        Global.seenstairs = false;

        for (ThingImp tp : Global.mlist) {
            tp.t_room = Chase.roomin(tp._t_pos);
        }

        DrawRoom.find_floor(null, Global.player._t_pos, false, true);
        Rooms.enter_room(Global.player._t_pos);
        Display.mvaddch(Global.player._t_pos.y, Global.player._t_pos.x, ObjectType.PLAYER.getValue());
        if (Human.instance.containsState(StateEnum.SEEMONST)) {
            Potions.turn_see(false);
        }
        if (Human.instance.containsState(StateEnum.ISHALU)) {
            Daemons.visuals();
        }
    }


    /*
     * put_things:
     *	Put potions and scrolls on this level
     */
    static void put_things() {

        /*
         * Once you have found the amulet, the only way to get new stuff is
         * go down into the dungeon.
         */
        Game game = Game.getInstance();
        if (game.isGoal() && Human.instance.getLevel() < Global.max_level) {
            return;
        }
        /*
         * check for treasure rooms, and if so, put it in.
         */
        if (Util.rnd(TREAS_ROOM) == 0) {
            treas_room();
        }
        /*
         * Do MAXOBJ attempts to put things on a level
         */
        ThingImp obj;
        for (int i = 0; i < Const.MAXOBJ; i++) {
            if (Util.rnd(100) < 36) {
                /*
                 * Pick a new object and link it in the list
                 */
                obj = (ThingImp) ThingFactory.create();
                Global.lvl_obj.add(obj);
                /*
                 * Put it somewhere
                 */
                DrawRoom.find_floor(null, obj._o_pos, false, false);
                Util.getPlace(obj._o_pos).p_ch = obj._o_type;
            }
        }
        /*
         * If he is really deep in the dungeon and he hasn't found the
         * amulet yet, put it somewhere on the ground
         */
        if (Human.instance.getLevel() >= Const.AMULETLEVEL && !game.isGoal()) {
            obj = new ThingImp();
            Global.lvl_obj.add(obj);
            obj._o_hplus = 0;
            obj._o_dplus = 0;
            obj._o_damage = "0x0";
            obj._o_hurldmg = "0x0";
            obj._o_arm = 11;
            obj._o_type = ObjectType.AMULET;
            /*
             * Put it somewhere
             */
            DrawRoom.find_floor(null, obj._o_pos, false, false);
            Util.getPlace(obj._o_pos).p_ch = ObjectType.AMULET;
        }
    }


    static final int MAXTREAS = 10; /* maximum number of treasures in a treasure room */
    static final int MINTREAS = 2;    /* minimum number of treasures in a treasure room */

    static final int MAXTRIES = 10;  /* max number of tries to put down a monster */

    /*
     * treas_room:
     *	Add a treasure room
     */
    static void treas_room() {

        Room rp = Global.rooms.get(DrawRoom.rnd_room());
        int spots = (rp.r_max.y - 2) * (rp.r_max.x - 2) - MINTREAS;
        if (spots > (MAXTREAS - MINTREAS)) {
            spots = (MAXTREAS - MINTREAS);
        }
        int nm;
        int num_monst = nm = Util.rnd(spots) + MINTREAS;
        ThingImp tp;
        Coordinate mp = new Coordinate();
        while (nm-- != 0) {
            DrawRoom.find_floor(rp, mp, 2 * MAXTRIES != 0, false);
            tp = new ThingImp();
            tp._o_pos = mp;
            Global.lvl_obj.add(tp);
            Util.getPlace(mp).p_ch = tp._o_type;
        }

        /*
         * fill up room with monsters from the next level down
         */

        if ((nm = Util.rnd(spots) + MINTREAS) < num_monst + 2) {
            nm = num_monst + 2;
        }
        spots = (rp.r_max.y - 2) * (rp.r_max.x - 2);
        if (nm > spots) {
            nm = spots;
        }
        Human.instance.upstairs();
        while (nm-- != 0) {
            spots = 0;
            if (DrawRoom.find_floor(rp, mp, MAXTRIES != 0, true)) {
                tp = new ThingImp();
                Monst.new_monster(tp, Monst.randmonster(false), mp);
                tp.addState(StateEnum.ISMEAN);    /* no sloughers in THIS room */
                Monst.give_pack(tp);
            }
        }
        Human.instance.downstairs();
    }

}
