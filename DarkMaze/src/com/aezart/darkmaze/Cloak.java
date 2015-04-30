package com.aezart.darkmaze;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class Cloak extends Entity{
	int confusionTimer = -1;
	int alertState = 0;
	int direction = game.rng.nextInt(4) * 2;
	int xOffset = 0;
	int yOffset = 0;
	XYCoords playerLastSeen = new XYCoords(0,0);
	XYCoords playerNextTurn = new XYCoords(0,0);
	boolean playerStillInView = false;
	XYCoords[]spriteCoords = {new XYCoords(16, 0), new XYCoords(0, 0), new XYCoords(0, 16), new XYCoords(16,16)};
	public Cloak(DungeonScene scene, int bboxX1, int bboxY1, int bboxX2, int bboxY2){
		super(scene.game.cloakSprite, scene);
		drawXOffset = -sprite.getWidth()/4;
		drawYOffset = -sprite.getHeight()/4;

		this.bboxX1 = bboxX1;
		this.bboxX2 = bboxX2;
		this.bboxY1 = bboxY1;
		this.bboxY2 = bboxY2;
	}
	
	public Cloak(DungeonScene scene, int xOffset, int yOffset){
		super(scene.game.cloakSprite, scene);
		drawXOffset = -sprite.getWidth()/4;
		drawYOffset = -sprite.getHeight()/4;

		/*this.bboxX1 = xOffset - 16;
		this.bboxY1 = yOffset - 16;
		this.bboxX2 = xOffset + 7;
		this.bboxY2 = yOffset + 7;
		this.xOffset = xOffset;
		this.yOffset = yOffset;*/
		if (xOffset == 8){
			bboxX1 = -8;
			bboxX2 = 23;
		}
		if (xOffset == 24){
			bboxX1 = -24;
			bboxX2 = 7;
		}
		
		if (yOffset == 8){
			bboxY1 = -8;
			bboxY2 = 23;
		}
		if (yOffset == 24){
			bboxY1 = -24;
			bboxY2 = 7;
		}
		
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	
	/*@Override 
	public void drawLights(java.awt.Graphics2D g) {
		if (!lineOfSight(playerLastSeen)){
			super.drawLights(g);
		}
	};*/
	@Override 
	public void draw(Graphics g){
		if (game.debug){
			g.setColor(Color.green);
			g.drawRect(xTile()*32, yTile()*32, 32, 32);
			g.setColor(Color.white);
			g.drawRect(playerLastSeen.xTile()*32, playerLastSeen.yTile()*32, 32, 32);
			g.setColor(Color.red);
			g.drawRect(playerNextTurn.xTile()*32, playerNextTurn.yTile()*32, 32, 32);
		}
		g.drawImage(sprite, x()+drawXOffset, y()+drawYOffset, x()+drawXOffset+16, y()+drawYOffset+16, spriteCoords[direction/2].x(),spriteCoords[direction/2].y(),spriteCoords[direction/2].x() + 16, spriteCoords[direction/2].y() + 16, null);

		//super.draw(g);
	}
	@Override
	public void drawEffects(Graphics g){
		if (alertState == 2){
			g.drawImage(game.alertIcon, x()-8, y()-24, null);
		} else if (alertState == 1){
			g.drawImage(game.lostIcon, x()-8, y()-24, null);
		}
		/*else{
			g.drawImage(game.glowingEyes, x()+drawXOffset, y()+drawYOffset, null);
		}*/
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
			/*
			g.fillRect(position.x()-fm.stringWidth(this.toString())/2, position.y()+1, 40, 27);*/
			g.setColor(Color.blue);
			g.drawRect(x() + bboxX1, y()+bboxY1, bboxX2 - bboxX1, bboxY2 - bboxY1);
			g.setColor(Color.cyan);
			g.drawString(this.toString(), position.x()-fm.stringWidth(this.toString())/2, position.y()-18);
			
			/*
			g.drawString("xT: " + position.xTile(), position.x()-fm.stringWidth(this.toString())/2, position.y()+9);
			
			g.drawString("yT: " + position.yTile(), position.x()-fm.stringWidth(this.toString())/2, position.y()+18);
			g.drawString("dir: " + direction, position.x()-fm.stringWidth(this.toString())/2, position.y()+27);
			g.drawString("toPLS: " + directionToTile(playerLastSeen),position.x()-fm.stringWidth(this.toString())/2, y()+36);
			
			g.setColor(Color.black);
			
			g.setColor(Color.cyan);
			g.drawString("x: " + playerLastSeen.x(), playerLastSeen.x(), playerLastSeen.y());
			g.drawString("y: " + playerLastSeen.y(), playerLastSeen.x(), playerLastSeen.y()+9);
			
			g.setColor(Color.cyan);
			g.drawString("x: " + playerNextTurn.x(), playerNextTurn.x(), playerNextTurn.y());
			g.drawString("y: " + playerNextTurn.y(), playerNextTurn.x(), playerNextTurn.y()+9);*/
		}
	}
	
	@Override
	public void tick(){
		if (alertState == 1){
			--confusionTimer;
		}
		if (confusionTimer < 0){
			alertState = 0;
		}
		int lastdirection = direction;
		int freedirections = 0;
		for (int i = 0; i < 4; ++i){
			if (validMove(nextCoord((lastdirection + 2*i)%8))){
				++freedirections;
			}
		}
		
		if (freedirections > 2){
			if (alertState != 1 || (confusionTimer%5 == 0 && confusionTimer < 21)){
			int variation = game.rng.nextInt(3) - 1;
			direction = (lastdirection + 2*variation+8)%8;
			if (!lineOfSight(playerLastSeen) && !lineOfSight(playerNextTurn) && alertState == 2){
				alertState = 1;
			}
			}
		}
		if (lineOfSight(scene.knight) && directionToTile(scene.knight.position) == direction){
			if (game.debug){
				System.out.println(this + " reports: player in line of sight!");
			}
			playerLastSeen.setPosition(scene.knight.xTile(), scene.knight.yTile(), xOffset, yOffset);
			playerStillInView = true;
			alertState = 2;
			confusionTimer = 34;
		}else if (playerStillInView){
			if (game.debug){
				System.out.println(this + " lost sight of player.");
			}
			playerStillInView = false;
			playerNextTurn.setPosition(scene.knight.xTile(), scene.knight.yTile(), xOffset, yOffset);
			
		}

		if (lineOfSight(playerLastSeen)){
			System.out.println(this + "direction to PLS: " + directionToTile(playerLastSeen));

				if (validMove(nextCoord(directionToTile(playerLastSeen)))){
					direction = directionToTile(playerLastSeen);
					if (game.debug){
						System.out.println(this + ": setting direction to " + direction);
					}
				}else{
					System.out.println(this + ": can't move to PLS" + nextCoord(directionToTile(playerLastSeen)));
					if (direction == WEST && x() < playerLastSeen.x()){
						System.out.println(this + ": Doubling back because " + x() + "<" + (playerLastSeen.x()));
						direction = EAST;
					}
					if (direction == EAST && x() > playerLastSeen.x()){
						System.out.println(this + ": Doubling back because " + x() + ">" + (playerLastSeen.x()));

						direction = WEST;
					}
					if (direction == NORTH && y() < playerLastSeen.y()){
						System.out.println(this + ": Doubling back because " + y() + "<" + (playerLastSeen.y()));

						direction = SOUTH;
					}
					if (direction == SOUTH && y() > playerLastSeen.y()){
						System.out.println(this + ": Doubling back because " + y() + ">" + (playerLastSeen.y()));
						direction = NORTH;
					}
					//direction = directionTo(position.xTile(0)*32 + 8, position.yTile(0)*32 + 8);
					//System.out.println("Recentering on tile: " + direction);
				}
			//}
			
		}
		
		if (lineOfSight(playerNextTurn)){
			System.out.println(this + "direction to PNT: " + directionToTile(playerNextTurn));
			if (validMove(nextCoord(directionToTile(playerNextTurn)))){
				System.out.println(this+ ": move is valid!");
				direction = directionToTile(playerNextTurn);
				if (game.debug){
					System.out.println(this + " Following trail: " + direction);
					System.out.println("my xy: " + position.x() + "," + position.y());
					System.out.println("target xy: " + playerNextTurn.x() + "," + playerNextTurn.y());
				}				
				playerNextTurn.setPosition(0,0);
			}else{
				System.out.println(this + ": can't move to " + nextCoord(directionToTile(playerNextTurn)));

				if (direction == WEST && x() < playerNextTurn.x()){
					System.out.println(this + ": Doubling back because " + x() + "<" + (playerNextTurn.x()));

					direction = EAST;
				}
				if (direction == EAST && x() > playerNextTurn.x()){
					System.out.println(this + ": Doubling back because " + x() + ">" + (playerNextTurn.x()));

					direction = WEST;
				}
				if (direction == NORTH && y() < playerNextTurn.y()){
					System.out.println(this + ": Doubling back because " + y() + "<" + (playerNextTurn.y()));

					direction = SOUTH;
				}
				if (direction == SOUTH && y() > playerNextTurn.y()){
					System.out.println(this + ": Doubling back because " + y() + ">" + (playerNextTurn.y()));
					direction = NORTH;
				}
			}
		}

		XYCoords nextCoord = nextCoord(direction);
		
		while (!validMove(nextCoord)){
			if (game.rng.nextBoolean()){
				direction = (lastdirection+2)%8;
			}else{
				direction = (lastdirection+6)%8;
			}
			nextCoord.setPosition(nextCoord(direction));
		}
		
		if (alertState != 1){
			position.setPosition(nextCoord);
		}
		
		if (position.equalsTile(playerLastSeen)){
		//if (xTile() == playerLastSeen.xTile() && yTile() == playerLastSeen.yTile()){
			playerLastSeen.setPosition(0,0);
		} 
		
		if (position.equalsTile(scene.knight.position)){
		//if (Math.abs(x() - scene.knight.x()) < 10 && Math.abs(y() - scene.knight.y()) < 10){
			scene.lives -= 1;
			scene.resetEntities();
			game.currentScene = new TransitionScene(scene, game);
			//scene.knight.setTile(9, 7, 16, 16);
		
		}
	}
	
	@Override
	public String toString(){
		return super.toString().substring(25);
	}
}
