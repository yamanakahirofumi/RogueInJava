package org.hiro.map;

/*
 * Coordinate data type
 * 座標
 */
public class Coord implements AbstractCoord {
	public int x;
	public int y;

	public Coord(){
		super();
	}

	public Coord(int x, int y){
		super();
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY(){
		return y;
	}

	@Override
	public AbstractCoord random(AbstractCoord position, AbstractCoord size) {
		return null;
	}

	public boolean equals(Object obj) {
		if(obj instanceof Coord){
			Coord c = (Coord) obj;
			return this.x == c.x && this.y ==c.y;
		}else {
			return false;
		}
	}

}
