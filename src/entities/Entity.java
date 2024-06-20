package entities;

import levels.manager.MapManager;
import main.Game;

import java.awt.*;

public abstract class Entity {
    protected float x, y;
    protected int width, height;
    protected boolean visible = true;
    protected Rectangle hitbox;
    protected MapManager mapManager;
    protected Game game;

    public Entity(float x, float y, int width, int height, Game game) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.game = game;
    }

    public void drawHitbox(Graphics g){
        g.setColor(Color.BLACK);
        g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        g.setColor(Color.MAGENTA);
        Graphics2D g2d = (Graphics2D) g;
        g2d.draw(getBoundLeft());
        g2d.draw(getBoundRight());
        g2d.draw(getBoundTop());
        g2d.draw(getBoundDown());
    }
    public void initHitbox(int x, int y, int width, int height){
        hitbox = new Rectangle(x, y, width, height);
    }
    public void loadLvlData(MapManager mapManager) {
        this.mapManager = mapManager;
    }

    public Rectangle getHitbox(){return hitbox;}
    public Rectangle getBoundTop(){
        return new Rectangle(hitbox.x,
                hitbox.y,
                hitbox.width,
                hitbox.height/3);
    }
    public Rectangle getBoundDown(){
        return new Rectangle(hitbox.x,
                hitbox.y + hitbox.height*3/4,
                hitbox.width,
                hitbox.height/3);
    }
    public Rectangle getBoundRight(){
        return new Rectangle(hitbox.x+hitbox.width - 5,
                hitbox.y + 5,
                5,
                hitbox.height - 10);
    }
    public Rectangle getBoundLeft(){
        return new Rectangle(hitbox.x,
                hitbox.y + 5,
                5,
                hitbox.height - 10);
    }
    public boolean isVisible(){return visible;}
    public void setVisible(boolean visible){this.visible = visible;}
}
