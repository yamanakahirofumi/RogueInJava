package org.hiro.things.ringtype;

import org.hiro.Const;
import org.hiro.things.Ring;
import org.hiro.things.RingEnum;

import java.util.List;

public class TeleportationRing extends Ring {
    public TeleportationRing(){
        super();
        this._o_which = RingEnum.Teleportation.getValue();
        this.add_o_flags(Const.ISCURSED);
    }

    public static boolean isInclude(List<Ring> rings){
        for(Ring r : rings){
            if(r instanceof TeleportationRing){
                return true;
            }
        }
        return false;
    }

}
