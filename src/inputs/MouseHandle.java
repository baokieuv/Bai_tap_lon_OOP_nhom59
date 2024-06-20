package inputs;

import main.GamePanel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseHandle implements MouseListener, MouseMotionListener {

	private final GamePanel gamePanel;

	public MouseHandle(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		gamePanel.getGame().getQuizManager().getQuizCurr().mouseDragged(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		gamePanel.getGame().getQuizManager().getQuizCurr().mouseMoved(e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		gamePanel.getGame().getQuizManager().getQuizCurr().mouseClicked(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		gamePanel.getGame().getQuizManager().getQuizCurr().mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		gamePanel.getGame().getQuizManager().getQuizCurr().mouseReleased(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		gamePanel.getGame().getQuizManager().getQuizCurr().mouseEntered(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		gamePanel.getGame().getQuizManager().getQuizCurr().mouseExited(e);
	}

}