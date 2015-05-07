package com.aezart.darkmaze;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class TransitionScene extends Scene{
	//measured in ~30fps frames
	int sceneTimer = 60;
	DungeonScene scene;
	
	public TransitionScene(DungeonScene dungeonScene){
		this.scene = dungeonScene;
	}
	@Override
	public void tick() {
		--sceneTimer;
		if (sceneTimer < 0){
			scene.game.currentScene = scene;
		}
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, 608, 480);
		FontMetrics fm = g.getFontMetrics();
		g.setColor(Color.white);
		String levelTitle = scene.tombs.get(scene.level%scene.names.size()) + scene.names.get(scene.level%scene.names.size());
		g.drawString(levelTitle, 304 - fm.stringWidth(levelTitle)/2, 240);
		g.drawString("Depth: " + scene.level*10 + "ft", 304 - fm.stringWidth("Depth: " + scene.level*10 + "ft")/2, 256);
		g.drawString("Lives: " + scene.lives, 304-fm.stringWidth("Lives: " + scene.lives)/2, 268);
	}

	@Override
	public void keyPressed(KeyEvent keyEvent) {
		if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER){
			scene.game.currentScene = scene;
		}
	}

}
