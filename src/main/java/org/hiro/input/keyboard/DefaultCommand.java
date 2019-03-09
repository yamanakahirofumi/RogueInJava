package org.hiro.input.keyboard;

import org.hiro.Const;
import org.hiro.Global;
import org.hiro.IOUtil;
import org.hiro.New_Level;
import org.hiro.Pack;
import org.hiro.Potions;
import org.hiro.Wizard;
import org.hiro.character.Player;
import org.hiro.character.StateEnum;
import org.hiro.output.Display;
import org.hiro.things.Armor;
import org.hiro.things.ArmorEnum;
import org.hiro.things.ObjectType;
import org.hiro.things.Stick;
import org.hiro.things.Thing;
import org.hiro.things.Weapon;
import org.hiro.things.WeaponEnum;

public class DefaultCommand implements KeyboardCommand {

    @Override
    public void execute(Player player) {
    }

    public void execute(Player player, int ch) {
        Global.after = false;
        // #ifdef MASTER
        if (Global.wizard) {
            switch (ch) {
                case '|':
                    IOUtil.msg("@ %d,%d", String.valueOf(player.getPositionY()), String.valueOf(player.getPositionX()));
                    break;
                case 'C':
                    // create_obj(); //TODO: 後で
                    break;
                case '$':
                    IOUtil.msg("inpack = %d", player.getBaggageSize());
                    break;
                case ('G' & 037):
                    Pack.inventory(Global.lvl_obj, ObjectType.Initial);
                    break;
                case ('W' & 037):
                    Wizard.whatIs(player, false, 0);
                    break;
                case ('D' & 037):
                    player.upstairs();
                    New_Level.new_level(player);
                    break;
                case ('A' & 037):
                    player.downstairs();
                    New_Level.new_level(player);
                    break;
                case ('F' & 037):
                    // show_map(); // TODO: 後で
                    break;
                case ('T' & 037):
                    Wizard.teleport(player);
                    break;
                case ('E' & 037):
                    IOUtil.msg("food left: %d", Global.food_left);
                    break;
                case ('Q' & 037):
                    // add_pass();  // TODO: 後で
                    break;
                case ('X' & 037):
                    Potions.turn_see(player.containsState(StateEnum.SEEMONST));
                    break;
                case '~':
                    Thing item;
                    if ((item = Pack.get_item("charge", ObjectType.STICK)) != null) {
                        ((Stick) item).setTimes(10000);
                    }
                    break;
                case ('I' & 037): {
                    for (int i = 0; i < 9; i++) {
                        Potions.raise_level();
                    }
                    /*
                     * Give him a sword (+1,+1)
                     */
                    Weapon obj = new Weapon(WeaponEnum.TWOSWORD, 1);
                    obj.addDamagePlus();
                    Pack.add_pack(obj, true);
                    player.putOnWeapon(obj);
                    /*
                     * And his suit of armor
                     */
                    Armor armor = new Armor(ArmorEnum.PLATE_MAIL, -5, Const.ISKNOW);
                    player.putOnArmor(armor);
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
