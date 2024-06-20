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
import java.util.Random;

import static helpgame.HelpMethods.*;

class WeaponBoss extends Weapon{
    private final int xPos;

    public WeaponBoss(float x, float y, int width, int height, Game game, BufferedImage img) {
        super(x, y, width, height, game);
        WEAPON_SPEED = -2;
        loadAnimations(img);
        this.width = hitbox.width = width;
        hitbox.height = height;
        xPos = (int)x;
    }

    private void loadAnimations(BufferedImage img){
        this.img = img;
    }

    public void update(int[][] greenLvlData){
        if(WEAPON_SPEED < 0){
            flipX = width;
            flipW = -1;
        }else{
            flipX = 0;
            flipW = 1;
        }
        hitbox.x += WEAPON_SPEED;
        if(hitbox.x < xPos-Game.GAME_WIDTH){
            visible = false;
        }
    }
}

public class Boss extends Entity {
    private final TrapID id = TrapID.BOSS;
    BufferedImage[][] animations;
    BufferedImage imgUI;
    private int action = 0, aniTick, aniIndex, aniSpeed = 30;
    private final ArrayList<WeaponBoss> weapons;
    private int HP = 30;
    private long timeAttack = System.currentTimeMillis();
    private long timeAttacked = System.currentTimeMillis();

    public Boss(float x, float y, int width, int height, Game game, MapManager mapManager) {
        super(x, y, width, height, game);
        this.mapManager = mapManager;
        weapons = new ArrayList<>();
        initHitbox((int)x, (int)y, width, height);
        loadAnimations();
        imgUI = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS).getSubimage(4*18, 2*18, 18, 18);
    }

    private void loadAnimations(){
        animations = new BufferedImage[2][8];
        BufferedImage img;
        if(mapManager.getCurrent() == 2)
            img = LoadSave.GetSpriteAtlas(LoadSave.BOSS2_ATLAS);
        else
            img = LoadSave.GetSpriteAtlas(LoadSave.BOSS_ATLAS);

        for(int i = 0; i < 4; i++) {
            animations[0][i] = img.getSubimage(i * 32, 0, 32, 32);
        }
        img = LoadSave.GetSpriteAtlas(LoadSave.BOSS_ATLAS);
        animations[0][4] = img.getSubimage(4*32, 0, 24, 16);

        img = LoadSave.GetSpriteAtlas(LoadSave.BOSS_DISAPPEARING);
        for(int i = 0; i < 8; i++){
            animations[1][i] = img.getSubimage(i*212, 0, 212, 212);
        }

    }

    public void update(Player player){
        importQuiz();
        if(action == 0) {
            attacking(player);
            collision(player);
        }
        updateAnimationTick();

        ArrayList<WeaponBoss> weaponRemove = new ArrayList<>();
        for(WeaponBoss weapon : weapons){
            if(weapon.isVisible()){
                weapon.update(player.getGreenLvlData());
            }else weaponRemove.add(weapon);
        }
        weapons.removeAll(weaponRemove);
    }
    public void render(Graphics g){
        drawUI(g);
        g.drawImage(animations[action][aniIndex], hitbox.x, hitbox.y, width, height, null);

        for(WeaponBoss weapon : weapons){
            weapon.render(g);
        }
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;

            if (action == 0 && aniIndex >= 4) {
                aniIndex = 0;
            }
            if(action == 1 && aniIndex >= animations[1].length){
                aniIndex = 6;
                visible = false;
            }
        }
    }
    public void collision(Player player){
        if(player.getHitbox().intersects(hitbox) && this.HP>0){
            player.setHP(0);
        }
        for (WeaponBoss weapon : weapons) {
            if (CheckCollision(weapon.getHitbox(), player.getHitbox())) {
                weapon.setVisible(false);
                playerCollision(player);
            }
        }
        for(int i = 0; i < player.getWeapons().size(); i++){
            Weapon weapon = player.getWeapons().get(i);
            if(CheckCollision(weapon.getBoundRight(), hitbox)){
                weapon.setVisible(false);
                if(System.currentTimeMillis()-timeAttacked >= 500) {
                    this.HP--;
                    timeAttacked = System.currentTimeMillis();
                }
                if(HP == 0){
                    action = 1;
                    aniIndex = aniTick = 0;
                    game.playLevelClear();
                }
            }
        }
    }
    private void playerCollision(Player player){
        player.setHP(player.getHP()-1);
        player.setInAir(false);
        player.jump();
        player.setHit(true);
    }
    private void attacking(Player player){
        int y = player.getHitbox().y;
        int boundTop = y-hitbox.height/3;
        int boundDown = y+hitbox.height/3;
        if(boundDown < hitbox.y) boundDown = hitbox.y+1;
        if(boundTop < hitbox.y) boundTop = hitbox.y;
        if(boundDown > hitbox.y+hitbox.height) boundDown = hitbox.y+hitbox.height;
        if(boundTop > hitbox.y+hitbox.height) boundTop = hitbox.y+hitbox.height-1;

        if(action == 0 && player.getHitbox().x >= hitbox.x - Game.GAME_WIDTH*3/4 && System.currentTimeMillis()-timeAttack >= 1500){
            Random random = new Random();
            weapons.add(new WeaponBoss(hitbox.x, random.nextInt(boundTop, boundDown), (int)(24*Game.SCALE*3), (int)(16*Game.SCALE*3), game, animations[0][4]));
            timeAttack = System.currentTimeMillis();
            game.playBossAttack();
        }
    }

    private void drawUI(Graphics g){
        for(int i = 0; i < HP; i++){
            g.drawImage(imgUI, -game.getCam().getX()+(int)((5.2+i%15)*Game.TILES_SIZE), (i/15)*Game.TILES_SIZE, Game.TILES_SIZE, Game.TILES_SIZE, null);
        }
    }
    private void importQuiz(){
        if(this.HP%8==0 && this.HP != 0){
            game.setQuiz(true);
            game.getQuizManager().setCurr();
            this.HP--;
        }
    }
}
