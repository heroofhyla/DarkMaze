package com.aezart.darkmaze;

import java.awt.Graphics;
import java.awt.Graphics2D;

public class Stairs extends Entity{
	boolean stairsReady = false;
	int coinTarget = 40;
	
	public Stairs(DarkMaze game){
		super(game.tileset.getSubimage(0, 16, 16, 16), game);
	}
	
	@Override
	public void draw(Graphics g) {
		if (stairsReady){
			g.drawImage(sprite, x(), y(), x()+32, y()+32, 0, 0, 16, 16, null);
		}
	}
	
	@Override
	public void drawLights(Graphics2D g){
		//nothing
	}
	
	@Override
	public void tick(){
		if (game.coinCount > coinTarget && !stairsReady){
			stairsReady = true;
		}
		
		if (stairsReady && game.knight.xTile() == xTile() && game.knight.yTile() == yTile()){
			game.nextLevel();
			stairsReady = false;
			coinTarget += 40;
		}
	}
}
