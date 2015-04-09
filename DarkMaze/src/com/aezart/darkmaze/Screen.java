package com.aezart.darkmaze;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Screen extends JPanel{
	DarkMaze darkMaze;
	BufferedImage mapSurface = new BufferedImage(608, 480, BufferedImage.TYPE_3BYTE_BGR);
	BufferedImage displaySurface = new BufferedImage(608, 480, BufferedImage.TYPE_3BYTE_BGR);
	BufferedImage lightsurface = new BufferedImage(608,480,BufferedImage.TYPE_4BYTE_ABGR_PRE);
	
	public Screen(DarkMaze darkMaze){
		this.darkMaze = darkMaze;
	}
	@Override
	protected void paintComponent(Graphics g) {
		
		Graphics dg = displaySurface.getGraphics();
		super.paintComponent(g);
		dg.setColor(Color.black);
		Graphics2D lg = (Graphics2D)lightsurface.getGraphics();
		lg.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
		lg.fillRect(0, 0, 608, 480);
		lg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		
		dg.drawImage(mapSurface, 0, 0, null);
		

		for (int i = 0; i < darkMaze.coins.length; ++i){
			for (int k = 0; k < darkMaze.coins[0].length; ++k){
				if (darkMaze.coins[i][k]){
					dg.drawImage(darkMaze.tileset, k*64+32, i*64+32, k*64+64, i*64+64, 32, 0, 48, 16, null);
				}
			}
		}
		
		for (Entity e: darkMaze.entities){
			e.draw(dg);
			e.drawLights(lg);
		}
		
		if (darkMaze.displayMode == DarkMaze.FULLBRIGHT){
			lg.fillRect(0, 0, 608, 480);
		}
		lg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP));
		lg.drawImage(displaySurface, 0, 0, null);
		
		lg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		
		Graphics2D g2D = (Graphics2D) g;
		
		g2D.setColor(Color.black);
		g2D.fillRect(0, 0, 608, 480);
		g2D.drawImage(lightsurface, 0, 0, null);
		for (Entity e: darkMaze.entities){
			e.drawEffects(g2D);
			//e.draw(g2D);
		}
	}
}
