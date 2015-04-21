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
		g.drawString("(Title screen goes here when I think of a name for the game)", 304-fm.stringWidth("(Title screen goes here when I think of a name for the game)")/2, 256);
		g.drawString("\"Oppressive Gloom\" Kevin MacLeod (incompetech.com)",0,460);
		g.drawString("Licensed under Creative Commons: By Attribution 3.0",0, 470); 
		g.drawString("http://creativecommons.org/licenses/by/3.0/",0,480);
		g.drawString("MP3 playback via JLayer library, see LGPL license", 608 - fm.stringWidth("MP3 playback via JLayer library, see LGPL license"), 480);
	}

	@Override
	public void keyPressed(KeyEvent keyEvent) {
		if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER){
			game.currentScene = new DungeonScene(game);
		}
	}

}
