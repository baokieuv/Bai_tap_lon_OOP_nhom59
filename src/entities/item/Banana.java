package entities.item;

import entities.Entity;
import entities.Player;
import main.Game;
import helpgame.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Banana extends Entity {
    private final ItemID id = ItemID.BANANA;
    private final BufferedImage[] animations;
    private int aniTick, aniIndex, aniSpeed = 15;

    public Banana(float x, float y, int width, int height, Game game) {
        super(x, y, width, height, game);
        initHitbox((int)x, (int)y, (int)(12*Game.SCALE), (int)(14*Game.SCALE));
        animations = new BufferedImage[17];
        loadAnimations();
    }

    private void loadAnimations(){
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.BANANAS_ATLAS);
        for(int i = 0; i < animations.length; i++){
            animations[i] = img.getSubimage(i*32, 0, 32, 32);
        }
    }

    public void update(Player player){
        collision(player);
        updateAnimationTick();
    }
    public void render(Graphics g){
        float yOffset = 7 * Game.SCALE;
        float xOffset = 10 * Game.SCALE;
        g.drawImage(animations[aniIndex], (int)(hitbox.x- xOffset), (int)(hitbox.y- yOffset), width, height, null);
    }

    private void updateAnimationTick() {
        aniTick++;

        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;

            if (aniIndex >= animations.length) {
                aniIndex = 0;
            }
        }
    }
    public void collision(Player player){
        if(player.getHitbox().intersects(hitbox)){
            player.getItem(id);
            visible = false;
            game.playCoin();
        }
    }
}
