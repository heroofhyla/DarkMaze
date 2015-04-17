package com.aezart.darkmaze;

import java.awt.Graphics;
import java.awt.Graphics2D;

public class Stairs extends Entity{
	boolean stairsReady = false;
	int coinTarget = 40;
	
	public Stairs(DungeonScene scene){
		super(scene.game.tileset.getSubimage(0, 16, 16, 16), scene);
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
			scene.textAlert.showTextAlert("The stairs have appeared!", 60);
		}
		
		if (stairsReady && scene.knight.xTile() == xTile() && scene.knight.yTile() == yTile()){
			scene.readyForNextLevel = true;
		}
	}
}
