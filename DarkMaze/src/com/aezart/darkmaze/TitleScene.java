package com.aezart.darkmaze;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class TitleScene extends Scene{
	DarkMaze game;
	public TitleScene(DarkMaze game){
		this.game = game;
	}
	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, 608, 480);
		FontMetrics fm = g.getFontMetrics();
		g.setColor(Color.white);
		g.drawString("Press enter to begin.", 304-fm.stringWidth("Press enter to begin.")/2, 256);
	}

	@Override
	public void keyPressed(KeyEvent keyEvent) {
		if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER){
			game.currentScene = new DungeonScene(game);
		}
	}

}
