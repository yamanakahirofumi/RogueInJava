package org.hiro;

/*
 * Stuff about objects
 * オブジェクト関連のデータ
 */
public class Obj_info {
    String oi_name;
	int oi_prob;  // なりやすさ
	int oi_worth;  // 価値
	String oi_guess;
	boolean oi_know; // 知っているかどうか

	public Obj_info(String oi_name, int oi_prob,int oi_worth, String oi_guess, boolean oi_know ){
		this.oi_name = oi_name;
		this.oi_prob = oi_prob;
		this.oi_worth = oi_worth;
		this.oi_guess = oi_guess;
		this.oi_know = oi_know;
	}
}
