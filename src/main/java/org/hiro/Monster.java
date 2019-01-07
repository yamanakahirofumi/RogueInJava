package org.hiro;

/*
 * Array containing information on all the various types of monsters
 */

public class Monster {

    String m_name;			/* What to call the monster */
	int m_carry;			/* Probability of carrying something */
	@Deprecated
	int m_flags;			/* things about the monster */
	Stats m_stats;		/* Initial stats */

}
