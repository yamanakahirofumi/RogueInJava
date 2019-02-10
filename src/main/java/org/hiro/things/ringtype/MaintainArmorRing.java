package org.hiro.things.ringtype;

import org.hiro.things.Ring;
import org.hiro.things.RingEnum;

import java.util.List;

public class MaintainArmorRing extends Ring {
    public MaintainArmorRing(){
        super();
        this._o_which = RingEnum.MaintainArmor.getValue();
    }

    public static boolean isInclude(List<Ring> rings){
        for(Ring r : rings){
            if(r instanceof MaintainArmorRing){
                return true;
            }
        }
        return false;
    }

}
