package com.aezart.darkmaze;

import java.awt.Color;
import java.awt.Graphics;

public class Cloak extends Entity{
	int direction = game.rng.nextInt(4) * 2;
	XYCoords playerLastSeen = XYCoords.fromTile(0,0);
	XYCoords playerNextTurn = XYCoords.fromTile(0, 0);
	boolean playerInSight = false;
	
	public Cloak(DarkMaze game){
		super(game.cloakSprite, game);
	}
	
	@Override 
	public void draw(Graphics g){
		g.setColor(Color.white);
		g.drawRect(playerLastSeen.x(), playerLastSeen.y(), 32, 32);
		g.setColor(Color.red);
		g.drawRect(playerNextTurn.x(), playerNextTurn.y(), 32, 32);
		super.draw(g);
	}
	@Override
	public void drawEffects(Graphics g){
		if (lineOfSight(game.knight)){
			g.drawImage(game.redEyes, x(), y(), null);
		}else{
			g.drawImage(game.glowingEyes, x(), y(), null);
		}
	}
	
	@Override
	public void tick(){
		int lastdirection = direction;
		if (lineOfSight(game.knight)){
			//playerLastSeen.x = game.knight.x();
			//playerLastSeen.y = game.knight.y();
			playerLastSeen.setXTile(game.knight.xTile());
			playerLastSeen.setYTile(game.knight.yTile());
			playerInSight = true;
		}else if (playerInSight){
			playerInSight = false;
			playerNextTurn.setXTile(game.knight.xTile());
			playerNextTurn.setYTile(game.knight.yTile());
		}

		if (lineOfSight(playerLastSeen)){
			direction = directionTo(playerLastSeen);
		}
		
		if (lineOfSight(playerNextTurn)){
			direction = directionTo(playerNextTurn);
			playerNextTurn.x = 0;
			playerNextTurn.y = 0;
		}
		if (xTile() == game.knight.xTile() && yTile() == game.knight.yTile()){
			direction = directionTo(game.knight);
		}
		int nextX = x();
		int nextY = y();

		
		if (direction == 0 || direction == 1 || direction == 7){
			if (!game.maze[y()/32][(x()+18)/32] &&
					!game.maze[(y()+16)/32][(x()+18)/32]){
				nextX += 2;
			}
		}
		if (direction == 3 || direction == 4 || direction == 5){
			if (!game.maze[ y()/32][(x()-2)/32] &&
					!game.maze[(y()+16)/32][(x()-2)/32]){
				nextX -= 2;
			}
		}
		if (direction == 1 || direction == 2 || direction == 3){
			if (!game.maze[(y()-2)/32][(x())/32] &&
					!game.maze[(y()-2)/32][(x()+16)/32]){
				nextY -= 2;
			}
		}
		if (direction == 5 || direction == 6 || direction == 7){
			if (!game.maze[(y()+18)/32][(x())/32] &&
					!game.maze[(y()+18)/32][(x()+16)/32]){
				nextY += 2;
			}
		}
		while (nextX ==  x() && nextY ==  y()){
			//direction = game.rng.nextInt(8)/2 * 2;
			if (game.rng.nextBoolean()){
				direction = (lastdirection+2)%8;
			}else{
				direction = (lastdirection+6)%8;
			}
			nextX =  x();
			nextY =  y();
			if (direction == 0 || direction == 1 || direction == 7){
				if (!game.maze[ y()/32][(x()+18)/32] &&
						!game.maze[(y()+16)/32][(x()+18)/32]){
					nextX += 2;
				}
			}
			if (direction == 3 || direction == 4 || direction == 5){
				if (!game.maze[ y()/32][(x()-2)/32] &&
						!game.maze[(y()+16)/32][(x()-2)/32]){
					nextX -= 2;
				}
			}
			if (direction == 1 || direction == 2 || direction == 3){
				if (!game.maze[(y()-2)/32][(x())/32] &&
						!game.maze[(y()-2)/32][(x()+16)/32]){
					nextY -= 2;
				}
			}
			if (direction == 5 || direction == 6 || direction == 7){
				if (!game.maze[(y()+18)/32][(x())/32] &&
						!game.maze[(y()+18)/32][(x()+16)/32]){
					nextY += 2;
				}
			}
		}
		
		 position.x = nextX;
		 position.y = nextY;
		 
		 if (x() == playerLastSeen.x() && y() == playerLastSeen.y()){
			 playerLastSeen.x = 0;
			 playerLastSeen.y = 0;
		 }
	}
}
