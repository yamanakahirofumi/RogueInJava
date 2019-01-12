package org.hiro;

/**
 * Help list
 *
 * 元はh_list
 */
public class Help_list {
	char h_ch;
    String h_desc;
	boolean h_print;

	public Help_list(){ }

	public Help_list(char h_ch, String h_desc, boolean h_print){
		this.h_ch = h_ch;
		this.h_desc = h_desc;
		this.h_print = h_print;
	}
}
