package org.hiro;

/*
 * Stuff about objects
 * オブジェクト関連のデータ
 */
public class Obj_info {
	/**
	 * 名前
	 *
	 * 昔はoi_name
	 */
    private String name;
	/**
	 * 生成されやすさ(取得しやすさ)
	 *
	 * Range: 0 ~ 100
	 * 昔はoi_prob
	 */
	private int probability;
	/**
	 * 生還したときの価値
	 *
	 * 昔はoi_worth
	 */
	private int worth;
	/**
	 * 未鑑定時につけられる名前
	 *
	 */
	String oi_guess;
	/**
	 * 鑑定済みフラグ
	 *
	 */
	boolean oi_know;

	public Obj_info(String name, int probability, int worth){
		this.name = name;
		this.probability = probability;
		this.worth = worth;
		this.oi_guess = "";
		this.oi_know = false;
	}

	public Obj_info(String name, int probability, int worth, String oi_guess, boolean oi_know ){
		this.name = name;
		this.probability = probability;
		this.worth = worth;
		this.oi_guess = oi_guess;
		this.oi_know = oi_know;
	}

	public int getProbability() {
		return probability;
	}

	public String getName(){
		return this.name;
	}

	public void setName(String name){
		this.name = name;
	}

	public int getWorth() {
		return this.worth;
	}
}
