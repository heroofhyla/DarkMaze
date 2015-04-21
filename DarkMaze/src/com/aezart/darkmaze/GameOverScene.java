package com.aezart.darkmaze;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.sound.sampled.AudioSystem;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class GameOverScene extends Scene{
	DarkMaze game;
	BufferedImage bgImage;
	
	public GameOverScene(DarkMaze game, BufferedImage bgImage){
		game.coinClip.close();
		game.bgmPlayer.close();
		this.game = game;
		this.bgImage = bgImage;
		
	}
	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		g.drawImage(bgImage, 0, 0, null);
		g.drawImage(game.gameOver, 0, 0, null);
		g.setColor(Color.white);
		
		g.drawString("Press enter to play again.", 304 - fm.stringWidth("Press [enter] to play again.")/2, 256);
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent keyEvent) {
		if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER){
			game.currentScene = new DungeonScene(game);
		}
		
	}

}
