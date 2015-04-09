package com.aezart.darkmaze;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class Entity {
	XYCoords position = XYCoords.fromAbsolute(0,0);
	//int x;
	//int y;
	
	BufferedImage sprite;
	DarkMaze game;
		
	public Entity(BufferedImage sprite, DarkMaze game){
		this.sprite = sprite;
		this.game = game;
		//x = 0;
		//y = 0;
	}
	
	int x(){
		return position.x;
	}
	
	int y(){
		return position.y;
	}
	//TODO: Don't make these functions assume a 16x16 image
	int xTile(){
		return position.xTile();
		//return (x+8)/32;
	}
	
	int yTile(){
		return position.yTile();
		//return (y+8)/32;
	}
	/*
	void setPositionTile(int x, int y){
		this.x = x*32 + 8;
		this.y = y*32 + 8;
	}*/
	
	void setPosition(XYCoords xy){
		this.position = xy;
	}
	
	int directionTo(XYCoords t){
		return directionTo(t.x(), t.y());
	}
	//returns: int 0 through seven, with 0 east, 1 north east, 2 north, ... 7 southwest
	//behavior undefined when x == e.x && y == e.y
	int directionTo(Entity e){
		return directionTo(e.x(), e.y());
	}
	
	int directionTo(int x, int y){
		if (this.x() > x){
			if (this.y() > y){
				return 3; 
			}
			if (this.y() < y){
				return 5;
			}
			return 4;
			
		}
		if (this.x() < x){
			if (this.y() > y){
				return 1;
			}
			if (this.y() < y){
				return 7;
			}
			return 0;
		}
		
		if (this.y() > y){
			return 2;
		}
		return 6;
		
	}

	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.drawRect(x(), y(), 32, 32);
		g.drawImage(sprite, x(), y(), null);
	}
	//TODO: Don't assume light is 72x72
	public void drawLights(Graphics2D g){
		g.drawImage(game.light, x()-28, y()-28, null);	
	}
	
	public void drawEffects(Graphics g){
		//nothing unless overridden
	}
	
	public boolean lineOfSight(Entity e){
		return lineOfSight(e.xTile(),e.yTile());
	}
	
	public boolean lineOfSight(XYCoords t){
		return lineOfSight(t.xTile(), t.yTile());
	}
	public boolean lineOfSight(int xTile, int yTile){
		boolean lineOfSight = false;
		if (yTile() == yTile){
			lineOfSight = true;
			for (int j = Math.min(xTile(), xTile); j < Math.max(xTile(), xTile); ++j){
				if (game.maze[yTile()][j]){
					lineOfSight = false;
				}
			}
		}
		if (xTile() == xTile){
			lineOfSight = true;
			for (int j = Math.min(yTile(), yTile); j < Math.max(yTile(), yTile); ++j){
				if (game.maze[j][xTile()]){
					lineOfSight = false;
				}
			}
		}
		
		return lineOfSight;
	}

	public void tick(){
		//nothing unless overridden
	}
}
