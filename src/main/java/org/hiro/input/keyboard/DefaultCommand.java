package org.hiro.input.keyboard;

import org.hiro.Const;
import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.New_Level;
import org.hiro.Pack;
import org.hiro.Potions;
import org.hiro.Wizard;
import org.hiro.character.Human;
import org.hiro.character.StateEnum;
import org.hiro.output.Display;
import org.hiro.things.Armor;
import org.hiro.things.ArmorEnum;
import org.hiro.things.ObjectType;
import org.hiro.things.ThingImp;
import org.hiro.things.Weapon;
import org.hiro.things.WeaponEnum;

public class DefaultCommand implements KeyboardCommand {

    @Override
    public void execute() {
    }

    public void execute(int ch) {
    Global.after = false;
        // #ifdef MASTER
        if (Global.wizard) {
            switch (ch) {
                case '|':
                    IOUtil.msg("@ %d,%d", String.valueOf(Global.player._t_pos.y), String.valueOf(Global.player._t_pos.x));
                    break;
                case 'C':
                    // create_obj(); //TODO: 後で
                    break;
                case '$':
                    IOUtil.msg("inpack = %d", Human.instance.getBaggageSize());
                    break;
                case ('G' & 037):
                    Pack.inventory(Global.lvl_obj, ObjectType.Initial);
                    break;
                case ('W' & 037):
                    Wizard.whatis(false, 0);
                    break;
                case ('D' & 037):
                    Human.instance.upstairs();
                    New_Level.new_level();
                    break;
                case ('A' & 037):
                    Human.instance.downstairs();
                    New_Level.new_level();
                    break;
                case ('F' & 037):
                    // show_map(); // TODO: 後で
                    break;
                case ('T' & 037):
                    Wizard.teleport();
                    break;
                case ('E' & 037):
                    IOUtil.msg("food left: %d", Global.food_left);
                    break;
                case ('Q' & 037):
                    // add_pass();  // TODO: 後で
                    break;
                case ('X' & 037):
                    Potions.turn_see(Human.instance.containsState(StateEnum.SEEMONST));
                    break;
                case '~': {
                    ThingImp item;
                    if ((item = Pack.get_item("charge", ObjectType.STICK)) != null) {
                        item._o_arm = 10000;
                    }
                }
                break;
                case ('I' & 037): {
                    int i;
                    ThingImp obj;

                    for (i = 0; i < 9; i++) {
                        Potions.raise_level();
                    }
                    /*
                     * Give him a sword (+1,+1)
                     */
                    obj = new Weapon(WeaponEnum.TWOSWORD, 1);
                    obj._o_dplus = 1;
                    Pack.add_pack(obj, true);
                    Human.instance.putOnWeapon((Weapon) obj);
                    /*
                     * And his suit of armor
                     */
                    Armor armor = new Armor(ArmorEnum.PLATE_MAIL, -5, Const.ISKNOW);
                    Human.instance.putOnArmor(armor);
                    Pack.add_pack(armor, true);
                }
                break;
                case '*':
                    // pr_list(); // 後で
                    break;
                default:
                    illcom(ch);
            }
        } else {
            // #endif
            illcom(ch);
        }

    }

    /*
     * illcom:
     *	What to do with an illegal command
     */
    private void illcom(int ch) {
        Global.save_msg = false;
        Global.count = 0;
        IOUtil.msg("illegal command '%s'", Display.unctrl(ch));
        Global.save_msg = true;
    }


}
