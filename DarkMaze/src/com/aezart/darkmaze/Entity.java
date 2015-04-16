package com.aezart.darkmaze;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Entity {
	public final static int EAST = 0;
	public final static int NORTHEAST = 1;
	public final static int NORTH = 2;
	public final static int NORTHWEST = 3;
	public final static int WEST = 4;
	public final static int SOUTHWEST = 5;
	public final static int SOUTH = 6;
	public final static int SOUTHEAST = 7;
	
	int bboxX1;
	int bboxX2;
	int bboxY1;
	int bboxY2;
	int drawXOffset;
	int drawYOffset;

	
	XYCoords position = new XYCoords(0,0);
	
	BufferedImage sprite;
	DarkMaze game;
		
	public Entity(BufferedImage sprite, DarkMaze game){
		this.sprite = sprite;
		this.game = game;
		bboxX1 = 0 - sprite.getWidth()/4;
		bboxY1 = 0 - sprite.getHeight()/4;
		bboxX2 = sprite.getWidth()/4;
		bboxY2 = sprite.getHeight()/4;
	}
	
	int x(){
		return position.x();
	}
	
	int y(){
		return position.y();
	}

	int xTile(){
		return position.xTile();
	}
	

	int yTile(){
		return position.yTile();
	}
	void setPosition(XYCoords xy){
		position = xy;
	}
	
	void setTile(int xTile, int yTile, int xOffset, int yOffset){
		position = new XYCoords(xTile, yTile, xOffset, yOffset);
	}
	//int directionTo(XYCoords t){
		//return directionTo(t.x(), t.y());
	//}
	
	//returns: int 0 through seven, with 0 east, 1 north east, 2 north, ... 7 southwest
	//behavior undefined when x == e.x && y == e.y
	//int directionTo(Entity e){
		//return directionTo(e.x(), e.y());
	//}
	
	int directionToTile(XYCoords xy){
		return directionToTile(xy.xTile(), xy.yTile());
	}
	
	int directionToTile(int xTile, int yTile){
		int deltaX = xTile - this.xTile();
		int deltaY = yTile - this.yTile();
		int absDeltaX = Math.abs(deltaX);
		int absDeltaY = Math.abs(deltaY);
		if (deltaX > 0){
			if (absDeltaX > absDeltaY){
				return EAST;
			}else if (deltaY > 0){
				return SOUTH;
			}else{
				return NORTH;
			}
		}
		if (deltaX < 0){
			if (absDeltaX > absDeltaY){
				return WEST;
			}else if (deltaY > 0){
				return SOUTH;
			}else{
				return NORTH;
			}
		}else{
			if (deltaY > 0){
				return SOUTH;
			}else{
				return NORTH;
			}
		}
	}
	/*int directionTo(int x, int y){
		if (this.x() > x){
			if (this.y() > y){
				return 3; 
			}
			if (this.y() < y){
				return 5;
			}
			return 4;
			
		}
		if (this.x() < x){
			if (this.y() > y){
				return 1;
			}
			if (this.y() < y){
				return 7;
			}
			return 0;
		}
		
		if (this.y() > y){
			return 2;
		}
		return 6;
		
	}*/

	public void draw(Graphics g) {
		g.drawImage(sprite, x()+drawXOffset, y()+drawYOffset, null);
	}
	//TODO: Don't assume light is 72x72
	public void drawLights(Graphics2D g){
		g.drawImage(game.light, x()-game.light.getWidth()/2 + drawXOffset, y()-game.light.getHeight()/2+drawYOffset, null);	
	}
	
	public void drawEffects(Graphics g){
		//nothing unless overridden
	}
	
	public boolean lineOfSight(Entity e){
		return lineOfSight(e.xTile(),e.yTile());
	}
	
	public boolean lineOfSight(XYCoords t){
		return lineOfSight(t.xTile(), t.yTile());
	}
	public boolean lineOfSight(int xTile, int yTile){
		boolean lineOfSight = false;
		if (yTile() == yTile){
			lineOfSight = true;
			for (int j = Math.min(xTile(), xTile); j < Math.max(xTile(), xTile); ++j){
				if (game.maze[yTile()][j]){
					lineOfSight = false;
				}
			}
		}
		if (xTile() == xTile){
			lineOfSight = true;
			for (int j = Math.min(yTile(), yTile); j < Math.max(yTile(), yTile); ++j){
				if (game.maze[j][xTile()]){
					lineOfSight = false;
				}
			}
		}
		
		return lineOfSight;
	}

	public void tick(){
		//nothing unless overridden
	}
	
	public boolean validMove(XYCoords xy){
		return validMove(xy.x(), xy.y());
	}
	public boolean validMove(int x, int y){
		XYCoords topLeft = new XYCoords(x+bboxX1, y+bboxY1);
		XYCoords bottomLeft = new XYCoords(x+bboxX1, y+bboxY2);
		
		XYCoords topRight = new XYCoords(x+bboxX2, y+bboxY1);
		XYCoords bottomRight = new XYCoords(x+bboxX2, y+bboxY2);

		if (game.maze[topLeft.yTile()][topLeft.xTile()]){
			return false;
		}
		if (game.maze[topRight.yTile()][topRight.xTile()]){
			return false;
		}
		if (game.maze[bottomLeft.yTile()][bottomLeft.xTile()]){
			return false;
		}
		if (game.maze[bottomRight.yTile()][bottomRight.xTile()]){
			return false;
		}
		return true;
	}
	
	public XYCoords nextCoord(int direction){
		int nextX = x();
		int nextY = y();
		if (direction == EAST || direction == NORTHEAST || direction == SOUTHEAST){
			nextX += 2;
		}
		if (direction == NORTHWEST || direction == WEST || direction == SOUTHWEST){
			nextX -= 2;
		}
		if (direction == NORTHWEST || direction == NORTH || direction == NORTHEAST){
			nextY -= 2;
		}
		if (direction == SOUTHWEST || direction == SOUTH || direction == SOUTHEAST){
			nextY += 2;
		}
		
		return new XYCoords(nextX, nextY);
	}
}
