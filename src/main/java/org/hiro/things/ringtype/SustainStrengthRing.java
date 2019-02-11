package org.hiro.things.ringtype;

import org.hiro.things.Ring;
import org.hiro.things.RingEnum;

import java.util.List;

public class SustainStrengthRing extends Ring {

    public SustainStrengthRing(){
        super();
        this._o_which = RingEnum.SustainStrength.getValue();
    }

    public static boolean isInclude(List<Ring> rings){
        for(Ring r : rings){
            if(r instanceof SustainStrengthRing){
                return true;
            }
        }
        return false;
    }

}
