package org.hiro.things;

import java.util.Arrays;

public enum ObjectType {

	PASSAGE('#'),
	DOOR('+'),
	FLOOR('.'),
	PLAYER('@'),
	TRAP('^'),
	STAIRS('%'),
	GOLD('*'),
	POTION('!'),
	SCROLL ('?'),
	MAGIC ('$'),
	FOOD (':'),
	WEAPON (')'),
	ARMOR (']'),
	AMULET (','),
	RING ('='),
	STICK ('/'),

	Vert('|'),
	Horizon('-'),
	Blank(' '),
	Initial('\u0000');

	private char value;
	ObjectType(char value){
		this.value = value;
	}

	public char getValue() {
		return value;
	}

	static public ObjectType get(char c){
		return Arrays.asList(ObjectType.values()).stream().filter(o -> o.getValue() == c).findFirst().get();
	}
}
