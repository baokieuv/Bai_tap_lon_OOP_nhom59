package inputs;

import main.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardHandle implements KeyListener {

	private final GamePanel gamePanel;

	public KeyboardHandle(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {
		gamePanel.getGame().getPlayer().keyReleased(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		gamePanel.getGame().getPlayer().keyPressed(e);
	}
}