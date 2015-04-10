package com.aezart.darkmaze;

import java.awt.Graphics;
import java.awt.Graphics2D;

public class Stairs extends Entity{
	boolean stairsReady = false;
	
	public Stairs(DarkMaze game){
		super(game.tileset.getSubimage(0, 16, 16, 16), game);
	}
	
	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		g.drawImage(sprite, x(), y(), x()+32, y()+32, 0, 0, 16, 16, null);
	}
	
	@Override
	public void drawLights(Graphics2D g){
		//nothing
	}
}
