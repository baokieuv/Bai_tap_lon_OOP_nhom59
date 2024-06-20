package entities.trap;

import entities.Entity;
import entities.Player;
import main.Game;
import helpgame.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static helpgame.HelpMethods.*;

public class Gate extends Entity {
    private final TrapID id = TrapID.GATE;
    private BufferedImage[] animations;
    private int aniTick, aniIndex, aniSpeed = 30;
    private final float xOffset = 8* Game.SCALE;

    public Gate(float x, float y, int width, int height, Game game) {
        super(x, y, width, height, game);
        initHitbox((int)x, (int)y, (int)(width-2*xOffset), height);
        loadAnimations();
    }

    private void loadAnimations() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.GATE_ATLAS);
        animations = new BufferedImage[9];
        for(int i = 0; i < animations.length; i++){
            animations[i] = img.getSubimage(i*498, 0, 498, 498);
        }
    }

    public boolean update(Player player){
        updateAnimationTick();
        return collision(player);
    }
    public void render(Graphics g){
        g.drawImage(animations[aniIndex], (int)(hitbox.x-xOffset), hitbox.y, width, height, null);
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
    private boolean collision(Player player){
        if(CheckCollision(player.getHitbox(), hitbox)){
            visible = false;
        }
        return !visible;
    }
}
