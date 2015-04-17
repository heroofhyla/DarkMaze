package com.aezart.darkmaze;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Knight extends Entity{
	int direction = 6;
	XYCoords[]spriteCoords = {new XYCoords(16, 0), new XYCoords(0, 0), new XYCoords(0, 16), new XYCoords(16,16)};

	public Knight(DungeonScene scene) {
		super(scene.game.knightSprite, scene);
		drawXOffset = -sprite.getWidth()/4;
		drawYOffset = -sprite.getHeight()/4;

	}
	
	@Override
	public void draw(Graphics g){
		if (game.debug){
			g.setColor(Color.green);
			g.drawRect(xTile()*32, yTile()*32, 32, 32);
		}
		g.drawImage(sprite, x()+drawXOffset, y()+drawYOffset, x()+drawXOffset+16, y()+drawYOffset+16, spriteCoords[direction/2].x(),spriteCoords[direction/2].y(),spriteCoords[direction/2].x() + 16, spriteCoords[direction/2].y() + 16, null);
		//super.draw(g);
	}
	
	@Override
	public void drawEffects(Graphics g){
		if (game.debug){
			FontMetrics fm = g.getFontMetrics();
			g.setColor(Color.black);
			g.fillRect(position.x()-fm.stringWidth(this.toString())/2, position.y()-8, fm.stringWidth(this.toString()), 8);
			g.fillRect(position.x()-fm.stringWidth(this.toString())/2, position.y()+1, 40, 18);
			g.setColor(Color.cyan);
			g.drawString(this.toString(), position.x()-fm.stringWidth(this.toString())/2, position.y());
			
			g.drawString("x: " + position.x(), position.x()-fm.stringWidth(this.toString())/2, position.y()+9);
			
			g.drawString("y: " + position.y(), position.x()-fm.stringWidth(this.toString())/2, position.y()+18);
		}
	}
	@Override
	public void tick(){
		if ( x()%64 > 34 &&  x()%64 < 62 && 
				 y()%64 >34 &&  y()%64 < 62){
			if (scene.coins[ y()/64][ x()/64]){
				scene.coins[ y()/64][ x()/64] = false;
				++ game.coinCount;
			}
		}
		if ((game.keyStates.get(KeyEvent.VK_LEFT) == true)){
			if (validMove(x()-2,y())){
				position = position.plus(-2, 0);
			}

		}
		if ((game.keyStates.get(KeyEvent.VK_RIGHT) == true)){
			if (validMove(x()+2, y())){
				position = position.plus(2, 0);
			}

		}
		if ((game.keyStates.get(KeyEvent.VK_UP) == true)){
			if (validMove(x(), y()-2)){
				 position = position.plus(0, -2);
			}
		}
		if ((game.keyStates.get(KeyEvent.VK_DOWN) == true)){
			if (validMove(x(), y()+2)){
				 position = position.plus(0, 2);
			}
		}
		
		//If not moving diagonally, change facing to match direction moving. Don't change facing if moving diagonally
		//This looks like Link to the Past's system
		if (game.keyStates.get(KeyEvent.VK_LEFT) == true && game.keyStates.get(KeyEvent.VK_UP) == false && game.keyStates.get(KeyEvent.VK_DOWN) == false){
			direction = WEST;
		}
		if (game.keyStates.get(KeyEvent.VK_RIGHT) == true && game.keyStates.get(KeyEvent.VK_UP) == false && game.keyStates.get(KeyEvent.VK_DOWN) == false){
			direction = EAST;
		}
		if (game.keyStates.get(KeyEvent.VK_UP) == true && game.keyStates.get(KeyEvent.VK_LEFT) == false && game.keyStates.get(KeyEvent.VK_RIGHT) == false){
			direction = NORTH;
		}
		if (game.keyStates.get(KeyEvent.VK_DOWN) == true && game.keyStates.get(KeyEvent.VK_LEFT) == false && game.keyStates.get(KeyEvent.VK_RIGHT) == false){
			direction = SOUTH;
		}
		
		//If facing entirely wrong way, flip around
		if (game.keyStates.get(KeyEvent.VK_DOWN) == true && direction == NORTH){
			direction = SOUTH;
		}
		if (game.keyStates.get(KeyEvent.VK_UP) == true && direction == SOUTH){
			direction = NORTH;
		}
		if (game.keyStates.get(KeyEvent.VK_LEFT) == true && direction == EAST){
			direction = WEST;
		}
		if (game.keyStates.get(KeyEvent.VK_RIGHT) == true && direction == WEST){
			direction = EAST;
		}
		
	}
	
	@Override
	public String toString(){
		return super.toString().substring(26);
	}

}
