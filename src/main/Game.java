package main;

import entities.Player;
import levels.manager.QuizManager;
import levels.manager.MapManager;
import helpgame.Constants;

import java.awt.*;


public class Game implements Runnable{
    private final GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;

    private final int sprite = Constants.PlayerSprite.VIRTUAL_GUY;
    private Player player;
    private int amountWeaponCur;
    private Point playerPosition = new Point(0, 0);

    private Camera cam;
    private MapManager mapManager;
    private QuizManager quizManager;
    private boolean quiz = false;
    private Sound soundManager;

    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float SCALE = 1f;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

    public Game(){
        initClasses();

        gamePanel = new GamePanel(this);
        GameWindow gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();

        startGameLoop();
    }

    private void initClasses() {
        mapManager = new MapManager(this);
        mapManager.initMap();
        Point position = mapManager.getCurrentMap().getLevel().beginMap();
        player = new Player((position.x+1)*TILES_SIZE, (position.y-1)*TILES_SIZE, (int)(32*SCALE), (int)(32*SCALE), this);
        player.loadLvlData(mapManager);
        cam = new Camera(0, 0);
        quizManager = new QuizManager(this);
        soundManager = new Sound(this);
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
        soundManager.restartBackground();
    }

    public void update(){
        if(player.isGameOver()) return;

        soundManager.update();
        if(quiz) {
            quizManager.update();
        }else {
            mapManager.update();
            player.update();
            if (mapManager.isHidden()) {
                backAfterHiddenMap();
            } else {
                nextMap();
                backMap();
            }
        }

    }
    public void render(Graphics g){
        cam.draw(player);
        g.translate(cam.getX(), cam.getY());
        mapManager.render(g);
        player.render(cam, g);
        if(quiz){
            quizManager.render(g);
        }
        if(player.isGameOver()){
            drawGameOver(g);
        }
    }

    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;

        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                gamePanel.repaint();
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
            }
        }
    }

    public Player getPlayer(){return player;}
    public MapManager getMapManager(){return mapManager;}
    public int getSprite(){return sprite;}
    public Camera getCam(){return cam;}
    public QuizManager getQuizManager(){return quizManager;}
    public void setQuiz(boolean quiz){this.quiz = quiz;}

    private void drawGameOver(Graphics g){
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRect(-cam.getX(), 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        String msg = "Game Over";

        g.setColor(Color.white);
        g.drawString(msg, (Game.GAME_WIDTH-cam.getX()) / 2, 150);
        msg = "Press esc to enter Main Menu!";
        g.drawString(msg, (Game.GAME_WIDTH-cam.getX()) / 2, 300);
    }

    private void nextMap(){
        Point position = mapManager.getCurrentMap().getLevel().getEnd();
        if(player.getHitbox().x+player.getHitbox().width >= position.x*Game.TILES_SIZE+Game.TILES_SIZE/2
                && !mapManager.isBossExist()){
            if(mapManager.isBoss()) {
                mapManager.setBoss(false);
                player.setWeaponAmount(amountWeaponCur);
                mapManager.setCurrent(mapManager.getCurrent() + 1);
            }else{
                mapManager.setBoss(true);
                amountWeaponCur = player.getWeaponAmount();
                player.setWeaponAmount(100);
            }
            System.out.println("START");
            resetStartPosition();
        }
    }
    private void backMap(){
        boolean ok = false;
        if(mapManager.getCurrent() == 0) return;
        Point position = mapManager.getCurrentMap().getLevel().getBegin();
        if(player.getHitbox().x <= position.x*Game.TILES_SIZE+Game.TILES_SIZE/2 && player.getHitbox().x >= 0
                && !mapManager.isBossExist()){
            if(mapManager.isBoss()) {
                mapManager.setBoss(false);
                player.setWeaponAmount(amountWeaponCur);
                ok = true;
            }else if(mapManager.getCurrent() != 0){
                mapManager.setBoss(true);
                amountWeaponCur = player.getWeaponAmount();
                mapManager.setCurrent(mapManager.getCurrent() - 1);
                ok = true;
            }
            System.out.println("BACK");
            if(ok) resetEndPosition();
        }
    }
    public void nextHiddenMap(){
        playerPosition = new Point(player.getHitbox().x, player.getHitbox().y);
        mapManager.setHidden(true);
        resetStartPosition();
    }
    public void backAfterHiddenMap(){
        Point position = mapManager.getCurrentMap().getLevel().getEnd();
        if(player.getHitbox().x+player.getHitbox().width >= position.x*Game.TILES_SIZE+Game.TILES_SIZE/2){
            mapManager.setHidden(false);
            player.setLocation(playerPosition.x, playerPosition.y);
        }
    }
    private void resetStartPosition(){
        Point position = mapManager.getCurrentMap().getLevel().getBegin();
        player.setLocation((position.x+1)*TILES_SIZE, (position.y-1)*TILES_SIZE);
    }
    private void resetEndPosition(){
        Point position = mapManager.getCurrentMap().getLevel().getEnd();
        player.setLocation((position.x-1)*TILES_SIZE, (position.y-1)*TILES_SIZE);
    }
    public void playCoin() {
        soundManager.playCoin();
    }

    public void playOneUp() {
        soundManager.playOneUp();
    }

    public void playMarioDies() {
        soundManager.pauseBackground();
        soundManager.playMarioDies();
    }

    public void playJump() {
        soundManager.playJump();
    }

    public void playFireball() {
        soundManager.playFireball();
    }

    public void playStomp() {
        soundManager.playStomp();
    }
    public void playBlockBreak(){
        soundManager.playBlockBreak();
    }
    public void playLevelClear(){
        soundManager.playLevelClear();
    }
    public void playAttack(){
        soundManager.playAttack();
    }
    public void playBossAttack(){
        soundManager.playBossAttack();
    }
    public void windowFocusLost(){
        player.reset();
    }
}
