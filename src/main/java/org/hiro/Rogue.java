package org.hiro;

import org.hiro.character.Human;
import org.hiro.character.Player;
import org.hiro.map.Dungeon;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootConfiguration
@SpringBootApplication
public class Rogue {
	public static void main(String[] args){
		Game g = Game.getInstance();

		// ゲームのオプションの設定
		Map<String,Object> options = new HashMap<>();
		options.put("passgo",false);
		g.setOptions(options);

		// ゲームの世界を作る
		String dungeonName= "洞窟";
		Dungeon d = new Dungeon(dungeonName);
		g.addDungeons(d);

		// プレーヤーメイク
		Player p = new Human(Global.whoami);
		g.setPlayer(p);

		g.move(dungeonName,p);

		System.out.println("hell");
	}

}
