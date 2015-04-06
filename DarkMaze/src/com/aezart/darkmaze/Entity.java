package com.aezart.darkmaze;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Entity {
	BufferedImage sprite;
	int x;
	int y;
	
	EntityType entityType;
	
	public Entity(EntityType entityType){
		x = 228;
		y = 228;
		this.entityType = entityType;
	}
	
	int xTile(){
		return (x+8)/32;
	}
	
	int yTile(){
		return (y+8)/32;
	}
	
	int directionTo(Entity e){
		if (x > e.x){
			if (y > e.y){
				return 3; 
			}
			if (y < e.y){
				return 5;
			}
			return 4;
			
		}
		if (x < e.x){
			if (y > e.y){
				return 1;
			}
			if (y < e.y){
				return 7;
			}
			return 0;
		}
		
		if (y > e.y){
			return 2;
		}
		return 6;
	}

	public void draw(Graphics dg) {
		entityType.draw(dg, this);
	}
	
	public void drawLights(Graphics2D lg){
		entityType.drawLights(lg, this);
	}
	
	public void drawEffects(Graphics g){
		entityType.drawEffects(g, this);
	}
}
