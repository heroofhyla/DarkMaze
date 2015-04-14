package com.aezart.darkmaze;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Entity {
	int bboxX1;
	int bboxX2;
	int bboxY1;
	int bboxY2;
	int drawXOffset;
	int drawYOffset;

	
	XYCoords position = XYCoords.fromAbsolute(0,0);
	
	BufferedImage sprite;
	DarkMaze game;
		
	public Entity(BufferedImage sprite, DarkMaze game){
		this.sprite = sprite;
		this.game = game;
		bboxX1 = 0 - sprite.getWidth()/2;
		bboxY1 = 0 - sprite.getHeight()/2;
		bboxX2 = sprite.getWidth()/2;
		bboxY2 = sprite.getHeight()/2;
		
		drawXOffset = -sprite.getWidth()/2;
		drawYOffset = -sprite.getHeight()/2;
	}
	
	int x(){
		return position.x();
	}
	
	int y(){
		return position.y();
	}

	int xTile(int xOffset){
		return position.xTile(xOffset);
	}
	

	int yTile(int yOffset){
		return position.yTile(yOffset);
	}
	void setPosition(XYCoords xy){
		position.set(xy);
	}
	
	void setTile(int xTile, int yTile, int xOffset, int yOffset){
		position.setTile(xTile, yTile, xOffset, yOffset);
	}
	int directionTo(XYCoords t){
		return directionTo(t.x(), t.y());
	}
	//returns: int 0 through seven, with 0 east, 1 north east, 2 north, ... 7 southwest
	//behavior undefined when x == e.x && y == e.y
	int directionTo(Entity e){
		return directionTo(e.x(), e.y());
	}
	
	int directionTo(int x, int y){
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
		
	}

	public void draw(Graphics g) {
		g.drawImage(sprite, x()+drawXOffset, y()+drawYOffset, null);
	}
	//TODO: Don't assume light is 72x72
	public void drawLights(Graphics2D g){
		g.drawImage(game.light, x()-28 + drawXOffset, y()-28+drawYOffset, null);	
	}
	
	public void drawEffects(Graphics g){
		//nothing unless overridden
	}
	
	public boolean lineOfSight(Entity e){
		return lineOfSight(e.xTile(0),e.yTile(0));
	}
	
	public boolean lineOfSight(XYCoords t){
		return lineOfSight(t.xTile(0), t.yTile(0));
	}
	public boolean lineOfSight(int xTile, int yTile){
		boolean lineOfSight = false;
		if (yTile(0) == yTile){
			lineOfSight = true;
			for (int j = Math.min(xTile(0), xTile); j < Math.max(xTile(0), xTile); ++j){
				if (game.maze[yTile(0)][j]){
					lineOfSight = false;
				}
			}
		}
		if (xTile(0) == xTile){
			lineOfSight = true;
			for (int j = Math.min(yTile(0), yTile); j < Math.max(yTile(0), yTile); ++j){
				if (game.maze[j][xTile(0)]){
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
		XYCoords topLeft = XYCoords.fromAbsolute(x+bboxX1, y+bboxY1);
		XYCoords bottomLeft = XYCoords.fromAbsolute(x+bboxX1, y+bboxY2);
		
		XYCoords topRight = XYCoords.fromAbsolute(x+bboxX2, y+bboxY1);
		XYCoords bottomRight = XYCoords.fromAbsolute(x+bboxX2, y+bboxY2);

		if (game.maze[topLeft.yTile(0)][topLeft.xTile(0)]){
			return false;
		}
		if (game.maze[topRight.yTile(0)][topRight.xTile(0)]){
			return false;
		}
		if (game.maze[bottomLeft.yTile(0)][bottomLeft.xTile(0)]){
			return false;
		}
		if (game.maze[bottomRight.yTile(0)][bottomRight.xTile(0)]){
			return false;
		}
		return true;
	}
	
	public XYCoords nextCoord(int direction){
		int nextX = x();
		int nextY = y();
		if (direction == 0 || direction == 1 || direction == 7){
			nextX += 2;
		}
		if (direction == 3 || direction == 4 || direction == 5){
			nextX -= 2;
		}
		if (direction == 1 || direction == 2 || direction == 3){
			nextY -= 2;
		}
		if (direction == 5 || direction == 6 || direction == 7){
			nextY += 2;
		}
		
		return XYCoords.fromAbsolute(nextX, nextY);
	}
}
