package org.hiro.things;

import org.hiro.Const;
import org.hiro.Global;
import org.hiro.ThingMethod;
import org.hiro.things.sticktype.ChangeMonster;
import org.hiro.things.sticktype.Drain;
import org.hiro.things.sticktype.ElectricBolt;
import org.hiro.things.sticktype.FireBolt;
import org.hiro.things.sticktype.HasteMonster;
import org.hiro.things.sticktype.IceBolt;
import org.hiro.things.sticktype.InvalidateEffect;
import org.hiro.things.sticktype.InvisibleSeeing;
import org.hiro.things.sticktype.LightUp;
import org.hiro.things.sticktype.MagicMissile;
import org.hiro.things.sticktype.SlowMonster;
import org.hiro.things.sticktype.TeleportAway;
import org.hiro.things.sticktype.TeleportMe;

import java.util.Arrays;

public class ThingFactory {
    public static Thing create() {
        switch (Global.no_food > 3 ? 2 : ThingMethod.pick_one(Arrays.asList(Global.things), Const.NUMTHINGS)) {
            case 0:
                return new Potion();
            case 1:
                return new Scroll();
            case 2:
                return new Food();
            case 3:
                return new Weapon();
            case 4:
                return new Armor();
            case 5:
                return new Ring();
            case 6:
                return createStick();
            default:
                throw new RuntimeException("Bugs!!");
        }
    }

    private static Stick createStick(){
        StickEnum s = StickEnum.get(ThingMethod.pick_one(Arrays.asList(Global.ws_info), StickEnum.getMaxValue()));
        switch (s) {
            case LightUp:
                return new LightUp();
            case InvisibleSeeing:
                return new InvisibleSeeing();
            case ElectricBolt:
                return new ElectricBolt();
            case FireBolt:
                return new FireBolt();
            case IceBolt:
                return new IceBolt();
            case ChangeMoster:
                return new ChangeMonster();
            case MagicMissile:
                return new MagicMissile();
            case HasteMonster:
                return new HasteMonster();
            case SlowMonster:
                return new SlowMonster();
            case Drain:
                return new Drain();
            case TeleportAway:
                return new TeleportAway();
            case TeleportMe:
                return new TeleportMe();
            case InvalidateEffect:
                return new InvalidateEffect();
            case NoEffect:
            default:
                return new Stick();
        }
    }
}
