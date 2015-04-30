package com.aezart.darkmaze;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Screen extends JPanel{
	DarkMaze darkMaze;
	
	BufferedImage gameSurface;
	//BufferedImage mapImage = new BufferedImage(608, 480, BufferedImage.TYPE_3BYTE_BGR);
	//BufferedImage drawingSurface = new BufferedImage(608, 480, BufferedImage.TYPE_3BYTE_BGR);
	//BufferedImage lightSurface = new BufferedImage(608, 480, BufferedImage.TYPE_4BYTE_ABGR);
	//BufferedImage finalRender = new BufferedImage(608,480,BufferedImage.TYPE_3BYTE_BGR);
	
	//Graphics mapG = mapImage.getGraphics();
	//Graphics drawingG = drawingSurface.getGraphics();
	//Graphics2D lightG2D = (Graphics2D) lightSurface.getGraphics();
	//Graphics finalG = finalRender.getGraphics();
	public Screen(DarkMaze darkMaze){
		this.darkMaze = darkMaze;
		gameSurface = darkMaze.createImage(608, 480, Transparency.OPAQUE);
	}
	@Override
	protected void paintComponent(Graphics g) {
		darkMaze.setTitle("" +this.getHeight());
		super.paintComponent(g);
		darkMaze.currentScene.draw(gameSurface.getGraphics());
		if (this.getHeight() * 1.2667 <= this.getWidth()){
			int renderWidth = (int) (this.getHeight() * 1.2667);
			g.drawImage(gameSurface,(this.getWidth() - renderWidth)/2,0,renderWidth, this.getHeight(), null);
		}else{
			int renderHeight = (int) (this.getWidth()*.7894);
			g.drawImage(gameSurface,0,(this.getHeight() - renderHeight)/2,this.getWidth(), renderHeight, null);
		}
	}
}
