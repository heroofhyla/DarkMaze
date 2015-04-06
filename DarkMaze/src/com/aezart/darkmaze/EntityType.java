package com.aezart.darkmaze;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class  EntityType {
	BufferedImage sprite;
	
	public EntityType(String imageString){
		try {
			sprite = ImageIO.read(new File(imageString));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public abstract void draw(Graphics dg, Entity e);
	
	public abstract void drawLights(Graphics2D lg, Entity e);
	
	public abstract void drawEffects(Graphics g, Entity e);
	
}
