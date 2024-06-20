package entities.item;

import entities.Entity;
import levels.manager.ItemManager;
import entities.Player;
import entities.Weapon;
import main.Game;
import helpgame.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import static entities.item.ItemID.*;

public class Box extends Entity {
    private final ItemID id = ItemID.BOX;
    private final BufferedImage[][] animations;
    private int aniTick = 0, aniIndex = 0, aniSpeed = 30;
    private int aniAc = 1;
    private boolean attacked = false;
    private final float xOffset = 7*Game.SCALE;
    private final float yOffset = 5*Game.SCALE;

    public Box(float x, float y, int width, int height, Game game) {
        super(x, y, width, height, game);
        initHitbox((int)(x+xOffset), (int)(y+yOffset+2), (int)(width-2*xOffset), (int)(height-yOffset));
        animations = new BufferedImage[2][8];
        loadAnimations();
        Random random = new Random();
        aniAc = random.nextInt(0, 2);
    }

    private void loadAnimations(){
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.BOX_ATLAS);
        for(int j = 0; j < animations.length; j++){
            for(int i = 0; i < animations[0].length; i++){
                animations[j][i] = img.getSubimage(i*40, j*30, 40, 30);
            }
        }
    }

    public void update(Player player, ItemManager itemManager){
        updateAnimationTick(itemManager);
        if(!attacked) {
            collision(player.getWeapons());
        }
    }
    public void render(Graphics g){
        g.drawImage(animations[aniAc][aniIndex], (int)(hitbox.x-xOffset), (int)(hitbox.y-yOffset), width, height, null);
        //drawHitbox(g);
    }

    private void updateAnimationTick(ItemManager itemManager) {
        if(!attacked) return;

        aniTick++;
        if (aniTick >= aniSpeed) {
            aniIndex++;
            aniTick = 0;

            if (aniIndex >= animations[0].length) {
                aniIndex = animations[0].length-1;
                visible = false;
                randomItem(itemManager);
            }
        }
    }
    public void collision(ArrayList<Weapon> weapons){
        for (Weapon weapon : weapons) {
            if (weapon.getBoundRight().intersects(hitbox)) {
                weapon.setVisible(false);
                attacked = true;
            }
        }
    }
    private void randomItem(ItemManager itemManager){
        Random random = new Random();
        int tmp = random.nextInt(0, 5);
        if (tmp == 1)
            itemManager.addItem(APPLE, hitbox.x, hitbox.y);
        else if (tmp == 2)
            itemManager.addItem(COIN, hitbox.x, hitbox.y);
        else if (tmp == 3)
            itemManager.addItem(BANANA, hitbox.x, hitbox.y);
        else if (tmp == 4)
            itemManager.addItem(ORANGE, hitbox.x, hitbox.y);
    }
}
