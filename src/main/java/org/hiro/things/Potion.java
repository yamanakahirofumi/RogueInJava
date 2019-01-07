package org.hiro.things;

import org.hiro.Obj_info;
import org.hiro.ThingMethod;

import java.util.List;

public class Potion extends ThingImp {

    Potion() {
        super();
        this._o_type = ObjectType.POTION;
        this._o_which = ThingMethod.pick_one(getTypes(), PotionEnum.getMaxValue());
    }

    /**
     * @return
     */
    public List<Obj_info> getTypes() {
        String filename = "potions.csv";
        return this.getTypes(filename);
    }
}
