package com.aezart.darkmaze;

import java.awt.Graphics;

public class Cloak extends Entity{
	int direction = 0;
	XYCoords playerLastSeen = XYCoords.fromTile(0,0);
	public Cloak(DarkMaze game){
		super(game.cloakSprite, game);
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
			playerLastSeen.x = game.knight.x();
			playerLastSeen.y = game.knight.y();			
		}

		if (lineOfSight(playerLastSeen)){
			direction = directionTo(playerLastSeen);
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
			direction = game.rng.nextInt(8)/2 * 2;
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
