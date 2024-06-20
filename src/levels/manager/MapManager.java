package levels.manager;

import levels.Map;
import main.Game;
import helpgame.LoadSave;

import java.awt.*;

public class MapManager {
    private final Game game;
    private final Map[] maps;
    private final Map[] bossMaps;
    private final Map[] hiddenMaps;
    private int current = 0;
    private boolean boss = true, hidden = false;

    public MapManager(Game game){
        this.game = game;
        maps = new Map[5];
        bossMaps = new Map[5];
        hiddenMaps = new Map[5];
    }

    public void update(){
        if(boss){
            bossMaps[current].update();
        }else if(hidden){
            hiddenMaps[current].update();
        }else{
            maps[current].update();
        }
    }
    public void render(Graphics g){
        if(boss){
            bossMaps[current].render(g);
        }else if(hidden){
            hiddenMaps[current].render(g);
        }else{
            maps[current].render(g);
        }
    }

    public Map getCurrentMap(){
        if(boss) return bossMaps[current];
        if(hidden) return hiddenMaps[current];
        return maps[current];
    }
    public int getCurrent() {
        return current;
    }
    public void setCurrent(int current) {
        if(current > 3 || current < 0) {
            current = 0;
            boss = true;
        }
        this.current = current;
    }

    public boolean isBoss() {
        return boss;
    }
    public void setBoss(boolean boss) {
        this.boss = boss;
    }
    public boolean isBossExist(){return getCurrentMap().bossExist();}

    public boolean isHidden() {
        return hidden;
    }
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void initMap(){
        long tmp = System.currentTimeMillis();
        bossMaps[0] = new Map(game, LoadSave.TEST_GAME_DATA, 0);
        current = 1;
        maps[1] = new Map(game, LoadSave.LEVEL_ONE_DATA, 1);
        bossMaps[1] = new Map(game, LoadSave.LEVEL_BOSS_ONE_DATA, 1);
        hiddenMaps[1] = new Map(game, LoadSave.LEVEL_ONE_HIDDEN_DATA, 1);
        current = 2;
        maps[2] = new Map(game, LoadSave.LEVEL_TWO_DATA, 2);
        bossMaps[2] = new Map(game, LoadSave.LEVEL_BOSS_TWO_DATA, 2);
        hiddenMaps[2] = new Map(game, LoadSave.LEVEL_TWO_HIDDEN_DATA, 2);
        current = 3;
        maps[3] = new Map(game, LoadSave.LEVEL_THREE_DATA, 3);
        bossMaps[3] = new Map(game, LoadSave.LEVEL_BOSS_THREE_DATA, 3);
        hiddenMaps[3] = new Map(game, LoadSave.LEVEL_THREE_HIDDEN_DATA, 3);
        current = 4;
        maps[4] = new Map(game, LoadSave.LEVEL_FOUR_DATA, 4);
        current = 0;
        System.out.println(System.currentTimeMillis()-tmp);
    }
}
