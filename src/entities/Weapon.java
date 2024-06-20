package entities;

import main.Game;
import helpgame.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static helpgame.Constants.PlayerSprite.*;
import static helpgame.HelpMethods.CanMove;

public class Weapon extends Entity{
    protected int WEAPON_SPEED = 2;
    protected BufferedImage img;
    protected long timeDis = 0;
    protected int flipX = 0;
    protected int flipW = 0;
    protected int xPos;

    public Weapon(float x, float y, int width, int height, Game game) {
        super(x, y, width, height, game);
        initHitbox((int)x, (int)y, width, height);
        loadAnimations();
        xPos = (int)x;
    }
    public Weapon(float x, float y, int width, int height, Game game, BufferedImage img){
        super(x, y, width, height, game);
        initHitbox((int)x, (int)y, width, height);
        xPos = (int)x;
        this.img = img;
    }
    private void loadAnimations() {
        if(game.getSprite() == MASK_DUDE)
            img= LoadSave.GetSpriteAtlas(LoadSave.WEAPON_MASKDUDE);
        else if (game.getSprite() == NINJA_FROG) {
            if (game.getMapManager().getCurrent() == 1) img = LoadSave.GetSpriteAtlas(LoadSave.WEAPON_NINJA_FROG2);
            else img = LoadSave.GetSpriteAtlas(LoadSave.WEAPON_NINJA_FROG);
            width = hitbox.width = (int) (16 * Game.SCALE);
        }
        else if (game.getSprite() == PINK_MAN) {
            img = LoadSave.GetSpriteAtlas(LoadSave.WEAPON_PINK_MAN);
            height = hitbox.height = (int) (16 * Game.SCALE);
            width = hitbox.width = (int) (1.8 * height);
        }
        else if (game.getSprite() == VIRTUAL_GUY) {
            img = LoadSave.GetSpriteAtlas(LoadSave.WEAPON_VIRTUAL_GUY);
            width = hitbox.width = height = hitbox.height = (int) (16 * Game.SCALE);
        }
    }

    public void update(int[][] greenLvlData){
        if(WEAPON_SPEED < 0){
            flipX = width;
            flipW = -1;
        }else{
            flipX = 0;
            flipW = 1;
        }
        if (CanMove(hitbox.x + WEAPON_SPEED, hitbox.y-hitbox.height, hitbox.width, hitbox.height, greenLvlData)){
            hitbox.x += WEAPON_SPEED;
        }else{
            if(timeDis == 0){
                timeDis = System.currentTimeMillis();
            }
            if(System.currentTimeMillis()-timeDis >= 2000){
                visible = false;
            }
        }
    }
    public void update(){
        if(WEAPON_SPEED < 0){
            flipX = width;
            flipW = -1;
        }else{
            flipX = 0;
            flipW = 1;
        }
        hitbox.x += WEAPON_SPEED;
        if(Math.abs(hitbox.x-xPos) > Game.GAME_WIDTH/3) visible = false;
    }
    public void render(Graphics g){
        g.drawImage(img, hitbox.x + flipX, hitbox.y, hitbox.width*flipW, hitbox.height, null);
        //drawHitbox(g);
    }

    public int getWEAPON_SPEED() {
        return WEAPON_SPEED;
    }
    public void setWEAPON_SPEED(int WEAPON_SPEED) {
        this.WEAPON_SPEED = WEAPON_SPEED;
    }

    public void setImg(String fileName){
        img = LoadSave.GetSpriteAtlas(fileName);
    }
}
