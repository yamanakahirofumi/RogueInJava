package org.hiro.things;

import org.hiro.Global;
import org.hiro.ThingMethod;
import org.hiro.things.scrolltype.Confuse;
import org.hiro.things.scrolltype.CreateMonster;
import org.hiro.things.scrolltype.EnchantArmor;
import org.hiro.things.scrolltype.EnchantWeapon;
import org.hiro.things.scrolltype.FoodDetection;
import org.hiro.things.scrolltype.HoldMonster;
import org.hiro.things.scrolltype.IdentifyArmor;
import org.hiro.things.scrolltype.IdentifyPotion;
import org.hiro.things.scrolltype.IdentifyRingOrStick;
import org.hiro.things.scrolltype.IdentifyScroll;
import org.hiro.things.scrolltype.IdentifyWeapon;
import org.hiro.things.scrolltype.MapScroll;
import org.hiro.things.scrolltype.ProtectArmor;
import org.hiro.things.scrolltype.RemoveCurse;
import org.hiro.things.scrolltype.Scare;
import org.hiro.things.scrolltype.Sleep;
import org.hiro.things.scrolltype.Teleportation;
import org.hiro.things.scrolltype.WakeUpMonster;
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
        switch (Global.no_food > 3 ? 2 : ThingMethod.pick_one(Arrays.asList(Global.things))) {
            case 0:
                return new Potion();
            case 1:
                return createScroll();
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

    private static Scroll createScroll() {
        ScrollEnum s = ScrollEnum.get(ThingMethod.pick_one(Arrays.asList(Global.scr_info)));
        switch (s) {
            case Confuse:
                return new Confuse();
            case MapScroll:
                return new MapScroll();
            case HoldMonster:
                return new HoldMonster();
            case Sleep:
                return new Sleep();
            case EnchantArmor:
                return new EnchantArmor();
            case IdentifyPotion:
                return new IdentifyPotion();
            case IdentifyScroll:
                return new IdentifyScroll();
            case IdentifyWeapon:
                return new IdentifyWeapon();
            case IdentifyArmor:
                return new IdentifyArmor();
            case IdentifyRingOrStick:
                return new IdentifyRingOrStick();
            case Scare:
                return new Scare();
            case FoodDetection:
                return new FoodDetection();
            case Teleportation:
                return new Teleportation();
            case EnchantWeapon:
                return new EnchantWeapon();
            case CreateMonster:
                return new CreateMonster();
            case RemoveCurse:
                return new RemoveCurse();
            case WakeUpMonster:
                return new WakeUpMonster();
            case ProtectArmor:
                return new ProtectArmor();
            default:
                return new Scroll();
        }
    }

    private static Stick createStick() {
        StickEnum s = StickEnum.get(ThingMethod.pick_one(Arrays.asList(Global.ws_info)));
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
