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
	private String temporaryName;
	/**
	 * 鑑定済みフラグ
	 *
	 */
	private boolean known;

	public Obj_info(String name, int probability, int worth){
		this.name = name;
		this.probability = probability;
		this.worth = worth;
		this.temporaryName = "";
		this.known = false;
	}

	public Obj_info(String name, int probability, int worth, String temporaryName, boolean known){
		this.name = name;
		this.probability = probability;
		this.worth = worth;
		this.temporaryName = temporaryName;
		this.known = known;
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

	public void know(){
		this.temporaryName = "";
		this.known = true;
	}

	public String getTemporaryName() {
		return temporaryName;
	}

	public boolean isTemporaryNamed(){
		return this.temporaryName.length() > 0;
	}

	public boolean isKnown() {
		return this.known;
	}

	public void setKnown(boolean known) {
		this.known = known;
	}

	public void addKnown(boolean known){
		this.known |= known;
	}
}
