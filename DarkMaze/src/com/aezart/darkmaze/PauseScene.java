package com.aezart.darkmaze;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class PauseScene extends Scene{
	BufferedImage bgImage;
	DungeonScene scene;
	public PauseScene(DungeonScene scene, BufferedImage bgImage){
		this.scene = scene;
		this.bgImage = bgImage;
	}

	@Override
	public void tick() {
		//nothing
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(bgImage, 0, 0, null);
		FontMetrics fm = g.getFontMetrics();
		g.setColor(Color.white);
		g.drawString("Pause", 304-fm.stringWidth("Pause")/2,256);		
	}

	@Override
	public void keyPressed(KeyEvent keyEvent) {
		if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE){
			scene.game.currentScene = scene;
		}
	}

}
