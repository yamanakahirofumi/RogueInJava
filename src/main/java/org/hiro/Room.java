package org.hiro;

import org.hiro.character.Player;
import org.hiro.map.AbstractCoord;
import org.hiro.map.AbstractCoordFactory;
import org.hiro.map.Coord;
import org.hiro.map.RoomInfoEnum;

import java.util.HashSet;

/*
 * Room structure
 */
public class Room {

	Coord r_pos;            /* Upper left corner */ // 左上
	Coord r_max;            /* Size of room */      // サイズ
	public Coord r_gold;            /* Where the gold is */ // goldの位置
	int r_goldval;            /* How much the gold is worth */
	@Deprecated
	private int r_flags;            /* info about the room */  // infoに変わったよ
	private HashSet<RoomInfoEnum> info;
	private  AbstractCoordFactory factory;
	int r_nexits;            /* Number of exits */
	Coord[] r_exit = new Coord[12];            /* Where the exits are */

	public Room(AbstractCoordFactory factory){
		this.factory = factory;
//		this.r_pos = factory.create();
//		this.r_max = factory.create();
//		this.r_gold = factory.create();
		this.r_goldval = 0;
		this.info = new HashSet<>();
		this.r_nexits = 0;
//		this.r_exit[0] = factory.create();
	}

	public Room(int posX, int posY,int maxX, int maxY){
		this.r_pos = new Coord(posX, posY);
		this.r_max = new Coord(maxX, maxY);
		this.r_gold = new Coord();
		this.r_goldval = 0;
		this.info = new HashSet<>();
//		this.r_nexits;
	}

	public Room(Coord position, Coord maxSize, Coord goldPosition, int goldWorth, RoomInfoEnum flag, int exitsNumber){
		this.r_pos = position;
		this.r_max = maxSize;
		this.r_gold = goldPosition;
		this.r_goldval = goldWorth;
		this.info = new HashSet<>();
		this.info.add(flag);
		this.r_nexits = exitsNumber;
	}

	public void setPlayer(Player p){
		AbstractCoord coord = this.randomPosition();
		p.setPostion(coord);
	}
	public AbstractCoord randomPosition(){
		AbstractCoord cp = factory.create().random(this.r_pos, this.r_max);
		return cp;
	}

	public void addInfo(RoomInfoEnum r){
		this.info.add(r);
	}

	public void setInfo(RoomInfoEnum r){
		this.info.clear();
		this.info.add(r);
	}

	public void removeInfo(RoomInfoEnum r){
		this.info.remove(r);
	}

	public boolean containInfo(RoomInfoEnum r){
		return this.info.contains(r);
	}

	public boolean equalsforInfo(RoomInfoEnum r){
		return this.info.size() == 1 && this.info.contains(r);
	}

	public void clearInfo(){
		this.info.clear();
	}

}
