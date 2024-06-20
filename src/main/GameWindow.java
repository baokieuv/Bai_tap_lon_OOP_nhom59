package main;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;

public class GameWindow {
    public GameWindow(GamePanel gamePanel) {
        JFrame jframe = new JFrame();

        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(gamePanel);
        jframe.setResizable(false);
        jframe.pack();
        //jframe.setLocation(0, 0);
        jframe.setLocationRelativeTo(null);
        jframe.setVisible(true);
        ImageIcon ii = new ImageIcon("/images/Logo.png");
        jframe.setIconImage(ii.getImage());
        jframe.addWindowFocusListener(new WindowFocusListener() {

            @Override
            public void windowLostFocus(WindowEvent e) {
                gamePanel.getGame().windowFocusLost();
            }

            @Override
            public void windowGainedFocus(WindowEvent e) {
                // TODO Auto-generated method stub

            }
        });
    }
}
