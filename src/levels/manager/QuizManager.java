package levels.manager;

import entities.trap.Quizzes;
import main.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static main.Game.*;

public class QuizManager {
    private final Game game;
    private final ArrayList<Quizzes> quizzes;
    private int curr = 0;

    public QuizManager(Game game){
        this.game = game;
        quizzes = new ArrayList<>();
        importQuizzes();
    }

    private void importQuizzes(){
        for(int i = 0; i < 4; i++){
            quizzes.add(new Quizzes(5*TILES_SIZE, 2*TILES_SIZE, "images/quizzes/cau"+(i+1)+".png", i%4, game));
        }
    }

    public void update(){
        quizzes.get(curr).update(game.getCam());
    }
    public void render(Graphics g){
        quizzes.get(curr).render(g);
    }

    public Quizzes getQuizCurr(){return quizzes.get(curr);}
    public void setCurr(){
        Random random = new Random();
        curr = random.nextInt(0, quizzes.size());
    }
}
