package levels.manager;

import entities.item.*;
import levels.Map;
import main.Game;

import java.awt.*;
import java.util.ArrayList;

import static entities.item.ItemID.*;

public class ItemManager {
    private final Game game;
    private final Map map;
    private final ArrayList<Apple> apples;
    private final ArrayList<Coin> coins;
    private final ArrayList<Box> boxes;
    private final ArrayList<Banana> bananas;
    private final ArrayList<Orange> oranges;

    public ItemManager(Game game, Map map){
        this.game = game;
        this.map = map;
        apples = new ArrayList<>();
        coins = new ArrayList<>();
        boxes = new ArrayList<>();
        bananas = new ArrayList<>();
        oranges = new ArrayList<>();
        initItem();
    }

    public void update(){
        ArrayList<Apple> appleRemove = new ArrayList<>();
        for(Apple apple : apples){
            if(apple.isVisible()){
                apple.update(game.getPlayer());
            }else appleRemove.add(apple);
        }
        apples.removeAll(appleRemove);

        ArrayList<Coin> coinRemove = new ArrayList<>();
        for(Coin coin : coins){
            if(coin.isVisible()){
                coin.update(game.getPlayer());
            }else coinRemove.add(coin);
        }
        coins.removeAll(coinRemove);

        ArrayList<Box> boxRemove = new ArrayList<>();
        for(Box box : boxes){
            if(box.isVisible()){
                box.update(game.getPlayer(), this);
            }else boxRemove.add(box);
        }
        boxes.removeAll(boxRemove);

        ArrayList<Banana> bananaRemove = new ArrayList<>();
        for(Banana banana : bananas){
            if(banana.isVisible()){
                banana.update(game.getPlayer());
            }else bananaRemove.add(banana);
        }
        bananas.removeAll(bananaRemove);

        ArrayList<Orange> orangeRemove = new ArrayList<>();
        for(Orange orange : oranges){
            if(orange.isVisible()){
                orange.update(game.getPlayer());
            }else orangeRemove.add(orange);
        }
        oranges.removeAll(orangeRemove);
    }
    public void render(Graphics g){
        for (Apple apple : apples) {
            apple.render(g);
        }
        for (Coin coin : coins) {
            coin.render(g);
        }
        for (Box box : boxes) {
            box.render(g);
        }
        for(Banana banana : bananas){
            banana.render(g);
        }
        for(Orange orange : oranges){
            orange.render(g);
        }
    }

    public void initItem(){
        int tile;
        for (int j = 0; j < map.getLevel().getLevelData().length; j++)
            for (int i = 0; i < map.getLevel().getLevelData()[0].length; i++) {
                tile = map.getLevel().getGreenLvlData()[j][i];
                if (tile == 50)
                    addItem(APPLE, i*Game.TILES_SIZE, j*Game.TILES_SIZE);
                else if (tile == 51)
                    addItem(COIN, i*Game.TILES_SIZE, j*Game.TILES_SIZE);
                else if (tile == 52)
                    addItem(BOX, i*Game.TILES_SIZE, j*Game.TILES_SIZE);
                else if(tile == 53)
                    addItem(BANANA, i*Game.TILES_SIZE, j*Game.TILES_SIZE);
                else if(tile == 54)
                    addItem(ORANGE, i*Game.TILES_SIZE, j*Game.TILES_SIZE);
            }
    }
    public void addItem(ItemID id, int x, int y){
        if (id == APPLE)
            apples.add(new Apple(x, y, (int)(32*Game.SCALE), (int)(32*Game.SCALE), game));
        else if (id == COIN)
            coins.add(new Coin(x, y, (int)(11*Game.SCALE), (int)(11*Game.SCALE), game));
        else if (id == BOX)
            boxes.add(new Box(x, y, (int)(40*Game.SCALE), (int)(30*Game.SCALE), game));
        else if (id == BANANA)
            bananas.add(new Banana(x, y, (int)(32*Game.SCALE), (int)(32*Game.SCALE), game));
        else if (id == ORANGE)
            oranges.add(new Orange(x, y, (int)(32*Game.SCALE), (int)(32*Game.SCALE), game));
    }
}
