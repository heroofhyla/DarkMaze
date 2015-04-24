package com.aezart.darkmaze;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class TransitionScene extends Scene{
	//measured in ~30fps frames
	int sceneTimer = 60;
	DungeonScene dungeonScene;
	DarkMaze game;
	
	public TransitionScene(DungeonScene dungeonScene, DarkMaze game){
		this.dungeonScene = dungeonScene;
		this.game = game;
	}
	@Override
	public void tick() {
		--sceneTimer;
		if (sceneTimer < 0){
			game.currentScene = dungeonScene;
		}
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, 608, 480);
		FontMetrics fm = g.getFontMetrics();
		g.setColor(Color.white);
		g.drawString("Depth: " + dungeonScene.level*10 + "ft", 304 - fm.stringWidth("Depth: " + dungeonScene.level*10 + "ft")/2, 256);
		g.drawString("Lives: " + dungeonScene.lives, 304-fm.stringWidth("Lives: " + dungeonScene.lives)/2, 268);
	}

	@Override
	public void keyPressed(KeyEvent keyEvent) {
		if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER){
			game.currentScene = dungeonScene;
		}
	}

}
