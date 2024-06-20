package entities.trap;

import entities.Entity;
import entities.Player;
import entities.Weapon;
import levels.manager.MapManager;
import main.Game;
import helpgame.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static helpgame.HelpMethods.*;
import static helpgame.Constants.*;

public class Turtle extends Entity {
    private final TrapID id = TrapID.TURTLE;
    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 20;
    private int action = ActionTurtle.IDLE;
    private float curSpeed = Game.SCALE, turtleSpeed = Game.SCALE;
    private final float xOffset = 6*Game.SCALE;
    private final float yOffset = 5*Game.SCALE;
    private int flipX;
    private final Rectangle attackBox;

    public Turtle(float x, float y, int width, int height, Game game, MapManager mapManager) {
        super(x, y, width, height, game);
        initHitbox((int)(x), (int)(y+2*yOffset+1), (int)(width-2*xOffset), (int)(height-yOffset));
        attackBox = new Rectangle(hitbox.x-hitbox.width, hitbox.y-hitbox.height, hitbox.width*3, hitbox.height*3);
        this.mapManager = mapManager;
        loadAnimations();
    }

    private void loadAnimations(){
        animations = new BufferedImage[3][13];
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.TURTLE_ATLAS);
        for(int j = 0; j < animations.length; j++){
            for(int i = 0; i < animations[j].length; i++){
                animations[j][i] = img.getSubimage(i*44, j*26, 44, 26);
            }
        }

    }

    public void update(Player player){
        updateAnimationTick();
        if(action != ActionTurtle.HIT) {
            updatePos(player);
            collision(player);
        }
    }
    public void render(Graphics g){
        g.drawImage(animations[action][aniIndex], (int)(hitbox.x+flipX-xOffset), (int)(hitbox.y-yOffset), (int)(width*(-turtleSpeed)), height, null);
    }

    private void updatePos(Player player){
        if(CheckCollision(player.getHitbox(), hitbox)) return;
        float tmp = curSpeed;
        if (CanMove(hitbox.x + curSpeed, hitbox.y-hitbox.height, hitbox.width, hitbox.height, mapManager.getCurrentMap().getLevel().getGreenLvlData())
                && IsFloor(hitbox, curSpeed, mapManager.getCurrentMap().getLevel().getGreenLvlData())) {
                hitbox.x += (int) curSpeed;
        }
        else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, curSpeed);
            if(curSpeed > 0){
                hitbox.x += width;
            }
            curSpeed *=-1;
        }
        if(curSpeed < 0){
            flipX = 0;
        }else if(curSpeed > 0){
            flipX = width;
        }
        if(tmp != curSpeed){
            turtleSpeed = curSpeed;
            curSpeed = 0;
        }
        attackBox.x = hitbox.x-hitbox.width;
    }
    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;

            if (aniIndex >= ActionTurtle.GetAmountSprite(action)) {
                aniIndex = 0;
                setAction();
                if(curSpeed == 0){
                    curSpeed = turtleSpeed;
                }
            }
        }
    }
    private void setAction(){
        if(action == ActionTurtle.HIT){
            visible = false;
        }
        else if(action == ActionTurtle.SPIKES){
            action = ActionTurtle.IDLE;
            curSpeed = turtleSpeed;
        }
    }
    public void collision(Player player){
        if(CheckCollision(player.getHitbox(), attackBox)){
            if(action != ActionTurtle.SPIKES) action = ActionTurtle.SPIKES;
        }
        if(CheckCollision(player.getHitbox(), hitbox)){
            player.setHP(player.getHP()-1);
            player.setInAir(false);
            player.jump();
            player.setHit(true);
        }
        for(int i = 0; i < player.getWeapons().size(); i++){
            Weapon weapon = player.getWeapons().get(i);
            if(CheckCollision(weapon.getBoundRight(), hitbox)){
                weapon.setVisible(false);
                game.playStomp();
                action = ActionTurtle.HIT;
                aniIndex = aniTick = 0;
                break;
            }
        }
    }
}
