package org.hiro;

import org.hiro.character.Player;
import org.hiro.input.InputDevice;
import org.hiro.input.KeyboardDevice;
import org.hiro.map.Dungeon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    private static final Game instance = new Game();
    private boolean goal;
    private Map<String, Dungeon> dungeons;
    private List<InputDevice> inputDevices;
    private Player player;

    /**
     * Options
     */
    /**
     * Follow passages (Option) 通路の角で止まらない
     */
    private boolean passgo;

    private Game() {
        this.goal = false;
        this.passgo = false;
        this.dungeons = new HashMap<>();
        this.inputDevices = new ArrayList<>();
        inputDevices.add(new KeyboardDevice());
    }

    public static Game getInstance() {
        return instance;
    }

    public void setOptions(Map<String, Object> options) {

    }

    public boolean isGoal() {
        return this.goal;
    }

    public void setGoal(boolean b) {
        this.goal = b;
    }

    public void addDungeons(Dungeon d) {
        this.dungeons.put("", d);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean move(String dungeonName, Player p) {
        if (!this.dungeons.containsKey(dungeonName)) {
            return false;
        }
        this.dungeons.get(dungeonName).setPlayer(p);
        return true;

    }
}
