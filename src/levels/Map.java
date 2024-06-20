package levels;

import levels.manager.ItemManager;
import levels.manager.TrapManager;
import main.Game;
import helpgame.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Map {
    private final int lvl;
    private final Level level;
    private final ItemManager itemManager;
    private final TrapManager trapManager;
    private BufferedImage[] levelSprite;
    private BufferedImage[][] backgrounds_spring;
    public Map(Game game, String fileNameLvl, int lvl){
        this.lvl = lvl;
        level = new Level(LoadSave.GetLevelData(fileNameLvl), LoadSave.GetGreenLevelData(fileNameLvl));
        importBackgroundSpring();
        itemManager = new ItemManager(game, this);
        trapManager = new TrapManager(game, this);
        importOutside();
    }

    public void update(){
        itemManager.update();
        trapManager.update();
    }
    public void render(Graphics g){
        draw(g);
        trapManager.render(g);
        itemManager.render(g);
    }

    public void draw(Graphics g) {
        if(lvl == 2) drawBackgroundBW(g);
        else drawBackgroundSpring(g);

        for (int j = 0; j < Game.TILES_IN_HEIGHT; j++)
            for (int i = 0; i < level.getLevelData()[0].length; i++) {
                int index = level.getSpriteIndex(i, j);
                g.drawImage(levelSprite[index], Game.TILES_SIZE * i, Game.TILES_SIZE * j, Game.TILES_SIZE, Game.TILES_SIZE, null);
            }
    }
    public void drawBackgroundSpring(Graphics g){
        for(int j = 0; j < Game.GAME_HEIGHT/3; j++){
            for(int i = 0; i < level.getLevelData()[0].length/4; i++){
                int j2 = j%3, i2 = i%4;
                g.drawImage(backgrounds_spring[j2][i2], i*Game.GAME_HEIGHT/3, j2*Game.GAME_HEIGHT/3, Game.GAME_HEIGHT/3,Game.GAME_HEIGHT/3, null);
            }
        }
    }
    private void importBackgroundSpring() {
        backgrounds_spring = new BufferedImage[3][4];
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_ATLAS);
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 4; i++) {
                backgrounds_spring[j][i] = img.getSubimage(i * 24, j * 24, 24, 24);
            }
        }
    }

    private void drawBackgroundBW(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, level.getLevelData()[0].length*Game.TILES_SIZE, Game.GAME_HEIGHT);
    }
   
    public ItemManager getItemManager() {
        return itemManager;
    }
    public Level getLevel(){return level;}

    private void importOutside(){
        if(lvl == 1 || lvl == 0){
            importOutsideLvl1();
        }else if(lvl == 2){
            importOutsideLvl2();
        }else if(lvl == 3){
            importOutsideLvl3();
        }else if(lvl == 4){
            importOutsideLvl4();
        }
    }
    private void importOutsideLvl1(){
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[181];
        for (int j = 0; j < 9; j++)
            for (int i = 0; i < 20; i++) {
                int index = j * 20 + i;
                levelSprite[index] = img.getSubimage(i * 18, j * 18, 18, 18);
            }
    }
    private void importOutsideLvl2(){
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_BW_ATLAS);
        levelSprite = new BufferedImage[401];
        for (int j = 0; j < 20; j++)
            for (int i = 0; i < 20; i++) {
                int index = j * 20 + i;
                levelSprite[index] = img.getSubimage(i * 16, j * 16, 16, 16);
            }
    }
    private void importOutsideLvl3(){
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_FOOD_ATLAS);
        levelSprite = new BufferedImage[181];
        for (int j = 0; j < 7; j++)
            for (int i = 0; i < 16; i++) {
                int index = j * 16 + i;
                levelSprite[index] = img.getSubimage(i * 18, j * 18, 18, 18);
            }
    }
    private void importOutsideLvl4(){
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_INSULATION_ATLAS);
        levelSprite = new BufferedImage[181];
        for (int j = 0; j < 7; j++)
            for (int i = 0; i < 16; i++) {
                int index = j * 16 + i;
                levelSprite[index] = img.getSubimage(i * 18, j * 18, 18, 18);
            }
    }
    public boolean bossExist(){
        return trapManager.getBoss() != null;
    }
}
