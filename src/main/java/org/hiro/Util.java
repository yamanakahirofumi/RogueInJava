package org.hiro;

import org.hiro.map.Coord;
import org.hiro.things.ObjectType;
import org.hiro.things.RingEnum;
import org.hiro.things.ThingImp;

import java.util.Random;

public class Util {
    /*
     * rnd:
     *	Pick a very random number.
     */
    public static int rnd(int range) {
        Random rn = new Random();
        return range == 0 ? 0 : rn.nextInt(range);
    }

    // 左に5つシフトは、*32と同じMAXLINES * MAXCOLSで32*80
    static Place INDEX(int y, int x) {
        return Global.places.get((x << 5) + y);
    }

    static int flat(int y, int x) {
        return Global.places.get((x << 5) + y).p_flags;
    }

    static int GOLDCALC() {
        return rnd(50 + 10 * Global.level) + 2;
    }

    static int CCHAR(int x) {
        return x; // A_CHARTEXTは、文字を取り出すためのビットマスク
    }

    static boolean ISRING(int h, RingEnum r) {
        return Global.cur_ring[h] != null && Global.cur_ring[h]._o_which == r.getValue();
    }

    static boolean ISWEARING(RingEnum r) {
        return ISRING(Const.LEFT, r) || ISRING(Const.RIGHT, r);
    }


    /**
     * thing.containsState(StateEnum.flg)に変更
     */
    @Deprecated
    static boolean on(ThingImp thing, int flag) {
        return (thing._t_flags & flag) != 0;
    }

    /**
     * Coordのequals()に変更
     */
    @Deprecated
    static boolean ce(Coord a, Coord b) {
        return (a.x == b.x && a.y == b.y);
    }

//	#define moat(y,x)	 Global.places.get((x << 5) + y).p_monst

    // INDEX.p_ch
//	static int chat(int y, int x) {
//		return Global.places.get((x << 5) + y).p_ch
//	}

    static ObjectType winat(int y, int x) {
        if (Global.places.get((x << 5) + y).p_monst != null) {
            return ObjectType.get((char) Global.places.get((x << 5) + y).p_monst._t_disguise);

        } else {
            return Global.places.get((x << 5) + y).p_ch;
        }
    }

    static char CTRL(char c) {
        return (char) (c & 037);
    }

    static boolean ISMULT(ObjectType type) {
        return (type == ObjectType.POTION || type == ObjectType.SCROLL || type == ObjectType.FOOD);
    }


}
