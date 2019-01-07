package org.hiro;

public class Score {
	int sc_uid;  // 元の型は uid_t
	int sc_score;
	long sc_flags;
	int sc_monster;
	String sc_name;
	int sc_level;
	long sc_time;

	public String getSc_name() {
		return sc_name.toString();
	}
}
