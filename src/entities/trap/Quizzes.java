package entities.trap;

import main.Camera;
import main.Game;
import helpgame.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static main.Game.TILES_SIZE;
import static helpgame.HelpMethods.CheckCollision;

class Answer{
    private final static int A_WIDTH = Quizzes.Q_WIDTH/2;
    private final static int A_HEIGHT = Quizzes.Q_HEIGHT/4;
    private final Rectangle rect;

    private boolean mouseOver;
    private boolean mousePressed;
    private final boolean correct;

    public Answer(int x, int y, boolean correct){
        this.correct = correct;
        rect = new Rectangle(x, y, A_WIDTH, A_HEIGHT);
    }

    public void render(Graphics g){
        if(mouseOver) {
            g.setColor(Color.MAGENTA);
            g.drawRect(rect.x, rect.y, A_WIDTH, A_HEIGHT);
        }
        if(mousePressed){
            if(correct) {
                g.setColor(Color.GREEN);
            }else g.setColor(Color.RED);
            g.drawRect(rect.x, rect.y, A_WIDTH, A_HEIGHT);
        }
    }

    public void setX(int x){rect.x = x;}
    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }
    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public boolean isIn(int x, int y){
        return CheckCollision(rect, new Rectangle(x, y, 1, 1));
    }
    public void resetBooleans(){
        mouseOver = mousePressed = false;
    }
    public boolean getCorrect(){
        boolean tmp = mousePressed;
        mousePressed = false;
        return tmp && correct;
    }
}

public class Quizzes {
    private final Game game;
    private int x;
    private int y;
    private final static int Q_TILES_IN_WIDTH = 16;
    private final static int Q_TILES_IN_HEIGHT = 10;
    protected final static int Q_WIDTH = TILES_SIZE * Q_TILES_IN_WIDTH;
    protected final static int Q_HEIGHT = TILES_SIZE * Q_TILES_IN_HEIGHT;
    private boolean press = false;
    private long timePress;

    private final BufferedImage img;
    private final ArrayList<Answer> answers = new ArrayList<>();

    public Quizzes(int x, int y, String filePath, int correctAns, Game game){
        this.x = x;
        this.y = y;
        this.img = LoadSave.GetSpriteAtlas(filePath);
        this.game = game;
        for(int i = 0; i < 4; i++){
            int y1 = y + (i <= 1 ? Q_HEIGHT / 2 : Q_HEIGHT * 3 / 4);
            if(i == correctAns)
                answers.add(new Answer(x+i%2*Q_WIDTH/2, y1, true));
            else
                answers.add(new Answer(x+i%2*Q_WIDTH/2, y1, false));
        }
    }

    public void update(Camera cam){
        this.x = -cam.getX()+5*TILES_SIZE;
        this.y = -cam.getY()+2*TILES_SIZE;
        for(int i = 0; i < 4; i++){
            answers.get(i).setX(this.x+i%2*Q_WIDTH/2);
        }
        setCorrect();
    }
    public void render(Graphics g){
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRect(x-5* TILES_SIZE, y-2* TILES_SIZE, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        g.drawImage(img, x, y, Q_WIDTH, Q_HEIGHT, null);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.YELLOW);
        g2d.draw(new Rectangle(x, y, Q_WIDTH, Q_HEIGHT));
        for(Answer ans : answers){
            ans.render(g);
        }
    }

    private boolean getCorrect(){
        for(Answer ans : answers){
            if(ans.getCorrect()) return true;
        }
        return false;
    }
    public void setCorrect(){
        if(press && System.currentTimeMillis()-timePress>=500){
            if(getCorrect()){
                game.getPlayer().setHP(game.getPlayer().getHP()+1);
            }else{
                game.getPlayer().setHP(game.getPlayer().getHP()-1);
                game.getPlayer().setInAir(false);
                game.getPlayer().jump();
                game.getPlayer().setHit(true);
            }
            game.setQuiz(false);
            press = false;
        }
    }

    public void mouseDragged(MouseEvent e) {
    }
    public void mouseMoved(MouseEvent e) {
        for(Answer ans : answers){
            ans.setMouseOver(ans.isIn(e.getX()-game.getCam().getX(), e.getY()));
        }
    }
    public void mouseClicked(MouseEvent e) {
    }
    public void mousePressed(MouseEvent e) {
        for(Answer ans : answers){
            if(ans.isIn(e.getX()-game.getCam().getX(), e.getY())){
                ans.setMousePressed(true);
                press = true;
                timePress = System.currentTimeMillis();
            }
        }
    }
    public void mouseReleased(MouseEvent e) {
    }
    public void mouseEntered(MouseEvent e) {
    }
    public void mouseExited(MouseEvent e) {
    }
}
