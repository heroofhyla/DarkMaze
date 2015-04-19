package com.aezart.darkmaze;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class GameOverScene extends Scene{
	DarkMaze game;
	
	public GameOverScene(DarkMaze game){
		this.game = game;
	}
	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(game.gameOver, 0, 0, null);
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent keyEvent) {
		if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER){
			game.currentScene = new DungeonScene(game);
		}
		
	}

}
