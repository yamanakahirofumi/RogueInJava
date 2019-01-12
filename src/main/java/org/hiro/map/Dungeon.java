package org.hiro.map;

import org.hiro.character.Player;

import java.util.List;

public class Dungeon {
    String name;
    int maxLevel;       // Deepest level 最深部
    int maxColumnSize;  // 階層の最大列数
    int maxLineSize;    // 階層の最大行数
    int maxRoomSize;    // 部屋の最大数
    List<Floor> Floors;

    public Dungeon(String name){
        this.name = name;
        this.maxLevel = 26;
        this.maxColumnSize = 80;
        this.maxLineSize = 24;

    }
    public String getName(){
        return this.name;
    }

    public void setPlayer(Player p){
        p.setDungeon(this.name);
        Floor f = new Floor(1,maxRoomSize,new TwoDimensionsCoordinateFactory());
        Floors.add(f);
        f.setPlayer(p);
    }

    private void createFloor(){
        Floor f = new Floor(1,maxRoomSize,new TwoDimensionsCoordinateFactory());
    }

    public int getMaxLevel(){
        return this.maxLevel;
    }
}
