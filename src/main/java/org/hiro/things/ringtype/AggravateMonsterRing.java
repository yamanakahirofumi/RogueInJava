package org.hiro.things.ringtype;

import org.hiro.Const;
import org.hiro.things.Ring;
import org.hiro.things.RingEnum;

import java.util.List;

public class AggravateMonsterRing extends Ring {
    public AggravateMonsterRing(){
        super();
        this._o_which = RingEnum.AggravateMonster.getValue();
        this.add_o_flags(Const.ISCURSED);
    }

    public static boolean isInclude(List<Ring> rings){
        for(Ring r : rings){
            if(r instanceof AggravateMonsterRing){
                return true;
            }
        }
        return false;
    }

}
