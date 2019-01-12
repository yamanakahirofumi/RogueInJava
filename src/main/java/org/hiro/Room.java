package org.hiro;

import org.hiro.character.Player;
import org.hiro.map.AbstractCoordinate;
import org.hiro.map.AbstractCoordinateFactory;
import org.hiro.map.Coordinate;
import org.hiro.map.RoomInfoEnum;

import java.util.HashSet;

/*
 * Room structure
 */
public class Room {

	Coordinate r_pos;            /* Upper left corner */ // 左上
	Coordinate r_max;            /* Size of room */      // サイズ
	public Coordinate r_gold;            /* Where the gold is */ // goldの位置
	int r_goldval;            /* How much the gold is worth */
	@Deprecated
	private int r_flags;            /* info about the room */  // infoに変わったよ
	private HashSet<RoomInfoEnum> info;
	private AbstractCoordinateFactory factory;
	int r_nexits;            /* Number of exits */
	Coordinate[] r_exit = new Coordinate[12];            /* Where the exits are */

	public Room(AbstractCoordinateFactory factory){
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
		this.r_pos = new Coordinate(posX, posY);
		this.r_max = new Coordinate(maxX, maxY);
		this.r_gold = new Coordinate();
		this.r_goldval = 0;
		this.info = new HashSet<>();
//		this.r_nexits;
	}

	public Room(Coordinate position, Coordinate maxSize, Coordinate goldPosition, int goldWorth, RoomInfoEnum flag, int exitsNumber){
		this.r_pos = position;
		this.r_max = maxSize;
		this.r_gold = goldPosition;
		this.r_goldval = goldWorth;
		this.info = new HashSet<>();
		this.info.add(flag);
		this.r_nexits = exitsNumber;
	}

	public void setPlayer(Player p){
		AbstractCoordinate coord = this.randomPosition();
		p.setPostion(coord);
	}
	public AbstractCoordinate randomPosition(){
        return factory.create().random(this.r_pos, this.r_max);
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
