package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class Sound {
	private final Game game;
	private final Clip background;
	private long clipTime = 0;

	public Sound(Game game) {
		this.game = game;
		background = getClip(loadAudio("background"));
	}

	private AudioInputStream loadAudio(String url) {
		try {
			InputStream audioSrc = getClass().getResourceAsStream("/music/" + url + ".wav");
			InputStream bufferedIn = new BufferedInputStream(audioSrc);
			return AudioSystem.getAudioInputStream(bufferedIn);

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		return null;
	}

	private Clip getClip(AudioInputStream stream) {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(stream);
			return clip;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public void update(){
		if(game.getPlayer().getHP()!=0 && !background.isRunning()){
			restartBackground();
		}
	}
	public void resumeBackground(){
		background.setMicrosecondPosition(clipTime);
		background.start();
	}

	public void pauseBackground(){
		clipTime = background.getMicrosecondPosition();
		background.stop();
	}

	public void restartBackground() {
		clipTime = 0;
		resumeBackground();
	}

	public void playJump() {
		Clip clip = getClip(loadAudio("jump"));
        if (clip != null) {
            clip.start();
        }
    }

	public void playCoin() {
		Clip clip = getClip(loadAudio("coin"));
		if (clip != null) {
			clip.start();
		}
	}

	public void playFireball() {
		Clip clip = getClip(loadAudio("fireball"));
		if (clip != null) {
			clip.start();
		}
	}

	public void playStomp() {
		Clip clip = getClip(loadAudio("stomp"));
		if (clip != null) {
			clip.start();
		}
	}

	public void playOneUp() {
		Clip clip = getClip(loadAudio("oneUp"));
		if (clip != null) {
			clip.start();
		}
	}

	public void playMarioDies() {
		Clip clip = getClip(loadAudio("marioDies"));
		if (clip != null) {
			clip.start();
		}
	}
	public void playBlockBreak(){
		Clip clip = getClip(loadAudio("blockbreak"));
		if (clip != null) {
			clip.start();
		}
	}
	public void playLevelClear(){
		Clip clip = getClip(loadAudio("level_clear"));
		if (clip != null) {
			clip.start();
		}
	}
	public void playAttack(){
		Clip clip = getClip(loadAudio("attack"));
		if (clip != null) {
			clip.start();
		}
	}
	public void playBossAttack(){
		Clip clip = getClip(loadAudio("bossAttack"));
		if (clip != null) {
			clip.start();
		}
	}
}
