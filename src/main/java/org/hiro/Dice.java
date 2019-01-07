package org.hiro;

public class Dice {

	/*
	 * roll:
	 *	Roll a number of dice
	 */
	static int roll(int number, int sides)
	{
		int total = 0;
		while (number-- > 0) {
			total += Util.rnd(sides) + 1;
		}
		return total;
	}

}
