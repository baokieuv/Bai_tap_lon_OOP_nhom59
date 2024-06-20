package main;

import inputs.KeyboardHandle;
import inputs.MouseHandle;

import javax.swing.*;
import java.awt.*;

import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;

public class GamePanel extends JPanel {
    private final Game game;
    public GamePanel(Game game) {
        MouseHandle mouseHandle = new MouseHandle(this);
        this.game = game;
        setPanelSize();
        addKeyListener(new KeyboardHandle(this));
        addMouseListener(mouseHandle);
        addMouseMotionListener(mouseHandle);
    }

    private void setPanelSize() {
        Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
        setPreferredSize(size);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.render(g);
    }

    public Game getGame() {
        return game;
    }
}
