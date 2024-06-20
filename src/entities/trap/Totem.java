package entities.trap;

import entities.Entity;
import entities.Player;
import entities.Weapon;
import main.Game;
import helpgame.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static helpgame.HelpMethods.*;
import static helpgame.Constants.*;

public class Totem extends Entity {
    private final TrapID id = TrapID.TOTEM;
    private BufferedImage[][] animations;
    private BufferedImage imgGun;
    private int aniTick, aniIndex, aniSpeed = 40;
    private int action = ActionTotem.IDLE;
    private boolean attack = false;
    private final float xOffset = 6*Game.SCALE;
    private final float yOffset = 10*Game.SCALE;
    private int flipX, flipW;
    private int HP = 3;
    private final ArrayList<Weapon> weapons;

    public Totem(float x, float y, int width, int height, Game game) {
        super(x, y, width, height, game);
        initHitbox((int)(x+xOffset), (int)(y+yOffset), (int)(width-xOffset), (int)(height-yOffset));
        loadAnimations();
        weapons = new ArrayList<>();
    }

    private void loadAnimations(){
        animations = new BufferedImage[4][6];
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.TOTEM_ATLAS);
        for(int j = 0; j < animations.length; j++){
            for(int i = 0; i < animations[j].length; i++){
                animations[j][i] = img.getSubimage(i*30, j*32, 30, 32);
            }
        }
        imgGun = LoadSave.GetSpriteAtlas(LoadSave.TOTEM_WEAPON);
    }

    public void update(Player player){
        collision(player);
        attack(player);
        updatePos(player);
        updateAnimationTick();
        ArrayList<Weapon> weaponsRemove = new ArrayList<>();
        for(Weapon weapon : weapons){
            if(weapon.isVisible()) weapon.update();
            else weaponsRemove.add(weapon);
        }
        weapons.removeAll(weaponsRemove);
    }
    public void render(Graphics g){
        g.drawImage(animations[action][aniIndex], (int)(hitbox.x+flipX-xOffset), (int)(hitbox.y-yOffset), width*flipW, height, null);
        //drawHitbox(g);
        for(Weapon weapon : weapons){
            weapon.render(g);
        }
    }

    private void updatePos(Player player){
        if(player.getHitbox().x < hitbox.x) flipW = 1;
        else flipW = -1;

        if(flipW > 0){
            flipX = 0;
        }else if(flipW < 0){
            flipX = width;
        }
    }
    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;

            if (aniIndex >= ActionTotem.GetAmountSprite(action)) {
                if(action == ActionTotem.DESTROYED) visible = false;
                attack = false;
                aniIndex = 0;
                action = ActionTotem.IDLE;
            }
        }
    }
    private void attack(Player player){
        if(action == ActionTotem.DESTROYED) return;
        if(player.getHitbox().x > hitbox.x - Game.GAME_HEIGHT/3
                && player.getHitbox().x < hitbox.x+Game.GAME_HEIGHT/3
                && Math.abs(hitbox.y-player.getHitbox().y) <= Game.TILES_SIZE)
        {
            action = ActionTotem.ATTACK;
            if(aniIndex == 2 && !attack){
                game.playFireball();
                attack = true;
                Weapon weapon = new Weapon(hitbox.x, hitbox.y + hitbox.width/2, (int)(8*Game.SCALE), (int)(8*Game.SCALE), game, imgGun);
                if(flipW > 0) weapon.setWEAPON_SPEED(weapon.getWEAPON_SPEED()*-1);
                weapons.add(weapon);
            }
        }
    }
    private void collision(Player player){
        if(action == ActionTotem.DESTROYED) return;
        if(CheckCollision(player.getBoundDown(), getBoundTop())){
            setHP(this.HP-1);
            player.setInAir(false);
            player.jump();
        }else if(CheckCollision(player.getHitbox(), hitbox)){
            player.setHP(player.getHP()-1);
            player.setInAir(false);
            player.jump();
            player.setHit(true);
            game.playStomp();
        }
        for(Weapon weapon : player.getWeapons()){
            if(CheckCollision(weapon.getBoundRight(), hitbox)){
                weapon.setVisible(false);
                setHP(this.HP-1);
                game.playStomp();
            }
        }
        for(Weapon weapon : weapons){
            if(CheckCollision(player.getHitbox(), weapon.getHitbox())){
                player.setDizzied(true);
                player.setHit(true);
                weapon.setVisible(false);
            }
        }
    }

    private void setHP(int HP){
        if(HP <= 0){
            action = ActionTotem.DESTROYED;
            resetTick();
            HP = 0;
        }
        this.HP = HP;
    }
    private void resetTick(){
        aniTick = aniIndex = 0;
    }
}
