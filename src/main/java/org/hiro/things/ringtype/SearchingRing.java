package org.hiro.things.ringtype;

import org.hiro.things.Ring;
import org.hiro.things.RingEnum;

import java.util.List;

public class SearchingRing extends Ring {
    public SearchingRing(){
        super();
        this._o_which = RingEnum.Searching.getValue();
    }

    public static boolean isInclude(List<Ring> rings){
        for(Ring r : rings){
            if(r instanceof SearchingRing){
                return true;
            }
        }
        return false;
    }

}
