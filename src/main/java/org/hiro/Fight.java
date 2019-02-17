package org.hiro;

import org.hiro.character.Human;
import org.hiro.character.Player;
import org.hiro.character.StateEnum;
import org.hiro.map.Coordinate;
import org.hiro.output.Display;
import org.hiro.things.Gold;
import org.hiro.things.Thing;
import org.hiro.things.ThingImp;
import org.hiro.things.Weapon;
import org.hiro.things.ringtype.AddDamageRing;
import org.hiro.things.ringtype.DexterityRing;
import org.hiro.things.ringtype.ProtectionRing;
import org.hiro.things.ringtype.SustainStrengthRing;

import java.util.List;

public class Fight {

    /*
     * set_mname:
     *	return the monster name for the given monster
     */
    static String set_mname(ThingImp tp) {
        char ch;
        String mname;
        String tbuf = "the ";

        if (!Chase.see_monst(tp) && !Human.instance.containsState(StateEnum.SEEMONST))
            return (Global.terse ? "it" : "something");
        else if (Human.instance.containsState(StateEnum.ISHALU)) {
            Display.move(tp._t_pos);
            ch = (char) Util.CCHAR(Display.inch());
            if (!Character.isUpperCase(ch)) {
                ch = (char) Util.rnd(26);
            } else {
                ch -= 'A';
            }
            mname = Global.monsters[ch].m_name;
        } else {
            mname = Global.monsters[tp.getType() - 'A'].m_name;
        }
        return tbuf + mname;
    }

    /*
     * swing:
     *	Returns true if the swing hits
     */
    static boolean swing(int at_lvl, int op_arm, int wplus) {
        int res = Util.rnd(20);
        int need = (20 - at_lvl) - op_arm;

        return (res + wplus >= need);
    }


    /*
     * fight:
     *	The player attacks the monster.
     */
    static boolean fight(Coordinate mp, Weapon weap, boolean thrown) {
        ThingImp tp;

        /*
         * Find the monster we want to fight
         */
        if ((tp = Util.getPlace(mp).p_monst) == null) {
            boolean MASTER = false;
            if (MASTER) {
                // debug("Fight what @ %d,%d", mp . y, mp . x);
            }
            return false;
        }
        /*
         * Since we are fighting, things are not quiet so no healing takes
         * place.
         */
        Global.count = 0;
        Global.quiet = 0;
        Chase.runto(mp);
        /*
         * Let him know it was really a xeroc (if it was one).
         */
        char ch = '\0';
        if (tp.getType() == 'X' && tp._t_disguise != 'X' && !Human.instance.containsState(StateEnum.ISBLIND)) {
            tp._t_disguise = 'X';
            if (Human.instance.containsState(StateEnum.ISHALU)) {
                ch = (char) (Util.rnd(26) + 'A');
                Display.mvaddch(tp._t_pos, ch);
            }
            // msg(Misc.choose_str("heavy!  That's a nasty critter!",
            //        "wait!  That's a xeroc!"));
            if (!thrown)
                return false;
        }
        String mname = set_mname(tp);
        boolean did_hit = false;
        Global.has_hit = (Global.terse && !Global.to_death);
        if (roll_em(Global.player, tp, weap, thrown)) {
            did_hit = false;
            if (thrown) {
                thunk(weap, mname, Global.terse);
            } else {
                hit(null, mname, Global.terse);
            }
            if (Human.instance.containsState(StateEnum.CANHUH)) {
                did_hit = true;
                tp.addState(StateEnum.ISHUH);
                Human.instance.removeState(StateEnum.CANHUH);
                IOUtil.endmsg();
                Global.has_hit = false;
                IOUtil.msg("your hands stop glowing %s", Init.pick_color("red"));
            }
            if (tp._t_stats.s_hpt <= 0) {
                killed(tp, true);
            } else if (did_hit && !Human.instance.containsState(StateEnum.ISBLIND)) {
                IOUtil.msg("%s appears confused", mname);
            }
            did_hit = true;
        } else if (thrown) {
            bounce(weap, mname, Global.terse);
        } else {
            miss(null, mname, Global.terse);
        }
        return did_hit;
    }


