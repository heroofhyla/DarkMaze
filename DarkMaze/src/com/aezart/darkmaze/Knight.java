package com.aezart.darkmaze;

import java.awt.Color;
import java.awt.FontMetrics;
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
			if (game.coins[ y()/64][ x()/64]){
				game.coins[ y()/64][ x()/64] = false;
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
	}
	
	@Override
	public String toString(){
		return super.toString().substring(26);
	}

}
