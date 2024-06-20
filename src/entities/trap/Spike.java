package entities.trap;

import entities.Entity;
import entities.Player;
import levels.manager.MapManager;
import main.Game;
import helpgame.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static helpgame.HelpMethods.*;

public class Spike extends Entity {
    private final TrapID id = TrapID.SPIKE;
    private BufferedImage img;
    private final float yOffset = 18* Game.SCALE;

    public Spike(float x, float y, int width, int height, Game game, MapManager mapManager) {
        super(x, y, width, height, game);
        initHitbox((int)x, (int)(y+yOffset), width, (int)(height-yOffset));
        super.loadLvlData(mapManager);
        loadAnimation();
    }

    private void loadAnimation(){
        if(mapManager.getCurrent() == 2){
            img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_BW_ATLAS).getSubimage(2 * 16, 6 * 16, 16, 16);
        }else{
            img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS).getSubimage(8 * 18, 3 * 18, 18, 18);
        }
    }

    public void update(Player player){
        if(CheckCollision(hitbox, player.getHitbox())){
            player.setHP(player.getHP()-1);
            player.setHit(true);
            player.setInAir(false);
            player.jump();
            game.playStomp();
        }
    }
    public void render(Graphics g){
        g.drawImage(img, hitbox.x, (int)(hitbox.y-yOffset), width, height, null);
        //drawHitbox(g);
    }
}
