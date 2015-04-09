package com.aezart.darkmaze;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class  EntityType {
	BufferedImage sprite;
	
	private DrawCallable draw;
	private DrawCallable drawLights;
	private DrawCallable drawEffects; 
		
	public EntityType(String imageString, DrawCallable draw, DrawCallable drawLights, DrawCallable drawEffects){
		if (imageString != null){
		try {
			sprite = ImageIO.read(new File(imageString));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		this.draw = draw;
		this.drawLights = drawLights;
		this.drawEffects = drawEffects;
	}
	public void draw(Graphics dg, Entity e){
		if (draw != null){
			draw.call((Graphics2D)dg, e);
		}
	}
	
	public void drawLights(Graphics2D lg, Entity e){
		if (drawLights != null){
			drawLights.call(lg, e);
		}
	}
	
	public void drawEffects(Graphics g, Entity e){
		if (drawEffects != null){
			drawEffects.call((Graphics2D)g, e);
		}
	}
	
}
