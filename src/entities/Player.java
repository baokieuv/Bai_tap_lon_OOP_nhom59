package entities;

import entities.item.ItemID;
import levels.manager.MapManager;
import main.Camera;
import main.Game;
import helpgame.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static helpgame.Constants.PlayerConstants.*;
import static helpgame.Constants.PlayerSprite.*;
import static helpgame.HelpMethods.*;

public class Player extends Entity{
    private BufferedImage[][] animations;
    private BufferedImage shieldImg;
    private int aniTick, aniIndex, aniSpeed = 15;
    private int playerAction = 0;
    private boolean moving = false, hit = false, chk = false, attackPressed = false;
    private boolean left, right, jump;
    private float playerSpeed = Game.SCALE;
    private final float xDrawOffset = 6 * Game.SCALE;
    private final float yDrawOffset = 1 * Game.SCALE;
    private final ArrayList<Weapon> weapons;
    private boolean gameOver = false;
    private long timeDamaged = System.currentTimeMillis(), timeDizzied = System.currentTimeMillis();

    // Jumping / Gravity
    private float airSpeed = 0f;
    private final float gravity = 0.04f * Game.SCALE;
    private final float jumpSpeed = -2.25f * Game.SCALE;
    private final float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean inAir = false;

    //Flip
    private int flipX = 0;
    private int flipW = 1;

    //Status
    private BufferedImage[] playerUI;
    private int HP = 9;
    private int COIN = 0;
    private boolean shield = false, dizzied = false;
    private int weapon = 100;

    public Player(float x, float y, int width, int height, Game game) {
        super(x, y, width, height, game);
        loadAnimations();
        initHitbox((int)x, (int)y, (int) (21 * Game.SCALE), (int) (29 * Game.SCALE));
        weapons = new ArrayList<>();
        importPlayerUI();
    }

    private void loadAnimations() {
        ArrayList<String> list = switch (game.getSprite()) {
            case MASK_DUDE -> initAnimationMaskDude();
            case NINJA_FROG -> initAnimationNinjaFrog();
            case PINK_MAN -> initAnimationPinkMan();
            case VIRTUAL_GUY -> initAnimationVirtualGuy();
            default -> new ArrayList<>();
        };

        animations = new BufferedImage[7][12];

        BufferedImage img = LoadSave.GetSpriteAtlas(list.get(0));
        for(int i = 0; i < GetSpriteAmount(APPEARING); i++){
            animations[0][i] = img.getSubimage(i*96, 0, 96, 96);
        }

        img = LoadSave.GetSpriteAtlas(list.get(1));
        for(int j = 0; j < animations.length-2; j++){
            for(int i = 0; i < animations[j].length; i++){
                animations[j+1][i] = img.getSubimage(i*32, j*32, 32, 32);
            }
        }

        img = LoadSave.GetSpriteAtlas(list.get(2));
        for(int i = 0; i < GetSpriteAmount(DISAPPEARING); i++){
            animations[6][i] = img.getSubimage(i*96, 0, 96, 96);
        }

        shieldImg = LoadSave.GetSpriteAtlas(LoadSave.SHIELD);

    }
    public void loadLvlData(MapManager mapManager) {
        super.loadLvlData(mapManager);
        if (!IsEntityOnFloor(hitbox, mapManager.getCurrentMap().getLevel().getGreenLvlData()))
            inAir = true;
    }

