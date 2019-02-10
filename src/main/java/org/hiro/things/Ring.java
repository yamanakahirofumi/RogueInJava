package org.hiro.things;

import org.hiro.Const;
import org.hiro.Global;
import org.hiro.Obj_info;

public class Ring extends ThingImp {
    public Ring() {
        super();
        this._o_which = RingEnum.Adornment.getValue();
//        this._o_which = ThingMethod.pick_one(Arrays.asList(Global.ring_info));
    }

    @Override
    public boolean isMagic() {
        return true;
    }

    @Override
    public int getWorth() {
        Obj_info op = Global.ring_info[this._o_which];
        int worth = op.getWorth();
        if (!this.contains_o_flags(Const.ISKNOW)) {
            worth /= 2;
        }
        this.add_o_flags(Const.ISKNOW);
        op.know();
        return worth;
    }

    @Override
    public ObjectType getDisplay() {
        return ObjectType.RING;
    }
}
