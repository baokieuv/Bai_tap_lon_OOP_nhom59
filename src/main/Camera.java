package main;

import entities.Player;

public class Camera {
    private int x;
    private final int y;

    public Camera(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void draw(Player player){
        if(player.getHitbox().x <= Game.GAME_WIDTH/2) x = 0;
        else if(player.getHitbox().x >= player.getLvlData()[0].length*Game.TILES_SIZE - Game.GAME_WIDTH/2){
            x = -player.getLvlData()[0].length*Game.TILES_SIZE + Game.GAME_WIDTH;
        }
        else x = -player.getHitbox().x+Game.GAME_WIDTH/2;
    }

    public int getX(){return x;}
    public int getY(){return y;}
}
