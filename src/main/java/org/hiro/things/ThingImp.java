package org.hiro.things;

import org.hiro.Obj_info;
import org.hiro.Room;
import org.hiro.Stats;
import org.hiro.baggage.Bag;
import org.hiro.character.StateEnum;
import org.hiro.map.Coordinate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/*
 * Structure for monsters and player
 * モンスターとプレイヤーの構造体
 */
public class ThingImp implements OriginalMonster, Thing {

    // これでアクセスされるパターン こっちはプレイヤやモンスタっぽい。
    public Coordinate _t_pos;            /* Position */
    /*
     * If slowed, is it a turn to move
     * 旧_t_turn
     */
    private boolean slow;
    private int _t_type;            /* What it is */
    private int _t_disguise; //ObjectType 変装用        /* What mimic looks like */

    private int _t_oldch; //ObjectType??            /* Character that was where it was */
    private Coordinate _t_dest;        /* Where it is running to */
    // private int _t_flags;            /* State word */ // Enumの配列が良さそう
    private HashSet<StateEnum> flags = new HashSet<>();
    private Stats _t_stats;        /* Physical description */  //
    /**
     * Current room for Monster
     *
     * 旧t_room
     */
    private Room room;
    @Deprecated
    private ThingImp _t_pack;        /* What the thing is carrying */  // 配列
    private List<ThingImp> baggage = new ArrayList<>();
    private Bag bag;

    // これでアクセスされるパターン２
    public Coordinate _o_pos;            /* Where it lives on the screen */
    public char _o_text;            /* What it says if you read it */
    public int _o_launch;            /* What you need to launch it */
    public int _o_count;            /* count for plural objects */
    public int _o_which;            /* Which object of a type it is */ // Thingクラスにできないか？
    private HashSet<Integer> _o_flags;            /* information about objects */
    int _o_group;            /* group number for this object */  // もしかして要らない
    public char _o_label;            /* Label for object */

    @Override
    public void addState(StateEnum se) {
        this.flags.add(se);
    }
    @Override
    public void removeState(StateEnum se) {
        this.flags.remove(se);
    }
    @Override
    public boolean containsState(StateEnum se) {
        return this.flags.contains(se);
    }

    public void setState(HashSet<StateEnum> flags) {
        this.flags = flags;
    }
    @Override
    public void setState(int flags){
        // TODO: どうしようか
    }

    @Override
    public int getType() {
        return this._t_type;
    }

    @Override
    public void setType(int type){
        this._t_type = type;
    }

    @Override
    public boolean isSlow() {
        return this.slow;
    }

    @Override
    public void slow() {
        this.slow = true;
    }

    @Override
    public void changeSlow(){
        this.slow = !this.slow;
    }

    @Override
    public Stats getStatus(){
        return  this._t_stats;
    }

    @Override
    public void setStatus(Stats status){
        this._t_stats = status;
    }

    @Override
    public Room getRoom(){
        return this.room;
    }

    @Override
    public void setRoom(Room room){
        this.room = room;
    }

    @Override
    public Coordinate getPosition(){
        return this._t_pos;
    }

    @Override
    public void setPosition(Coordinate coordinate){
        this._t_pos = coordinate;
    }

    @Override
    public Coordinate getRunPosition(){
        return  this._t_dest;
    }

    @Override
    public void setRunPosition(Coordinate coordinate){
        this._t_dest = coordinate;
    }

    @Override
    public int getFloorTile(){
        return this._t_oldch;
    }

    @Override
    public void setFloorTile(int tile){
        this._t_oldch = tile;
    }

    @Override
    public int getDisplayTile(){
        return this._t_disguise;
    }

    @Override
    public void setDisplayTile(int tile){
        this._t_disguise = tile;
    }


    @Override
    public void addItem(ThingImp th) {
        this.baggage.add(th);
    }
    @Override
    public void removeItem(ThingImp th) {
        this.baggage.remove(th);
    }

    @Override
    public List<ThingImp> getBaggage() {
        return this.baggage;
    }
    @Override
    public int getBaggageSize() {
        return this.baggage.size();
    }
    @Override
    public void setBaggage(List<ThingImp> list) {
        this.baggage = list;
    }

    @Override
    public void add_o_flags(int flag) {
        this._o_flags.add(flag);
    }

    @Override
    public void delete_o_flags(int flag) {
        this._o_flags.remove(flag);
    }

    @Override
    public void set_o_flags(int flag) {
        this._o_flags.clear();
        this._o_flags.add(flag);
    }

    @Override
    public boolean contains_o_flags(int flag) {
        return this._o_flags.contains(flag);
    }

    /**
     * new_Thing()の代わり
     */
    public ThingImp() {
        this.bag = new Bag();
        this._o_count = 1;
        this._o_group = 0;
        this._o_flags = new HashSet<>();
    }

    @Override
    public int foodValue() {
        return 0;
    }

    @Override
    public Optional<Thing> eat() {
        return Optional.of(this);
    }

    @Override
    public int getWorth() {
        return 0;
    }

    List<Obj_info> getTypes(String filename) {
        List<Obj_info> result = new ArrayList<>();
        try (InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(filename);
             BufferedReader reader = new BufferedReader(new InputStreamReader(resourceAsStream))) {
            reader.lines().forEach(line -> result.add(convertLine(line)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Obj_info convertLine(String line) {
        String[] cols = line.split(",");
        if (cols.length < 5) {
            throw new RuntimeException("バグやで");
        }
        String oi_name = cols[0];
        int oi_prob = Integer.valueOf(cols[1]);
        int oi_worth = Integer.valueOf(cols[2]);
        String oi_guess = "null".equals(cols[3]) ? "" : cols[3];
        boolean oi_know = Boolean.valueOf(cols[4]);
        // TODO:
        return new Obj_info(oi_name, oi_prob, oi_worth, oi_guess, oi_know);
    }

    @Override
    public boolean isGroup() {
        return this._o_group != 0;
    }

    @Override
    public int getGroup(){
        return this._o_group;
    }

    @Override
    public void addCount(int c) {
        this._o_count += c;
    }

    @Override
    public int getCount() {
        return this._o_count;
    }
}
