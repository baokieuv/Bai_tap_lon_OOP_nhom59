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

public class Bat extends Entity {
    private final TrapID id = TrapID.BAT;
    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 30, aniAction = 0;
    private float batSpeed = Game.SCALE, batSpeedCur = Game.SCALE;
    private boolean hit = false;
    private final int yPos;
    private final float yDrawOffset = 13 * Game.SCALE;

    public Bat(float x, float y, int width, int height, Game game, MapManager mapManager) {
        super(x, y, width, height, game);
        initHitbox((int)x, (int)(y), width, (int)(height-yDrawOffset));
        loadAnimations();
        yPos = (int)y;
        super.loadLvlData(mapManager);
    }

    private void loadAnimations() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.BAT_FLY_ATLAS);
        animations = new BufferedImage[2][4];
        for(int i = 0; i < 3; i++){
            animations[0][i] = img.getSubimage(i*24, 0, 24, 24);
        }
        img = LoadSave.GetSpriteAtlas(LoadSave.BAT_HIT_ATLAS);
        for(int i = 0; i < 4; i++){
            animations[1][i] = img.getSubimage(i*24, 0, 24, 24);
        }
    }

    public void update(ArrayList<Weapon> entities, Player player){
        updateAnimationTick();
        if(!hit) {
            updatePos();
            collision(entities, player);
        }
    }
    public void render(Graphics g){
        g.drawImage(animations[aniAction][aniIndex], hitbox.x, (int)(hitbox.y-yDrawOffset+6), width, height, null);
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
                if(batSpeedCur == 0){
                    batSpeedCur = batSpeed;
                }
            }
        }
    }
    private void updatePos(){
        float tmp = batSpeedCur;
        if (CanMove(hitbox.x, hitbox.y + batSpeedCur, hitbox.width, hitbox.height, mapManager.getCurrentMap().getLevel().getGreenLvlData())) {
            hitbox.y += (int) batSpeedCur;
            if(hitbox.y <= yPos - Game.GAME_HEIGHT/3){
                batSpeedCur*=-1;
            }
        } else {
            hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, batSpeedCur);
            batSpeedCur*=-1;
        }
        if(tmp != batSpeedCur){
            batSpeed = batSpeedCur;
            batSpeedCur = 0;
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
        player.setInAir(false);
        player.jump();
    }
    private void beforeDie(){
        game.playStomp();
        hit = true;
        aniAction = 1;
        aniIndex = aniTick = 0;
    }
}
