package org.hiro.things;

import org.hiro.Const;
import org.hiro.Global;
import org.hiro.Obj_info;
import org.hiro.Util;
import org.hiro.character.Player;

public class Stick extends ThingImp {
    private String power;
    private String hurlPower;
    private int times;

    public Stick(){
        this._o_which = StickEnum.NoEffect.getValue();
        this.times = Util.rnd(5) + 3;
        fix_stick();
//				boolean MASTER = false;
//				if (MASTER) {
//					break;
//					default:
//					debug("Picked a bad kind of object");
//					wait_for(stdscr, ' ');
//				}

    }

    /*
     * fix_stick:
     *	Set up a new stick
     */
    private void fix_stick() {
        if ("staff".equals(Global.ws_type[this._o_which])) {
            this.power = "2x3";
        } else {
            this.power = "1x1";
        }
        this.hurlPower = "1x1";
    }

    @Override
    public boolean isMagic(){
        return true;
    }

    @Override
    public int getWorth(){
        Obj_info op = Global.ws_info[this._o_which];
        int worth = op.getWorth();
        worth += 20 * this.times;
        if (!this.contains_o_flags(Const.ISKNOW)) {
            worth /= 2;
        }
        this.add_o_flags(Const.ISKNOW);
        op.know();
        return worth;
    }

    @Override
    public ObjectType getDisplay() {
        return ObjectType.STICK;
    }

    public void shake(Player player){
        this.use();
    }

    public void use(){
        this.times--;
    }

    public boolean isUse(){
        return this.times > 0;
    }
}
