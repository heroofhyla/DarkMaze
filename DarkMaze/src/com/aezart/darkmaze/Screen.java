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
		super.paintComponent(g);
		darkMaze.currentScene.draw(g);
	}
}
