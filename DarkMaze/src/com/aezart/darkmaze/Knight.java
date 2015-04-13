package com.aezart.darkmaze;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Knight extends Entity{

	public Knight(DarkMaze game) {
		super(game.knightSprite, game);
	}
	
	@Override
	public void draw(Graphics g){
		if (game.debug){
			g.setColor(Color.green);
			g.drawRect(xTile()*32, yTile()*32, 32, 32);
		}
		super.draw(g);
	}
	@Override
	public void tick(){
		if ( x()%64 > 26 &&  x()%64 < 54 && 
				 y()%64 >26 &&  y()%64 < 54){
			if (game.coins[ y()/64][ x()/64]){
				game.coins[ y()/64][ x()/64] = false;
				++ game.coinCount;
			}
		}
		if ((game.keyStates.get(KeyEvent.VK_LEFT) == true)){
			if (validMove(x()-2,y())){
				position.xPlus(-2);
			}
		}
		if ((game.keyStates.get(KeyEvent.VK_RIGHT) == true)){
			if (validMove(x()+2, y())){
				position.xPlus(2);
			}
		}
		if ((game.keyStates.get(KeyEvent.VK_UP) == true)){
			if (validMove(x(), y()-2)){
				 position.yPlus(-2);
			}
		}
		if ((game.keyStates.get(KeyEvent.VK_DOWN) == true)){
			if (validMove(x(), y()+2)){
				 position.yPlus(2);
			}
		}
	}

}