    /*
     * hit:
     *	Print a message to indicate a succesful hit
     */
    static void hit(String er, String ee, boolean noend) {
        String[] h_names = {        /* strings for hitting */
                " scored an excellent hit on ",
                " hit ",
                " have injured ",
                " swing and hit ",
                " scored an excellent hit on ",
                " hit ",
                " has injured ",
                " swings and hits "
        };

        if (Global.to_death) {
            return;
        }
        IOUtil.addmsg(prname(er, true));
        String s;
        if (Global.terse) {
            s = " hit";
        } else {
            int i = Util.rnd(4);
            if (er != null) {
                i += 4;
            }
            s = h_names[i];
        }
        IOUtil.addmsg(s);
        if (!Global.terse) {
            IOUtil.addmsg(prname(ee, false));
        }
        if (!noend) {
            IOUtil.endmsg();
        }
    }

    /*
     * prname:
     *	The print name of a combatant
     */
    static String prname(String mname, boolean upper) {
        String tbuf;
        if (mname == null || mname.length() < 1) {
            tbuf = "you";
        } else {
            tbuf = mname;
        }
        if (upper) {
            tbuf = Character.toUpperCase(tbuf.charAt(0)) + tbuf.substring(1);
        }
        return tbuf;
    }

    /*
     * killed:
     *	Called to put a monster to death
     */
    public static void killed(ThingImp tp, boolean pr) {

        Human.instance.addExperience(tp._t_stats.s_exp);

        /*
         * If the monster was a venus flytrap, un-hold him
         */
        switch (tp.getType()) {
            case 'F':
                Human.instance.removeState(StateEnum.ISHELD);
                Global.vf_hit = 0;
                break;
            case 'L': {
                if (WeaponMethod.fallpos(tp._t_pos, tp.t_room.r_gold) && Human.instance.getLevel() >= Global.max_level) {
                    Gold gold = new Gold(Human.instance.getLevel());
                    if (Monst.save(Const.VS_MAGIC)) {
                        gold.addGold(Util.GOLDCALC() + Util.GOLDCALC() + Util.GOLDCALC() + Util.GOLDCALC());
                    }
                    tp.addItem(gold);
                }
            }
        }
        /*
         * Get rid of the monster.
         */
        String mname = set_mname(tp);
        remove_mon(tp._t_pos, tp, true);
        if (pr) {
            if (Global.has_hit) {
                IOUtil.addmsg(".  Defeated ");
                Global.has_hit = false;
            } else {
                if (!Global.terse) {
                    IOUtil.addmsg("you have ");
                }
                IOUtil.addmsg("defeated ");
            }
            IOUtil.msg(mname);
        }
        /*
         * Do adjustments if he went up a level
         */
        Misc.check_level();
        if (Global.fight_flush) {
            Mach_dep.flush_type();
        }
    }


    /*
     * remove_mon:
     *	Remove a monster from the screen
     */
    static void remove_mon(Coordinate mp, ThingImp tp, boolean waskill) {
        ThingImp nexti;

        for (ThingImp obj : tp.getBaggage()){
            obj._o_pos = tp._t_pos;
            tp.removeItem(obj);
            if (waskill) {
                WeaponMethod.fall(obj, false);
            }
        }
        Util.getPlace(mp).p_monst = null;
        Display.mvaddch(mp, (char) tp._t_oldch);
        Global.mlist.remove(tp);
        if (tp.containsState(StateEnum.ISTARGET)) {
            Global.kamikaze = false;
            Global.to_death = false;
            if (Global.fight_flush) {
                Mach_dep.flush_type();
            }
        }
    }

    /*
     * bounce:
     *	A missile misses a monster
     */
    static void bounce(ThingImp weap, String mname, boolean noend) {
        if (Global.to_death) {
            return;
        }
        if (weap instanceof Weapon) {
            IOUtil.addmsg("the %s misses ", Global.weap_info[weap._o_which].getName());
        } else {
            IOUtil.addmsg("you missed ");
        }
        IOUtil.addmsg(mname);
        if (!noend) {
            IOUtil.endmsg();
        }
    }


