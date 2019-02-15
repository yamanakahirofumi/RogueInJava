package org.hiro.input.keyboard.equipment;

import org.hiro.Global;
import org.hiro.Misc;
import org.hiro.Pack;
import org.hiro.StickMethod;
import org.hiro.character.Human;
import org.hiro.input.keyboard.KeyboardCommand;
import org.hiro.things.ObjectType;
import org.hiro.things.Thing;

public class ZapWandCommand implements KeyboardCommand {

    @Override
    public void execute() {
        if (Misc.get_dir()) {
            Thing obj = Pack.get_item("zap with", ObjectType.STICK);
            StickMethod.do_zap(Human.instance, obj);
        } else {
            Global.after = false;
        }
    }
}
