package helpgame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class LoadSave {
    /*************************************************************************************************/
    public final static String PLAYER_APPEARING_ATLAS = "images/Mask Dude/Appearing (96x96).png";
    public final static String MASK_DUDE_ATLAS = "images/Mask Dude/mask_dude.png";
    public final static String PLAYER_DISAPPEARING_ATLAS = "images/Mask Dude/Disappearing (96x96).png";
    public final static String WEAPON_MASKDUDE = "images/Mask Dude/weapon.png";
    /***********************************************************************************************/
    public final static String NINJA_FROG_ATLAS = "images/Ninja_Frog/ninja_frog.png";
    public final static String WEAPON_NINJA_FROG = "images/Ninja_Frog/weapon.png";
    public final static String WEAPON_NINJA_FROG2 = "images/Ninja_Frog/weapon2.png";
    /***********************************************************************************************/
    public final static String PINK_MAN_ATLAS = "images/Pink_Man/pink_man.png";
    public final static String WEAPON_PINK_MAN = "images/Pink_Man/weapon.png";
    /***********************************************************************************************/
    public final static String VIRTUAL_GUY_ATLAS = "images/Virtual_Guy/virtual_guy.png";
    public final static String WEAPON_VIRTUAL_GUY = "images/Virtual_Guy/weapon.png";
    /***********************************************************************************************/
    public final static String BUG_RUN_ATLAS = "images/trap/Bug/bug.png";
    public final static String BUG_HIT_ATLAS = "images/trap/Bug/bug_hit.png";
    /***********************************************************************************************/
    public final static String BAT_FLY_ATLAS = "images/trap/Bat/bat.png";
    public final static String BAT_HIT_ATLAS = "images/trap/Bat/bat_hit.png";
    /***********************************************************************************************/
    public final static String ROCK_HEAD_BLINK_ATLAS = "images/trap/Rock Head/Blink.png";
    public final static String ROCK_HEAD_HIT_ATLAS = "images/trap/Rock Head/Bottom Hit (42x42).png";
    /***********************************************************************************************/
    public final static String TURTLE_ATLAS = "images/trap/Turtle/turtle.png";
    /***********************************************************************************************/
    public final static String TOTEM_ATLAS = "images/trap/totems/totem.png";
    public final static String TOTEM_WEAPON = "images/trap/totems/weapon.png";
    /***********************************************************************************************/
    public final static String BOSS_ATLAS = "images/trap/Boss/boss.png";
    public final static String BOSS2_ATLAS = "images/trap/Boss/boss2.png";
    public final static String BOSS_DISAPPEARING = "images/trap/Boss/Disappearing.png";
    /***********************************************************************************************/
    public final static String BROKENBRICK_ATLAS = "images/trap/Brick/break.png";
    public final static String BROKENBRICK2_ATLAS = "images/trap/Brick/break2.png";
    /***********************************************************************************************/
    public final static String GATE_ATLAS = "images/trap/gate.png";
    /***********************************************************************************************/
    public final static String APPLE_ATLAS = "images/item/Apple.png";
    public final static String BANANAS_ATLAS = "images/item/Bananas.png";
    public final static String COIN_ATLAS = "images/item/coin.png";
    public final static String BOX_ATLAS = "images/item/box.png";
    public final static String ORANGE_ATLAS = "images/item/Orange.png";
    /***********************************************************************************************/
    public final static String SHIELD = "images/item/Shield.png";
    /***********************************************************************************************/
    public final static String LEVEL_ATLAS = "images/tilemap/tilemap_packed.png";
    public final static String LEVEL_BW_ATLAS = "images/tilemap/monochrome_tilemap_transparent.png";
    public final static String LEVEL_INSULATION_ATLAS = "images/tilemap/tilemap_packed_industrial.png";
    public final static String LEVEL_FOOD_ATLAS = "images/tilemap/tilemap_packed_food.png";
    /***********************************************************************************************/
    public final static String TEST_GAME_DATA = "images/level/testgame.png";
    public final static String LEVEL_ONE_DATA = "images/level/level_one.png";
    public final static String LEVEL_ONE_HIDDEN_DATA = "images/level/level_one_hidden.png";
    public final static String LEVEL_BOSS_ONE_DATA = "images/level/boss1.png";
    public final static String LEVEL_TWO_DATA = "images/level/level_two.png";
    public final static String LEVEL_TWO_HIDDEN_DATA = "images/level/level_two_hidden.png";
    public final static String LEVEL_BOSS_TWO_DATA = "images/level/boss2.png";
    public final static String LEVEL_FOUR_DATA = "images/level/level_four.png";
    public final static String LEVEL_THREE_DATA = "images/level/level_three.png";
    public final static String LEVEL_THREE_HIDDEN_DATA = "images/level/level_three_hidden.png";
    public final static String LEVEL_BOSS_THREE_DATA = "images/level/boss3.png";
    /***********************************************************************************************/
    public final static String BACKGROUND_ATLAS = "images/backgrounds_packed.png";
    public final static String BACKGROUND_INTELLIJ = "images/Logo.png";
    /***********************************************************************************************/

    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
        try {
            img = ImageIO.read(is);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }

    public static int[][] GetLevelData(String fileName) {
        BufferedImage img = GetSpriteAtlas(fileName);
        int[][] lvlData = new int[img.getHeight()][img.getWidth()];

        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getRed() + 255*color.getBlue();
                if(color.getBlue() > 1) value -= 255*color.getBlue();
                if (value >= 401)
                    value = 0;
                lvlData[j][i] = value;
            }
        return lvlData;
    }
    public static int[][] GetGreenLevelData(String fileName) {
        BufferedImage img = GetSpriteAtlas(fileName);
        int[][] greenLvlData = new int[img.getHeight()][img.getWidth()];

        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                greenLvlData[j][i] = value;
            }
        return greenLvlData;
    }
}
