package org.hiro.things.ringtype;

import org.hiro.things.Ring;
import org.hiro.things.RingEnum;

import java.util.List;

public class StealthRing extends Ring {
    public StealthRing(){
        super();
        this._o_which = RingEnum.Stealth.getValue();
    }

    public static boolean isInclude(List<Ring> rings){
        for(Ring r : rings){
            if(r instanceof StealthRing){
                return true;
            }
        }
        return false;
    }

}
