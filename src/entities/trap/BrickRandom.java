package entities.trap;

import entities.Entity;
import levels.manager.ItemManager;
import entities.Player;
import levels.manager.MapManager;
import main.Game;
import helpgame.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import static entities.item.ItemID.*;
import static helpgame.HelpMethods.*;

public class BrickRandom extends Entity {
    private final TrapID id = TrapID.BRICKRANDOM;
    private final BufferedImage[] animations;

    public BrickRandom(float x, float y, int width, int height, Game game, MapManager mapManager) {
        super(x, y, width, height, game);
        initHitbox((int)x, (int)y, width, (height+2));
        super.loadLvlData(mapManager);
        animations = new BufferedImage[2];
        loadAnimations();
    }

    private void loadAnimations(){
        if(mapManager.getCurrent()==2){
            BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_BW_ATLAS);
            animations[0] = img.getSubimage(7 * 16, 0, 16, 16);
            animations[1] = img.getSubimage(7 * 16, 16, 16, 16);
        }else{
            BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
            animations[0] = img.getSubimage(10 * 18, 0, 18, 18);
            animations[1] = img.getSubimage(10 * 18, 18, 18, 18);
        }
    }

    public void update(ItemManager itemManager, Player player){
        collision(itemManager, player);
    }
    public void render(Graphics g){
        if(visible){
            g.drawImage(animations[0], hitbox.x, hitbox.y, width, height, null);
        }else{
            g.drawImage(animations[1], hitbox.x, hitbox.y, width, height, null);
        }
        //drawHitbox(g);
    }

    private void collision(ItemManager itemManager, Player player){
        if(visible && CheckCollision(player.getBoundTop(), getBoundDown())){
            visible = false;
            randomItem(itemManager);
            game.playOneUp();
        }
    }
    private void randomItem(ItemManager itemManager){
        Random random = new Random();
        int tmp = random.nextInt(0, 5);
        if (tmp == 1)
            itemManager.addItem(APPLE, hitbox.x, hitbox.y-Game.TILES_SIZE);
        else if (tmp == 2)
            itemManager.addItem(COIN, hitbox.x, hitbox.y-Game.TILES_SIZE);
        else if (tmp == 3)
            itemManager.addItem(BANANA, hitbox.x, hitbox.y-Game.TILES_SIZE);
        else if (tmp == 4)
            itemManager.addItem(ORANGE, hitbox.x, hitbox.y-Game.TILES_SIZE);
    }
}
