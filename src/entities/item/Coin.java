package entities.item;

import entities.Entity;
import entities.Player;
import main.Game;
import helpgame.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Coin extends Entity {
    private final ItemID id = ItemID.COIN;
    private final BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 30;
    private int aniAc = 1;

    public Coin(float x, float y, int width, int height, Game game) {
        super(x, y, width, height, game);
        initHitbox((int)x, (int)y, width, height);
        animations = new BufferedImage[3][4];
        loadAnimations();
        Random random = new Random();
        aniAc = random.nextInt(1, 3);
    }

    private void loadAnimations(){
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.COIN_ATLAS);
        for(int j = 0; j < animations.length; j++){
            for(int i = 0; i < animations[0].length; i++){
                animations[j][i] = img.getSubimage(i*11, j*11, 11, 11);
            }
        }
    }

    public void update(Player player){
        collision(player);
        updateAnimationTick();
    }
    public void render(Graphics g){
        g.drawImage(animations[aniAc][aniIndex], hitbox.x, hitbox.y, width, height, null);
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
    public void collision(Player player){
        if(player.getHitbox().intersects(hitbox)){
            game.playCoin();
            player.getItem(id);
            visible = false;
        }
    }
}
