package entities.trap;

import entities.Entity;
import entities.Player;
import levels.manager.MapManager;
import main.Game;
import helpgame.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static helpgame.HelpMethods.*;

public class BrokenBrick extends Entity {
    private final TrapID id = TrapID.BROKENBRICK;
    private final BufferedImage[] animations;
    private int aniTick, aniIndex;
    private final int aniSpeed = 30;
    private boolean hit = false;

    public BrokenBrick(float x, float y, int width, int height, Game game) {
        super(x, y, width, height, game);
        initHitbox((int)x, (int)y, width, (height+2));
        animations = new BufferedImage[7];
        loadAnimations();
    }

    private void loadAnimations(){
        BufferedImage img;
        if(game.getMapManager().getCurrent() == 2) img = LoadSave.GetSpriteAtlas(LoadSave.BROKENBRICK2_ATLAS);
        else img= LoadSave.GetSpriteAtlas(LoadSave.BROKENBRICK_ATLAS);
        for(int i = 0; i < 7; i++){
            animations[i] = img.getSubimage(i*27, 0, 27, 22);
        }
    }

    public void update(MapManager mapManager, Player player){
        collision(mapManager, player);
        if(hit){
            updateAnimationTick();
        }
    }
    public void render(Graphics g){
        if(hit){
            g.drawImage(animations[aniIndex], hitbox.x, hitbox.y, width, height, null);
        }
        //drawHitbox(g);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;

            if (aniIndex >= animations.length) {
                aniIndex = 0;
                visible = false;
            }
        }
    }
    private void collision(MapManager mapManager, Player player){
        if(!hit && CheckCollision(player.getBoundTop(), getBoundDown())){
            hit = true;
            mapManager.getCurrentMap().getLevel().setSpriteIndex((int)(x/Game.TILES_SIZE), (int)(y/Game.TILES_SIZE), 180);
            mapManager.getCurrentMap().getLevel().setGreenSpriteIndex((int)(x/Game.TILES_SIZE), (int)(y/Game.TILES_SIZE), 255);
            player.collisionBrick();
            game.playBlockBreak();
        }
    }
}
