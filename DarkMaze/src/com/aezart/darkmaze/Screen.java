package com.aezart.darkmaze;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Screen extends JPanel{
	DarkMaze darkMaze;
	BufferedImage mapLayer = new BufferedImage(608, 480, BufferedImage.TYPE_3BYTE_BGR);
	BufferedImage entityLayer = new BufferedImage(608, 480, BufferedImage.TYPE_4BYTE_ABGR_PRE);
	BufferedImage lightLayer = new BufferedImage(608, 480, BufferedImage.TYPE_4BYTE_ABGR_PRE);
	BufferedImage effectsLayer = new BufferedImage(608, 480, BufferedImage.TYPE_4BYTE_ABGR_PRE);
	BufferedImage finalRender = new BufferedImage(608, 480, BufferedImage.TYPE_4BYTE_ABGR_PRE);
	Graphics mapG = mapLayer.getGraphics();
	Graphics2D entityG2D = (Graphics2D)entityLayer.getGraphics();
	Graphics2D lightG2D = (Graphics2D)lightLayer.getGraphics();
	Graphics2D effectsG2D = (Graphics2D)effectsLayer.getGraphics();
	Graphics2D finalG2D = (Graphics2D)finalRender.getGraphics();
	public Screen(DarkMaze darkMaze){
		this.darkMaze = darkMaze;
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		lightG2D.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
		lightG2D.fillRect(0, 0, 608, 480);
		lightG2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		entityG2D.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
		entityG2D.fillRect(0, 0, 608, 480);
		entityG2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		effectsG2D.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
		effectsG2D.fillRect(0, 0, 608, 480);
		effectsG2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		
		for (int i = 0; i < darkMaze.coins.length; ++i){
			for (int k = 0; k < darkMaze.coins[0].length; ++k){
				if (darkMaze.coins[i][k]){
					entityG2D.drawImage(darkMaze.tileset, k*64+32, i*64+32, k*64+64, i*64+64, 32, 0, 48, 16, null);
				}
			}
		}
		
		for (Entity e: darkMaze.entities){
			e.draw(entityG2D);
			e.drawLights(lightG2D);
			e.drawEffects(effectsG2D);
		}
		
		effectsG2D.setColor(Color.black);
		effectsG2D.fillRect(0,468,608,480);
		effectsG2D.setColor(Color.white);
		effectsG2D.drawString("Lives: " + darkMaze.lives + " Depth: " + (10*darkMaze.level)  + "ft Coins: " + darkMaze.coinCount, 0, 478);

		finalG2D.drawImage(mapLayer, 0, 0, null);
		finalG2D.drawImage(entityLayer, 0, 0, null);
		finalG2D.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_ATOP));
		finalG2D.drawImage(lightLayer, 0, 0, null);
		finalG2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		finalG2D.drawImage(effectsLayer, 0, 0, null);
		
		g.setColor(Color.black);
		g.fillRect(0, 0, 608, 480);
		g.drawImage(finalRender, 0, 0, null);
	}
}
