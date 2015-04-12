package com.aezart.darkmaze;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class TextAlert extends Entity {
	int duration = 0;
	String message;
	public TextAlert(DarkMaze game){
		super (game.noSprite, game);
		position.y = 240;
	}
	
	public void showTextAlert(String message, int duration){
		this.message = message;
		this.duration = duration;
	}
	@Override
	public void draw(Graphics g){
		//nothing
	}
	
	@Override
	public void drawLights(Graphics2D g){
		//nothing
	}
	
	@Override
	public void drawEffects(Graphics g){
		FontMetrics fm = g.getFontMetrics();
		if (duration > 0){
			g.setColor(Color.white);
			g.drawString(message, 304-fm.stringWidth(message)/2, y());
		}

	}
	@Override
	public void tick(){
		if (duration > 0){
			--duration;
		}
	}
}