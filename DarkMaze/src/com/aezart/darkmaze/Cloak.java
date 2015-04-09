package com.aezart.darkmaze;

import java.awt.Graphics;

public class Cloak extends Entity{
	int direction = 0;
	TileXY playerLastSeen = new TileXY(0,0);
	public Cloak(DarkMaze game){
		super(game.cloakSprite, game);
	}
	
	@Override
	public void drawEffects(Graphics g){
		if (lineOfSight(game.knight)){
			g.drawImage(game.redEyes, x, y, null);
		}else{
			g.drawImage(game.glowingEyes, x, y, null);
		}
	}
	
	@Override
	public void tick(){
		if (lineOfSight(game.knight)){
			playerLastSeen.xTile = game.knight.xTile();
			playerLastSeen.yTile = game.knight.yTile();			
		}

		if (lineOfSight(playerLastSeen)){
			direction = directionTo(playerLastSeen);
		}
		
		int nextX = x;
		int nextY = y;

		
		if (direction == 0 || direction == 1 || direction == 7){
			if (!game.maze[y/32][(x+18)/32] &&
					!game.maze[(y+16)/32][(x+18)/32]){
				nextX += 2;
			}
		}
		if (direction == 3 || direction == 4 || direction == 5){
			if (!game.maze[ y/32][(x-2)/32] &&
					!game.maze[(y+16)/32][(x-2)/32]){
				nextX -= 2;
			}
		}
		if (direction == 1 || direction == 2 || direction == 3){
			if (!game.maze[(y-2)/32][(x)/32] &&
					!game.maze[(y-2)/32][(x+16)/32]){
				nextY -= 2;
			}
		}
		if (direction == 5 || direction == 6 || direction == 7){
			if (!game.maze[(y+18)/32][(x)/32] &&
					!game.maze[(y+18)/32][(x+16)/32]){
				nextY += 2;
			}
		}
		while (nextX ==  x && nextY ==  y){
			direction = game.rng.nextInt(8)/2 * 2;
			nextX =  x;
			nextY =  y;
			if (direction == 0 || direction == 1 || direction == 7){
				if (!game.maze[ y/32][(x+18)/32] &&
						!game.maze[(y+16)/32][(x+18)/32]){
					nextX += 2;
				}
			}
			if (direction == 3 || direction == 4 || direction == 5){
				if (!game.maze[ y/32][(x-2)/32] &&
						!game.maze[(y+16)/32][(x-2)/32]){
					nextX -= 2;
				}
			}
			if (direction == 1 || direction == 2 || direction == 3){
				if (!game.maze[(y-2)/32][(x)/32] &&
						!game.maze[(y-2)/32][(x+16)/32]){
					nextY -= 2;
				}
			}
			if (direction == 5 || direction == 6 || direction == 7){
				if (!game.maze[(y+18)/32][(x)/32] &&
						!game.maze[(y+18)/32][(x+16)/32]){
					nextY += 2;
				}
			}
		}
		
		 x = nextX;
		 y = nextY;			
	}
}
