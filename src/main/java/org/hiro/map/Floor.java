package org.hiro.map;

import org.hiro.Place;
import org.hiro.Room;
import org.hiro.Util;
import org.hiro.character.Player;
import org.hiro.things.Gold;
import org.hiro.things.Thing;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Floor {
    private List<Room> rooms;
    private Place places; // リストが良いのか？二次元配列？Coordに合わせて？
    // private int maxrooms;
    private int level;
    private AbstractCoordinateFactory factory;
    private List<Thing> thingList; // ものリスト

    Floor(int level,int maxrooms, AbstractCoordinateFactory factory){
//        this.maxrooms = maxrooms;
        this.factory = factory;
        this.level = level;
        this.thingList = new LinkedList<>();

        // DrawRoom do_rooms

        AbstractCoordinate base = factory.getMaxCoordinate();

        rooms = Stream.generate(() -> new Room(factory))
                .limit(maxrooms)
                .collect(Collectors.toList());

        // 最大４つ明かりをつける
        int left_out = Util.rnd(4);
        for (int i = 0; i < left_out; i++) {
            Room r = rooms.get(rnd_room());
            r.addInfo(RoomInfoEnum.ISGONE);
        }
        /*
         * その階の部屋を設置
         */
        for(Room r: rooms) {
            // 部屋で回す

            // 明かりの部屋処理
            if(r.containInfo(RoomInfoEnum.ISGONE)){

                continue;
            }
            // 部屋の設定
            if(Util.rnd(10) < level -1 ){
                r.addInfo(RoomInfoEnum.ISDARK);        /* dark room */
                if (Util.rnd(15) == 0) {
                    r.setInfo(RoomInfoEnum.ISMAZE);        /* maze room */
                }
            }

            // 部屋を置く

            // 金を置く
            if(Util.rnd(2) == 0){
                Gold g = new Gold(level);
                AbstractCoordinate coord = r.randomPosition();
                // g.
                this.thingList.add(g);
                // r.r_gold = r.randomPosition();
            }

            // モンスターを置く
        }

        // 道を作る
        // Passage.do_passages()

        // trapを置く

        // 階段を置く
    }

    /*
     * rnd_room:
     *	Pick a room that is really there
     */
    private int rnd_room() {
        int rm;

        do {
            rm = Util.rnd(this.rooms.size());
        } while (this.rooms.get(rm).containInfo(RoomInfoEnum.ISGONE));
        return rm;
    }

    /**
     *
     */
    public void setPlayer(Player p){
        p.setLevel(this.level);
        Room r = this.rooms.get(rnd_room());
        r.setPlayer(p);

    }
}
