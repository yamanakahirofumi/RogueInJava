package org.hiro;

import org.hiro.character.Human;
import org.hiro.map.Coordinate;
import org.hiro.things.Food;
import org.hiro.things.ObjectType;
import org.hiro.things.Potion;
import org.hiro.things.RingEnum;
import org.hiro.things.Scroll;
import org.hiro.things.Thing;

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
    public static Place getPlace(Coordinate c){
        return Global.places.get((c.getX() << 5) + c.getY());
    }

    public static Place INDEX(int y, int x) {
        return Global.places.get((x << 5) + y);
    }

    public static int flat(Coordinate c) {
        return getPlace(c).p_flags;
    }

    static int GOLDCALC() {
        return rnd(50 + 10 * Human.instance.getLevel()) + 2;
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
    static boolean on(Thing thing, int flag) {
        // return (thing.containsState(flag);
        throw new UnsupportedOperationException("使わないで");
    }

    /**
     * Coordinateのequals()に変更
     */
    @Deprecated
    static boolean ce(Coordinate a, Coordinate b) {
        return false;
    }

//	#define moat(y,x)	 Global.places.get((x << 5) + y).p_monst

    // INDEX.p_ch
//	static int chat(int y, int x) {
//		return Global.places.get((x << 5) + y).p_ch
//	}

    public static ObjectType winat(Coordinate coordinate) {
        if (getPlace(coordinate).p_monst != null) {
            return ObjectType.get((char) getPlace(coordinate).p_monst._t_disguise);

        } else {
            return getPlace(coordinate).p_ch;
        }
    }

    static char CTRL(char c) {
        return (char) (c & 037);
    }

    // 違う判定がよいかも
    static boolean ISMULT(Thing type) {
        return (type instanceof Potion || type instanceof Scroll || type instanceof Food);
    }


}
