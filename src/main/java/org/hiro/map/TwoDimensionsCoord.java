package org.hiro.map;

import org.hiro.Util;

/*
 * Coordinate data type
 * 座標
 */
public class TwoDimensionsCoord implements AbstractCoord {
	int x;
	int y;

	public TwoDimensionsCoord(){
		this.x = 0;
		this.y =0;
	}

	public TwoDimensionsCoord(int x, int y){
		this.x = x;
		this.y = y;
	}

	@Override
	public AbstractCoord random(AbstractCoord position, AbstractCoord size) {
		if(!(position instanceof TwoDimensionsCoord)){
			throw new IllegalArgumentException("");
		}
		if(!(size instanceof TwoDimensionsCoord)){
			throw new IllegalArgumentException("");
		}
		int _x = ((TwoDimensionsCoord)position).x + Util.rnd(((TwoDimensionsCoord)size).x - 2) + 1;
		int _y = ((TwoDimensionsCoord)position).x + Util.rnd(((TwoDimensionsCoord)size).y - 2) + 1;
		return new TwoDimensionsCoord( _x ,_y);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof TwoDimensionsCoord){
			TwoDimensionsCoord c = (TwoDimensionsCoord) obj;
			return this.x == c.x && this.y ==c.y;
		}else {
			return false;
		}
	}
}
