package entities.trap;

import entities.Entity;
import entities.Player;
import entities.Weapon;
import main.Game;
import helpgame.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static helpgame.HelpMethods.*;


public class RockHead extends Entity {
    private final TrapID id = TrapID.ROCKHEAD;
    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 30;
    private int action = 0;
    private final float rockSpeed = Game.SCALE;
    private boolean fall = false;
    private long timeDis;
    private final int[][] greenLvlData;
    private final float xOffset = 4*Game.SCALE;
    private final float yOffset = 4*Game.SCALE;

    public RockHead(float x, float y, int width, int height, Game game, int[][] greenLvlData) {
        super(x, y, width, height, game);
        initHitbox((int)x, (int)(y), (int)(30*Game.SCALE), (int)(30*Game.SCALE));
        loadAnimations();
        this.greenLvlData = greenLvlData;
    }

    private void loadAnimations() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.ROCK_HEAD_BLINK_ATLAS);
        animations = new BufferedImage[2][4];
        for(int i = 0; i < 4; i++){
            animations[0][i] = img.getSubimage(i*42, 0, 42, 42);
        }
        img = LoadSave.GetSpriteAtlas(LoadSave.ROCK_HEAD_HIT_ATLAS);
        for(int i = 0; i < 4; i++){
            animations[1][i] = img.getSubimage(i*42, 0, 42, 42);
        }
    }

    public void update(Player player){
        if(player.getHitbox().x+player.getHitbox().width > hitbox.x){
            fall = true;
        }
        if(fall){
            updatePos();
        }
        updateAnimationTick();
        collision(player);
    }
    public void render(Graphics g){
        g.drawImage(animations[action][aniIndex], (int)(hitbox.x-xOffset), (int)(hitbox.y-yOffset), width, height, null);
        //drawHitbox(g);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;

            if (aniIndex >= animations[0].length) {
                aniIndex = 0;
            }
        }
    }
    private void updatePos(){
        if (CanMove(hitbox.x, hitbox.y + rockSpeed, hitbox.width, hitbox.height, greenLvlData)) {
            hitbox.y += (int) rockSpeed;
        } else {
            hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, rockSpeed);
            action = 1;
            if(timeDis == 0){
                timeDis = System.currentTimeMillis();
            }
            if(System.currentTimeMillis() - timeDis >= 600)
                visible = false;
        }
    }

    public void collision(Player player){
        if(CheckCollision(getBoundDown(), player.getHitbox())){
            collidePlayer(player);
            player.setHP(player.getHP()-1);
            player.setHit(true);
        }
        for(int i = 0; i < player.getWeapons().size(); i++){
            Weapon weapon = player.getWeapons().get(i);
            if(CheckCollision(weapon.getBoundRight(), hitbox)){
                weapon.setVisible(false);
            }
        }
    }
    private void collidePlayer(Player player){
        visible = false;
        player.setInAir(false);
        player.jump();
        game.playStomp();
    }
}
