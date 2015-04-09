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
		//g.setColor(Color.green);
		//g.drawRect(position.xTile()*32, position.yTile()*32, 32, 32);
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
			if (!game.maze[ y()/32][( x()-2)/32] &&
					!game.maze[( y()+16)/32][( x()-2)/32]){
				position.x -= 2;
			}
		}
		if ((game.keyStates.get(KeyEvent.VK_RIGHT) == true)){
			if (!game.maze[ y()/32][( x()+18)/32] &&
					!game.maze[( y()+16)/32][( x()+18)/32]){
				 position.x += 2;
			}
		}
		if ((game.keyStates.get(KeyEvent.VK_UP) == true)){
			if (!game.maze[( y()-2)/32][( x())/32] &&
					!game.maze[( y()-2)/32][( x()+16)/32]){
				 position.y -= 2;
			}
		}
		if ((game.keyStates.get(KeyEvent.VK_DOWN) == true)){
			if (!game.maze[( y()+18)/32][( x())/32] &&
					!game.maze[( y()+18)/32][( x()+16)/32]){
				 position.y += 2;
			}
		}
	}

}