    public void update(){
        updatePos();
        updateAnimationTick();
        setAnimation();

        ArrayList<Weapon> weaponRemove = new ArrayList<>();
        for(Weapon weapon : weapons){
            if(weapon.isVisible()){
                weapon.update(mapManager.getCurrentMap().getLevel().getGreenLvlData());
            }else weaponRemove.add(weapon);
        }
        weapons.removeAll(weaponRemove);

        if(System.currentTimeMillis()-timeDizzied >= 1000){
            dizzied = false;
        }
    }
    public void render(Camera cam, Graphics g) {
        g.drawImage(animations[playerAction][aniIndex], (int) (hitbox.x - xDrawOffset + flipX), (int) (hitbox.y - yDrawOffset), width*flipW, height, null);
        if(shield) {
            g.drawImage(shieldImg, (int) (hitbox.x + flipX * 3 / 2 - xDrawOffset * 2 - 3), (int) (hitbox.y - yDrawOffset * 4 - 1), width * flipW * 3 / 2, height * 3 / 2, null);
        }
        //drawHitbox(g);
        for(Weapon weapon : weapons){
            weapon.render(g);
        }
        drawUI_player(cam, g);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;

            if (aniIndex >= GetSpriteAmount(playerAction)) {
                aniIndex = 0;
                if(playerAction == DISAPPEARING){
                    System.out.println(1);
                    aniIndex = GetSpriteAmount(DISAPPEARING)-1;
                    gameOver = true;
                }else {
                    if (!chk) {
                        hit = false;
                    }
                    chk = false;
                }
            }
        }
    }
    private void setAnimation() {
        int startAni = playerAction;
        if(startAni == DISAPPEARING) {
            resetInAir();
            playerSpeed = 0;
            return;
        }

        if (moving)
            playerAction = RUN;
        else
            playerAction = IDLE;

        if (inAir) {
            if (airSpeed < 0) {
                playerAction = JUMP;
            } else
                playerAction = FALL;
        }
        if(hit) playerAction = HIT;
        if (startAni != playerAction) aniTick = aniIndex = 0;
    }
    private void updatePos() {
        moving = false;

        if (jump && !dizzied){
            jump();
        }

        if (!inAir)
            if ((!left && !right) || (right && left))
                return;

        float xSpeed = 0;

        if (left) {
            flipX = width;
            flipW = -1;
            xSpeed -= playerSpeed;
        }
        if (right) {
            flipX = 0;
            flipW = 1;
            xSpeed += playerSpeed;
        }

        if (!inAir) {
            if (!IsEntityOnFloor(hitbox, mapManager.getCurrentMap().getLevel().getGreenLvlData()))
                inAir = true;
        }

        if (inAir) {
            if (CanMove(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, mapManager.getCurrentMap().getLevel().getGreenLvlData())) {
                hitbox.y += airSpeed;
                airSpeed += gravity;
            } else {
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if (airSpeed > 0)
                    resetInAir();
                else
                    airSpeed = fallSpeedAfterCollision;
            }

        }

        if(!dizzied) {
            updateXPos(xSpeed);
        }
        moving = true;
    }
    private void updateXPos(float xSpeed) {
        if (CanMove(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, mapManager.getCurrentMap().getLevel().getGreenLvlData()))
            hitbox.x += (int) xSpeed;
        else
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
    }

    public void jump() {
        if (inAir) {
            return;
        }
        inAir = true;
        airSpeed = jumpSpeed;
        game.playJump();
    }
    private void attack(){
        Weapon temp = new Weapon(hitbox.x, hitbox.y + (float) hitbox.height /2, (int)(32*Game.SCALE), (int)(16*Game.SCALE), this.game);
        if(flipW == -1){
            temp.setWEAPON_SPEED(-temp.getWEAPON_SPEED());
        }else{
            temp.getHitbox().x = hitbox.x + hitbox.width;
        }
        weapons.add(temp);
        //this.weapon--;
        game.playAttack();
    }
    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }
    public void collisionBrick(){
        airSpeed = fallSpeedAfterCollision;
    }

    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W)
            jump = false;
        else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A)
            left = false;
        else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D)
            right = false;
        else if (key == KeyEvent.VK_SPACE)
            attackPressed = false;
    }
    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W)
            jump = true;
        else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A)
            left = true;
        else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D)
            right = true;
        else if (key == KeyEvent.VK_SPACE) {
            if(!attackPressed){
                attackPressed = true;
                if(this.weapon == 0) return;
                if(!dizzied)
                    attack();
            }
        }
    }

    public int[][]getLvlData(){return mapManager.getCurrentMap().getLevel().getLevelData();}
    public int[][]getGreenLvlData(){return mapManager.getCurrentMap().getLevel().getGreenLvlData();}
    public int getHP(){return HP;}
    public void setHP(int HP){
        if(this.HP <= 0) return;
        if(shield && HP < this.HP){
            shield = false;
            timeDamaged = System.currentTimeMillis();
            return;
        }
        if(System.currentTimeMillis() - timeDamaged <= 1000){
            return;
        }
        if(HP == 0) {
            playerAction = DISAPPEARING;
            aniTick = aniIndex = 0;
            aniSpeed = 30;
            game.playMarioDies();
        }
        if(HP > 9) HP = 9;
        //this.HP = HP;
        timeDamaged = System.currentTimeMillis();
    }

    public void setInAir(boolean inAir){this.inAir = inAir;}
    public ArrayList<Weapon> getWeapons(){return weapons;}
    public int getWeaponAmount(){return weapon;}
    public void setWeaponAmount(int weapon){this.weapon = weapon;}

    public boolean isGameOver(){return gameOver;}
    public void setHit(boolean hit){
        this.hit = hit;
        this.chk = hit;
    }
    public void setLocation(float x, float y){
        hitbox.x = (int)x;
        hitbox.y = (int)y;
    }

    public void getItem(ItemID item){
        switch (item){
            case APPLE:
                this.HP += 1;
                if(this.HP > 9) this.HP = 9;
                break;
            case COIN:
                this.COIN += 1;
                break;
            case BANANA:
                this.shield = true;
                break;
            case ORANGE:
                this.weapon += 5;
                break;
        }
    }

    public void drawUI_player(Camera cam, Graphics g){
        g.drawImage(playerUI[0], -cam.getX(), 0, Game.TILES_SIZE, Game.TILES_SIZE, null);
        g.drawImage(playerUI[2], -cam.getX()+Game.TILES_SIZE, 0, Game.TILES_SIZE, Game.TILES_SIZE, null);
        g.drawImage(playerUI[HP+5], -cam.getX()+2*Game.TILES_SIZE, 0, Game.TILES_SIZE, Game.TILES_SIZE, null);

        g.drawImage(playerUI[1], -cam.getX(), Game.TILES_SIZE, Game.TILES_SIZE, Game.TILES_SIZE, null);
        g.drawImage(playerUI[2], -cam.getX()+Game.TILES_SIZE, Game.TILES_SIZE, Game.TILES_SIZE, Game.TILES_SIZE, null);

        char[] coin = (Integer.toString(COIN)).toCharArray();
        for(int i = 0; i < coin.length; i++) {
            g.drawImage(playerUI[coin[i]-'0' + 5], -cam.getX() + (2+i) * Game.TILES_SIZE, Game.TILES_SIZE, Game.TILES_SIZE, Game.TILES_SIZE, null);
        }

        if(shield){
            g.drawImage(playerUI[3], -cam.getX(), 2*Game.TILES_SIZE, Game.TILES_SIZE, Game.TILES_SIZE, null);
        }

        g.drawImage(playerUI[4], -cam.getX()+Game.TILES_SIZE, 2*Game.TILES_SIZE, Game.TILES_SIZE, Game.TILES_SIZE, null);
        g.drawImage(playerUI[2], -cam.getX()+2*Game.TILES_SIZE, 2*Game.TILES_SIZE, Game.TILES_SIZE, Game.TILES_SIZE, null);

        char[] weapon = (Integer.toString(this.weapon)).toCharArray();
        for(int i = 0; i < weapon.length; i++) {
            g.drawImage(playerUI[weapon[i]-'0' + 5], -cam.getX() + (3+i) * Game.TILES_SIZE, 2 * Game.TILES_SIZE, Game.TILES_SIZE, Game.TILES_SIZE, null);
        }
    }
    private void importPlayerUI(){
        playerUI = new BufferedImage[35];
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        playerUI[0] = img.getSubimage(4*18, 2*18, 18, 18);
        playerUI[1] = img.getSubimage(11*18, 7*18, 18, 18);
        playerUI[2] = img.getSubimage(18*18, 7*18, 18, 18);
        playerUI[3] = LoadSave.GetSpriteAtlas(LoadSave.SHIELD);

        if (game.getSprite() == MASK_DUDE)
            playerUI[4] = LoadSave.GetSpriteAtlas(LoadSave.WEAPON_MASKDUDE);
        else if (game.getSprite() == NINJA_FROG)
            playerUI[4] = LoadSave.GetSpriteAtlas(LoadSave.WEAPON_NINJA_FROG);
        else if (game.getSprite() == PINK_MAN)
            playerUI[4] = LoadSave.GetSpriteAtlas(LoadSave.WEAPON_PINK_MAN);
        else if (game.getSprite() == VIRTUAL_GUY)
            playerUI[4] = LoadSave.GetSpriteAtlas(LoadSave.WEAPON_VIRTUAL_GUY);

        for(int i = 0; i < 20; i++){
            playerUI[i+5] = img.getSubimage(i*18, 8*18, 18, 18);
        }
    }

    private ArrayList<String> initAnimationNinjaFrog(){
        ArrayList<String> list = new ArrayList<>();
        list.add(LoadSave.PLAYER_APPEARING_ATLAS);
        list.add(LoadSave.NINJA_FROG_ATLAS);
        list.add(LoadSave.PLAYER_DISAPPEARING_ATLAS);
        return list;
    }
    private ArrayList<String> initAnimationMaskDude(){
        ArrayList<String> list = new ArrayList<>();
        list.add(LoadSave.PLAYER_APPEARING_ATLAS);
        list.add(LoadSave.MASK_DUDE_ATLAS);
        list.add(LoadSave.PLAYER_DISAPPEARING_ATLAS);
        return list;
    }
    private ArrayList<String> initAnimationPinkMan(){
        ArrayList<String> list = new ArrayList<>();
        list.add(LoadSave.PLAYER_APPEARING_ATLAS);
        list.add(LoadSave.PINK_MAN_ATLAS);
        list.add(LoadSave.PLAYER_DISAPPEARING_ATLAS);
        return list;
    }
    private ArrayList<String> initAnimationVirtualGuy(){
        ArrayList<String> list = new ArrayList<>();
        list.add(LoadSave.PLAYER_APPEARING_ATLAS);
        list.add(LoadSave.VIRTUAL_GUY_ATLAS);
        list.add(LoadSave.PLAYER_DISAPPEARING_ATLAS);
        return list;
    }
    public void setDizzied(boolean dizzied){
        if(shield && dizzied){
            shield = false;
            timeDizzied = System.currentTimeMillis();
            return;
        }
        if(System.currentTimeMillis()-timeDizzied <= 1000) return;
        this.dizzied = dizzied;
        timeDizzied = System.currentTimeMillis();
    }
    public void reset(){
        moving = left = right = false;
        resetInAir();
    }
}
