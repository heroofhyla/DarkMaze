package com.aezart.darkmaze;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D)g;
		if (darkMaze.light == darkMaze.fancyLight){
			g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		}else{
			g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);

		}
		darkMaze.currentScene.draw(gameSurface.getGraphics());
		if (this.getWidth() == 608 && this.getHeight() == 480){
			g.drawImage(gameSurface,0,0,null);
			darkMaze.setTitle(":)");
		}else{
			int renderWidth;
			int renderHeight;
			if (this.getHeight() * 1.2667 <= this.getWidth()){
				renderWidth = (int) (this.getHeight() * 1.2667);
				renderHeight = this.getHeight();
			}else{
				renderWidth = (int) (this.getWidth());
				renderHeight = (int) (this.getWidth()*.7894);
			}
			darkMaze.setTitle(this.getWidth() + "," +this.getHeight());

			g.drawImage(gameSurface,(this.getWidth() - renderWidth)/2,(this.getHeight() - renderHeight)/2,renderWidth, renderHeight, null);
		}
	}
}
