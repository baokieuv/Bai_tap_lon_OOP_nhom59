package entities.trap;

import entities.Entity;
import entities.Player;
import entities.Weapon;
import levels.manager.MapManager;
import main.Game;
import helpgame.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static helpgame.HelpMethods.*;

public class Bug extends Entity {
    private final TrapID id = TrapID.BUG;
    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniAction = 0;
    private final int aniSpeed = 30;
    private float bugSpeed = Game.SCALE;
    private float bugSpeedCur = Game.SCALE;
    private boolean hit = false;
    private int flipX;

    public Bug(float x, float y, int width, int height, Game game, MapManager mapManager) {
        super(x, y, width, height, game);
        initHitbox((int)x, (int)(y), width, height);
        loadAnimations();
        super.loadLvlData(mapManager);
    }

    private void loadAnimations() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.BUG_RUN_ATLAS);
        animations = new BufferedImage[2][4];
        for(int i = 0; i < 3; i++){
            animations[0][i] = img.getSubimage(i*24, 0, 24, 24);
        }
        img = LoadSave.GetSpriteAtlas(LoadSave.BUG_HIT_ATLAS);
        for(int i = 0; i < 4; i++){
            animations[1][i] = img.getSubimage(i*24, 0, 24, 24);
        }
    }

    public void update(ArrayList<Weapon> weapons, Player player){
        updateAnimationTick();
        if(!hit) {
            updatePos();
            collision(weapons, player);
        }
    }
    public void render(Graphics g){
        g.drawImage(animations[aniAction][aniIndex], hitbox.x+flipX, (hitbox.y), (int)(width*(-bugSpeed)), height, null);
        //drawHitbox(g);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;

            if (aniIndex >= aniAction+3) {
                aniIndex = 0;
                if(aniAction == 1){
                    visible = false;
                }
                if(bugSpeedCur == 0){
                    bugSpeedCur = bugSpeed;
                }
            }
        }
    }
    private void updatePos(){
        float tmp = bugSpeedCur;
        if (CanMove(hitbox.x + bugSpeedCur, hitbox.y- hitbox.height, hitbox.width, hitbox.height, mapManager.getCurrentMap().getLevel().getGreenLvlData())
            && IsFloor(hitbox, bugSpeedCur,mapManager.getCurrentMap().getLevel().getGreenLvlData())) {
            hitbox.x += (int) bugSpeedCur;
        }
        else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, bugSpeedCur);
            if(bugSpeedCur > 0){
                hitbox.x += width;
            }
            bugSpeedCur *=-1;
        }
        if(bugSpeedCur < 0){
            flipX = 0;
        }else if(bugSpeedCur > 0){
            flipX = width;
        }
        if(tmp != bugSpeedCur){
            bugSpeed = bugSpeedCur;
            bugSpeedCur = 0;
        }
    }

    public void collision(ArrayList<Weapon> weapons, Player player){
        for(Weapon weapon: weapons){
            if(CheckCollision(hitbox, weapon.getBoundRight())){
                beforeDie();
                weapon.setVisible(false);
            }
        }
        if(CheckCollision(getBoundTop(), player.getBoundDown())){
            beforeDie();
            collidePlayer(player);
        }else if(CheckCollision(hitbox, player.getHitbox())){
            collidePlayer(player);
            player.setHP(player.getHP()-1);
            player.setHit(true);
            game.playStomp();
        }
    }
    private void collidePlayer(Player player){
        //beforeDie();
        player.setInAir(false);
        player.jump();
    }
    private void beforeDie(){
        game.playStomp();
        hit = true;
        aniAction = 1;
        aniTick = aniIndex = 0;
    }
}
