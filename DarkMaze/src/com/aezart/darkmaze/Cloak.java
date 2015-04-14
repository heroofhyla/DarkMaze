package com.aezart.darkmaze;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class Cloak extends Entity{
	int direction = game.rng.nextInt(4) * 2;
	XYCoords playerLastSeen = new XYCoords(0,0);
	XYCoords playerNextTurn = new XYCoords(0,0);
	boolean playerStillInView = false;
	
	public Cloak(DarkMaze game){
		super(game.cloakSprite, game);
		bboxX1 = -16;
		bboxX2 = 15;
		bboxY1 = -16;
		bboxY2 = 15;
	}
	
	@Override 
	public void drawLights(java.awt.Graphics2D g) {
		if (!lineOfSight(playerLastSeen)){
			super.drawLights(g);
		}
	};
	@Override 
	public void draw(Graphics g){
		if (game.debug){
			g.setColor(Color.green);
			g.drawRect(xTile(0)*32, yTile(0)*32, 32, 32);
			g.setColor(Color.white);
			g.drawRect(playerLastSeen.xTile(0)*32, playerLastSeen.yTile(0)*32, 32, 32);
			g.setColor(Color.red);
			g.drawRect(playerNextTurn.xTile(0)*32, playerNextTurn.yTile(0)*32, 32, 32);
		}
			super.draw(g);
	}
	@Override
	public void drawEffects(Graphics g){
		if (lineOfSight(playerLastSeen) || lineOfSight(playerNextTurn)){
			g.drawImage(game.redEyes, x()+drawXOffset, y()+drawYOffset, null);
		}else{
			g.drawImage(game.glowingEyes, x()+drawXOffset, y()+drawYOffset, null);
		}
		if (game.debug){
			if (playerLastSeen.x() != 0){
				g.setColor(Color.white);
				g.drawLine(x(), y(), playerLastSeen.x(), playerLastSeen.y());
			}else if (playerNextTurn.x() != 0){
				g.setColor(Color.red);
				g.drawLine(x(), y(), playerNextTurn.x(), playerNextTurn.y());
			}
			FontMetrics fm = g.getFontMetrics();
			g.setColor(Color.black);
			g.fillRect(position.x()-fm.stringWidth(this.toString())/2, position.y()-8, fm.stringWidth(this.toString()), 8);
			g.fillRect(position.x()-fm.stringWidth(this.toString())/2, position.y()+1, 40, 27);
			g.setColor(Color.cyan);
			g.drawString(this.toString(), position.x()-fm.stringWidth(this.toString())/2, position.y());
			
			g.drawString("x: " + position.x(), position.x()-fm.stringWidth(this.toString())/2, position.y()+9);
			
			g.drawString("y: " + position.y(), position.x()-fm.stringWidth(this.toString())/2, position.y()+18);
			g.drawString("dir: " + direction, position.x()-fm.stringWidth(this.toString())/2, position.y()+27);
			
			g.setColor(Color.black);
			
			g.setColor(Color.cyan);
			g.drawString("x: " + playerLastSeen.x(), playerLastSeen.x(), playerLastSeen.y());
			g.drawString("y: " + playerLastSeen.y(), playerLastSeen.x(), playerLastSeen.y()+9);
			
			g.setColor(Color.cyan);
			g.drawString("x: " + playerNextTurn.x(), playerNextTurn.x(), playerNextTurn.y());
			g.drawString("y: " + playerNextTurn.y(), playerNextTurn.x(), playerNextTurn.y()+9);
		}
	}
	
	@Override
	public void tick(){
		int lastdirection = direction;
		int freedirections = 0;
		for (int i = 0; i < 4; ++i){
			if (validMove(nextCoord((lastdirection + 2*i)%8))){
				++freedirections;
			}
		}
		
		if (freedirections > 2){
			int variation = game.rng.nextInt(3) - 1;
			direction = (direction + 2*variation+8)%8;
		}
		if (lineOfSight(game.knight)){
			if (game.debug){
				System.out.println(this + " reports: player in line of sight!");
			}
			playerLastSeen = new XYCoords(game.knight.position,16,16);
			playerStillInView = true;
		}else if (playerStillInView){
			if (game.debug){
				System.out.println(this + " lost sight of player.");
			}
			playerStillInView = false;
			playerNextTurn = new XYCoords(game.knight.position,16,16);
			
		}

		if (lineOfSight(playerLastSeen)){
			if (directionTo(playerLastSeen)%2 == 0){
				if (validMove(nextCoord(directionTo(playerLastSeen)))){
					direction = directionTo(playerLastSeen);
					if (game.debug){
						System.out.println(this + ": setting direction to " + direction);
					}
				}else{
					//direction = directionTo(position.xTile(0)*32 + 8, position.yTile(0)*32 + 8);
					//System.out.println("Recentering on tile: " + direction);
				}
			}
			
		}
		
		if (lineOfSight(playerNextTurn)){
			if (directionTo(playerNextTurn)%2 == 0 && validMove(nextCoord(directionTo(playerNextTurn)))){
				direction = directionTo(playerNextTurn);
				if (game.debug){
					System.out.println(this + " Following trail: " + direction);
					System.out.println("my xy: " + position.x() + "," + position.y());
					System.out.println("target xy: " + playerNextTurn.x() + "," + playerNextTurn.y());
				}				
				playerNextTurn = new XYCoords(0,0);
			}
		}

		XYCoords nextCoord = nextCoord(direction);
		
		while (!validMove(nextCoord.x(), nextCoord.y())){
			if (game.rng.nextBoolean()){
				direction = (lastdirection+2)%8;
			}else{
				direction = (lastdirection+6)%8;
			}
			nextCoord = nextCoord(direction);
		}
		
		position = nextCoord;
		 
		 if (x() == playerLastSeen.x() && y() == playerLastSeen.y()){
			 playerLastSeen = new XYCoords(0,0);
		 }
		 
		 if (xTile(0) == game.knight.xTile(0) && yTile(0) == game.knight.yTile(0)){
			 game.lives -= 1;
			 game.resetMaze();
			 game.knight.setTile(9, 7, 16, 16);

		 }
	}
	
	@Override
	public String toString(){
		return super.toString().substring(25);
	}
}