    /*
     * thunk:
     *	A missile hits a monster
     */
    static void thunk(ThingImp weap, String mname, boolean noend) {
        if (Global.to_death) {
            return;
        }
        if (weap instanceof Weapon) {
            IOUtil.addmsg("the %s hits ", Global.weap_info[weap._o_which].getName());
        } else {
            IOUtil.addmsg("you hit ");
        }
        IOUtil.addmsg("%s", mname);
        if (!noend)
            IOUtil.endmsg();
    }

    /*
     * miss:
     *	Print a message to indicate a poor swing
     */
    static void miss(String er, String ee, boolean noend) {

        String[] m_names = {        /* strings for missing */
                " miss",
                " swing and miss",
                " barely miss",
                " don't hit",
                " misses",
                " swings and misses",
                " barely misses",
                " doesn't hit",
        };

        if (Global.to_death) {
            return;
        }
        IOUtil.addmsg(prname(er, true));
        int i;
        if (Global.terse) {
            i = 0;
        } else {
            i = Util.rnd(4);
        }
        if (er != null) {
            i += 4;
        }
        IOUtil.addmsg(m_names[i]);
        if (!Global.terse) {
            IOUtil.addmsg(" %s", prname(ee, false));
        }
        if (!noend) {
            IOUtil.endmsg();
        }
    }

    /*
     * roll_em:
     *	Roll several attacks
     */
    static boolean roll_em(ThingImp thatt, ThingImp thdef, Weapon weap, boolean hurl) {
        /*
         * adjustments to hit probabilities due to strength
         */
        int[] str_plus = {
                -7, -6, -5, -4, -3, -2, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1,
                1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3,
        };
        /*
         * adjustments to damage done due to strength
         */
        int[] add_dam = {
                -7, -6, -5, -4, -3, -2, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 3,
                3, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 6
        };
        boolean did_hit = false;
        int hplus= 0;
        int dplus = 0;

        Stats att = thatt._t_stats;
        Stats def = thdef._t_stats;
        String cp = att.s_dmg;
        if (weap != null) {
            hplus = weap.getHitPlus();
            dplus = weap.getDamagePlus();
            List<Weapon> curWeapons = Human.instance.getWeapons();
            if (curWeapons.size() > 0 && weap == curWeapons.get(0)) {
                if (Global.cur_ring[Const.LEFT] instanceof AddDamageRing) {
                    dplus += ((AddDamageRing)Global.cur_ring[Const.LEFT]).getDamage();
                } else if (Global.cur_ring[Const.LEFT] instanceof DexterityRing) {
                    hplus += ((DexterityRing)Global.cur_ring[Const.LEFT]).getDexterity();
                }
                if (Global.cur_ring[Const.RIGHT] instanceof AddDamageRing) {
                    dplus += ((AddDamageRing)Global.cur_ring[Const.RIGHT]).getDamage();
                } else if (Global.cur_ring[Const.RIGHT] instanceof DexterityRing) {
                    hplus += ((DexterityRing)Global.cur_ring[Const.RIGHT]).getDexterity();
                }
            }
            cp = weap.getDamage();
            if (hurl) {
                if (weap.contains_o_flags(Const.ISMISL) && curWeapons.size() > 0 &&
                        curWeapons.get(0)._o_which == weap._o_launch) {
                    cp = weap._o_hurldmg;
                    hplus += curWeapons.get(0).getHitPlus();
                    dplus += curWeapons.get(0).getDamagePlus();
                } else if (weap._o_launch < 0) {
                    cp = weap._o_hurldmg;
                }
            }
        }
        /*
         * If the creature being attacked is not running (alseep or held)
         * then the attacker gets a plus four bonus to hit.
         */
        if (!thdef.containsState(StateEnum.ISRUN))
            hplus += 4;
        int def_arm = def.s_arm;
        if (def == Global.player._t_stats) {
            if (Human.instance.isEquippedArmor()) {
                def_arm = Human.instance.getArmor().getDefence();
            }
            if (Global.cur_ring[Const.LEFT] instanceof ProtectionRing) {
                def_arm -= ((ProtectionRing)Global.cur_ring[Const.LEFT]).getDefence();
            }
            if (Global.cur_ring[Const.RIGHT] instanceof ProtectionRing) {
                def_arm -= ((ProtectionRing)Global.cur_ring[Const.RIGHT]).getDefence();
            }
        }
        boolean MASTER = false;
        while (cp != null && cp.length() < 1) {
            int ndice;
            try {
                ndice = Integer.valueOf(cp);
            } catch (NumberFormatException e) {
                ndice = 0;
            }
            if (cp.indexOf('x') == -1) {
                break;
            } else {
                cp = cp.substring(cp.indexOf('x'));
            }
            int nsides;
            try {
                cp = cp.substring(1);
                nsides = Integer.valueOf(cp);
            } catch (NumberFormatException e) {
                nsides = 0;
            }
            if (swing(att.s_lvl, def_arm, hplus + str_plus[att.s_str])) {
                int proll;

                proll = Dice.roll(ndice, nsides);
                if (MASTER) {
                    if (ndice + nsides > 0 && proll <= 0) {
                        // debug("Damage for %dx%d came out %d, dplus = %d, add_dam = %d, def_arm = %d", ndice, nsides, proll, dplus, add_dam[att.s_str], def_arm);
                    }
                }
                int damage = dplus + proll + add_dam[att.s_str];
                def.s_hpt -= Math.max(0, damage);
                did_hit = true;
            }
            if (cp.indexOf('/') == -1) {
                break;
            } else {
                cp = cp.substring(cp.indexOf('/'));
            }
            cp = cp.substring(1);
        }
        return did_hit;
    }

