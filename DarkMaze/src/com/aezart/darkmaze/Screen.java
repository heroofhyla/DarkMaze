package com.aezart.darkmaze;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Screen extends JPanel{
	DarkMaze darkMaze;
	BufferedImage mapImage = new BufferedImage(608, 480, BufferedImage.TYPE_3BYTE_BGR);
	BufferedImage drawingSurface = new BufferedImage(608, 480, BufferedImage.TYPE_3BYTE_BGR);
	BufferedImage lightSurface = new BufferedImage(608, 480, BufferedImage.TYPE_4BYTE_ABGR);
	BufferedImage finalRender = new BufferedImage(608,480,BufferedImage.TYPE_3BYTE_BGR);
	
	Graphics mapG = mapImage.getGraphics();
	Graphics drawingG = drawingSurface.getGraphics();
	Graphics2D lightG2D = (Graphics2D) lightSurface.getGraphics();
	Graphics finalG = finalRender.getGraphics();
	public Screen(DarkMaze darkMaze){
		this.darkMaze = darkMaze;
	}
	@Override
	protected void paintComponent(Graphics g) {
		long paintTime = System.currentTimeMillis();
		super.paintComponent(g);
		
		drawingG.drawImage(mapImage, 0, 0, null);
		lightG2D.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
		lightG2D.fillRect(0, 0, 608, 480);
		lightG2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		
		for (int i = 0; i < darkMaze.coins.length; ++i){
			for (int k = 0; k < darkMaze.coins[0].length; ++k){
				if (darkMaze.coins[i][k]){
					drawingG.drawImage(darkMaze.tileset, k*64+32, i*64+32, k*64+64, i*64+64, 32, 0, 48, 16, null);
				}
			}
		}

		
		for (Entity e: darkMaze.entities){
			e.draw(drawingG);
			e.drawLights(lightG2D);
		}
		
		lightG2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN));
		lightG2D.drawImage(drawingSurface, 0, 0, null);
		finalG.setColor(Color.black);
		finalG.fillRect(0, 0, 608, 480);
		finalG.drawImage(lightSurface, 0, 0, null);
		
		finalG.setColor(Color.black);
		finalG.fillRect(0,468,608,480);
		finalG.setColor(Color.white);
		finalG.drawString("Lives: " + darkMaze.lives + " Depth: " + (10*darkMaze.level)  + "ft Coins: " + darkMaze.coinCount, 0, 478);

		for (Entity e: darkMaze.entities){
			e.drawEffects(finalG);
		}
		
		g.drawImage(finalRender, 0, 0, null);
		System.out.println(System.currentTimeMillis() - paintTime);
		
	}
}
