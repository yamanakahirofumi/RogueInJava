package org.hiro.things.ringtype;

import org.hiro.things.Ring;
import org.hiro.things.RingEnum;

import java.util.List;

public class SeeInvisibleRing extends Ring {
    public SeeInvisibleRing(){
        super();
        this._o_which = RingEnum.SeeInvisible.getValue();
    }

    public static boolean isInclude(List<Ring> rings){
        for(Ring r : rings){
            if(r instanceof SeeInvisibleRing){
                return true;
            }
        }
        return false;
    }

}