    /*
     * attack:
     *	The monster attacks the player
     */
    static int attack(Player player, ThingImp mp) {
        String mname;
        int oldhp;

        /*
         * Since this is an attack, stop running and any healing that was
         * going on at the time.
         */
        Global.running = false;
        Global.count = 0;
        Global.quiet = 0;
        if (Global.to_death && !mp.containsState(StateEnum.ISTARGET)) {
            Global.to_death = false;
            Global.kamikaze = false;
        }
        if (mp.getType() == 'F') {
            Global.vf_hit = Integer.valueOf(mp._t_stats.s_dmg);
        }
        if (mp.getType() == 'X' && mp._t_disguise != 'X' && !player.containsState(StateEnum.ISBLIND)) {
            mp._t_disguise = 'X';
            if (player.containsState(StateEnum.ISHALU)) {
                Display.mvaddch(mp._t_pos, (char) (Util.rnd(26) + 'A'));
            }
        }
        mname = set_mname(mp);
        oldhp = player.getHp();
        if (roll_em(mp, Global.player, null, false)) {
            if (mp.getType() != 'I') {
                if (Global.has_hit) {
                    IOUtil.addmsg(".  ");
                }
                hit(mname, null, false);
            } else if (Global.has_hit) {
                IOUtil.endmsg();
            }
            Global.has_hit = false;
            if (player.getHp() <= 0) {
                Rip.death(mp.getType());    /* Bye bye life ... */
            } else if (!Global.kamikaze) {
                oldhp -= player.getHp();
                if (oldhp > Global.max_hit) {
                    Global.max_hit = oldhp;
                }
                if (player.getHp() <= Global.max_hit) {
                    Global.to_death = false;
                }
            }
            if (!mp.containsState(StateEnum.ISCANC)) {
                switch (mp.getType()) {
                    case 'A':
                        /*
                         * If an aquator hits, you can lose armor class.
                         */
                        Move.rust_armor(player);
                        break;
                    case 'I':
                        /*
                         * The ice monster freezes you
                         */
                        player.removeState(StateEnum.ISRUN);
                        if (Global.no_command == 0) {
                            IOUtil.addmsg("you are frozen");
                            if (!Global.terse) {
                                IOUtil.addmsg(" by the %s", mname);
                            }
                            IOUtil.endmsg();
                        }
                        Global.no_command += Util.rnd(2) + 2;
                        if (Global.no_command > Const.BORE_LEVEL)
                            Rip.death('h');
                        break;
                    case 'R':
                        /*
                         * Rattlesnakes have poisonous bites
                         */
                        if (!Monst.save(Const.VS_POISON)) {
                            if (!SustainStrengthRing.isInclude(player.getRings())) {
                                Misc.chg_str(-1);
                                if (!Global.terse) {
                                    IOUtil.msg("you feel a bite in your leg and now feel weaker");
                                } else {
                                    IOUtil.msg("a bite has weakened you");
                                }
                            } else if (!Global.to_death) {
                                if (!Global.terse) {
                                    IOUtil.msg("a bite momentarily weakens you");
                                } else {
                                    IOUtil.msg("bite has no effect");
                                }
                            }
                        }
                        break;
                    case 'W':
                    case 'V':
                        /*
                         * Wraiths might drain energy levels, and Vampires
                         * can steal max_hp
                         */
                        if (Util.rnd(100) < (mp.getType() == 'W' ? 15 : 30)) {
                            int fewer;

                            if (mp.getType() == 'W') {
                                if (Global.player._t_stats.s_exp == 0) {
                                    Rip.death('W');        /* All levels gone */
                                }
                                if (--Global.player._t_stats.s_lvl == 0) {
                                    Global.player._t_stats.s_exp = 0;
                                    Global.player._t_stats.s_lvl = 1;
                                } else
                                    Global.player._t_stats.s_exp = Global.e_levels[Global.player._t_stats.s_lvl - 1] + 1;
                                fewer = Dice.roll(1, 10);
                            } else
                                fewer = Dice.roll(1, 3);
                            player.deleteHp(fewer);
                            Global.player._t_stats.s_maxhp -= fewer;
                            if (player.getHp() <= 0) {
                                Global.player._t_stats.s_hpt = 1;
                            }
                            if (player.getMaxHp() <= 0) {
                                Rip.death(mp.getType());
                            }
                            IOUtil.msg("you suddenly feel weaker");
                        }
                        break;
                    case 'F':
                        /*
                         * Venus Flytrap stops the poor guy from moving
                         */
                        player.addState(StateEnum.ISHELD);
                        mp._t_stats.s_dmg = ++Global.vf_hit + "x1";
                        if (--Global.player._t_stats.s_hpt <= 0) {
                            Rip.death('F');
                        }
                        break;
                    case 'L': {
                        /*
                         * Leperachaun steals some gold
                         */
                        int lastpurse;

                        lastpurse = Global.purse;
                        Global.purse -= Util.GOLDCALC();
                        if (!Monst.save(Const.VS_MAGIC))
                            Global.purse -= Util.GOLDCALC() + Util.GOLDCALC() + Util.GOLDCALC() + Util.GOLDCALC();
                        if (Global.purse < 0) {
                            Global.purse = 0;
                        }
                        remove_mon(mp._t_pos, mp, false);
                        mp = null;
                        if (Global.purse != lastpurse) {
                            IOUtil.msg("your purse feels lighter");
                        }
                    }
                    break;
                    case 'N': {
                        Thing steal;
                        int nobj = 0;

                        /*
                         * Nymph's steal a magic item, look through the pack
                         * and pick out one we like.
                         */
                        steal = null;
                        for (Thing obj : Global.player.getBaggage()) {
                            if (!player.isEquipped(obj)
                                    && obj.isMagic() && Util.rnd(++nobj) == 0) {
                                steal = obj;
                            }
                        }
                        if (steal != null) {
                            remove_mon(mp._t_pos, Util.getPlace(mp._t_pos).p_monst, false);
                            mp = null;
                            steal = Pack.leave_pack((ThingImp) steal, true, false);
                            IOUtil.msg("she stole %s!", ThingMethod.inventoryName(steal, true));
                        }
                    }
                    break;
                    default:
                        break;
                }
            }
        } else if (mp.getType() != 'I') {
            if (Global.has_hit) {
                IOUtil.addmsg(".  ");
                Global.has_hit = false;
            }
            if (mp.getType() == 'F') {
                player.deleteHp(Global.vf_hit);
                if (player.getHp() <= 0) {
                    Rip.death(mp.getType());    /* Bye bye life ... */
                }
            }
            miss(mname, null, false);
        }
        if (Global.fight_flush && !Global.to_death) {
            Mach_dep.flush_type();
        }
        Global.count = 0;
        IOUtil.status(player);
        if (mp == null)
            return (-1);
        else
            return (0);
    }


}
