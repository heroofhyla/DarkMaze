package com.aezart.darkmaze;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

public class DungeonScene extends Scene{
	BufferedImage mapImage = new BufferedImage(608, 480, BufferedImage.TYPE_3BYTE_BGR);
	BufferedImage drawingSurface = new BufferedImage(608, 480, BufferedImage.TYPE_3BYTE_BGR);
	BufferedImage lightSurface = new BufferedImage(608, 480, BufferedImage.TYPE_4BYTE_ABGR);
	BufferedImage finalRender = new BufferedImage(608,480,BufferedImage.TYPE_3BYTE_BGR);
	
	Graphics mapG = mapImage.getGraphics();
	Graphics drawingG = drawingSurface.getGraphics();
	Graphics2D lightG2D = (Graphics2D) lightSurface.getGraphics();
	Graphics finalG = finalRender.getGraphics();
	
	int level = 0;
	boolean readyForNextLevel;
	boolean[][] maze = new boolean[15][19];
	boolean[][] coins = new boolean[7][9];
	Vector<Entity> entities = new Vector<Entity>();
	Vector<Entity> toRemove = new Vector<Entity>();
	Vector<Cloak> cloaks = new Vector<Cloak>(4);
	Knight knight;
	TextAlert textAlert;
	Stairs stairs;
	int lastTorch = -1;


	DarkMaze game;
	public DungeonScene(DarkMaze game){
		this.game = game;
		
		stairs = new Stairs(this);
		stairs.setPosition(new XYCoords(5, 5, 0, 0));
		entities.add(stairs);
		knight = new Knight(this);
		textAlert = new TextAlert(this);
		entities.add(knight);
		entities.add(textAlert);
		cloaks.add(new Cloak(this, -8,-8,23,23));
		cloaks.add(new Cloak(this, -24,-8,7,23));
		cloaks.add(new Cloak(this, -8,-24,23,7));
		cloaks.add(new Cloak(this, -24,-24,7,7));
		entities.addAll(cloaks);		
		nextLevel();

	}
	@Override
	public void tick() {
		for (Entity e: entities){
			e.tick();
		}
		
		if (readyForNextLevel){
			nextLevel();
			stairs.stairsReady = false;
			stairs.coinTarget = game.coinCount + 40;
			
			readyForNextLevel = false;

		}		
	}

	@Override
	public void draw(Graphics g) {
		drawingG.drawImage(mapImage, 0, 0, null);
		lightG2D.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
		lightG2D.fillRect(0, 0, 608, 480);
		lightG2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		
		for (int i = 0; i < coins.length; ++i){
			for (int k = 0; k < coins[0].length; ++k){
				if (coins[i][k]){
					drawingG.drawImage(game.tileset, k*64+32, i*64+32, k*64+64, i*64+64, 32, 0, 48, 16, null);
				}
			}
		}

		
		for (Entity e: entities){
			e.draw(drawingG);
			e.drawLights(lightG2D);
		}
		
		if (game.debug){
			lightG2D.fillRect(0, 0, 608, 480);
		}
		lightG2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN));
		lightG2D.drawImage(drawingSurface, 0, 0, null);
		finalG.setColor(Color.black);
		finalG.fillRect(0, 0, 608, 480);
		finalG.drawImage(lightSurface, 0, 0, null);
		
		finalG.setColor(Color.black);
		finalG.fillRect(0,468,608,480);
		finalG.setColor(Color.white);
		finalG.drawString("Lives: " + game.lives + " Depth: " + (10*level)  + "ft Coins: " + game.coinCount, 0, 478);

		for (Entity e: entities){
			e.drawEffects(finalG);
		}
		g.drawImage(finalRender, 0, 0, null);		
	}
	
	public void nextLevel(){
		++level;
		textAlert.showTextAlert("Depth: " + 10*level + "ft", 60);
		generateMaze(maze);
		resetEntities();
		paintBackground(mapG);
		for (int i = 0; i < coins.length; ++i){
			for (int k = 0; k < coins[0].length; ++k){
				coins[i][k] = true;
			}
			coins[3][4] = false;
			
		}

	}
	
	void resetEntities(){
		cloaks.get(0).setTile(1, 1, 8, 8);
		cloaks.get(1).setTile(17,1, 24, 8);
		cloaks.get(2).setTile(1,13, 8, 24);
		cloaks.get(3).setTile(17,13,24, 24);
		
		for (Cloak c: cloaks){
			c.playerLastSeen = new XYCoords(0,0);
			c.playerNextTurn = new XYCoords(0,0);
			c.playerStillInView = false;
			c.alertState = 0;
		}
		knight.setPosition(new XYCoords(9, 7, 8, 8));


	}
	
	void generateMaze(boolean[][] maze){
		Iterator<Entity> itr = entities.iterator();
		while (itr.hasNext()){
			Entity e = itr.next();
			if (e instanceof Torch){
				itr.remove();
			}
		}
		Vector<XYCoords> deadEnds = new Vector<XYCoords>();
		for (int i = 0; i < maze.length; ++i){
			for (int k = 0; k < maze[0].length; ++k){
				if (i%2 == 1 && k%2 == 1){
					maze[i][k] = false;
				}else{
					maze[i][k] = true;
				}
			}
		}
		boolean[][] visited = new boolean[(maze.length-1)/2][(maze[0].length-1)/2];
		int firstX = game.rng.nextInt(visited[0].length);
		int firstY = game.rng.nextInt(visited.length);
		
		deadEnds.add(new XYCoords(firstX, firstY, 0, 0));
		entities.add(new Torch(this));
		entities.lastElement().setTile(deadEnds.lastElement().xTile()*2 + 1, deadEnds.lastElement().yTile()*2 + 1, 8, 8);

		generateNext(firstX, firstY, maze, visited, deadEnds);
		
		for (XYCoords d: deadEnds){
			ArrayList<XYCoords> adjacentCells = new ArrayList<XYCoords>();
			if (d.xTile() > 0){
				adjacentCells.add(new XYCoords(2*d.xTile(),2*d.yTile()+1, 0, 0));
			}
			if (d.xTile() < (maze[0].length-1)/2-1){
				adjacentCells.add(new XYCoords(2*d.xTile()+2,2*d.yTile()+1, 0, 0));
			}
			if (d.yTile() > 0){
				adjacentCells.add(new XYCoords(2*d.xTile()+1,2*d.yTile(), 0, 0));
			}
			if (d.yTile() < (maze.length-1)/2-1){
				adjacentCells.add(new XYCoords(2*d.xTile()+1,2*d.yTile()+2, 0, 0));
			}
			Collections.shuffle(adjacentCells);
			for (XYCoords t: adjacentCells){
				if (maze[t.yTile()][t.xTile()]){
					maze[t.yTile()][t.xTile()] = false;
					break;
				}
			}
		}
	}
	
	void generateNext(int x, int y, boolean[][] maze, boolean[][] visited, Vector<XYCoords> deadEnds){
		visited[y][x] = true;
		ArrayList<XYCoords> adjacentCells = new ArrayList<XYCoords>();
		if (x > 0){
			adjacentCells.add(new XYCoords(x-1,y, 0, 0));
		}
		if (x < visited[0].length - 1){
			adjacentCells.add(new XYCoords(x+1,y, 0, 0));
		}
		if (y>0){
			adjacentCells.add(new XYCoords(x,y-1, 0, 0));
		}
		if (y < visited.length - 1){
			adjacentCells.add(new XYCoords(x,y+1, 0, 0));
		}
		
		Collections.shuffle(adjacentCells);
		int numVisited = 0;

		for (XYCoords d: adjacentCells){

			if (!visited[d.yTile()][d.xTile()]){
				++numVisited;
				int deltaX = x - d.xTile();
				int deltaY = y - d.yTile();
				
				maze[y*2 + 1 - deltaY][x*2 + 1 - deltaX] = false;
				generateNext(d.xTile(), d.yTile(), maze, visited, deadEnds);
			}
		}
		if (numVisited == 0){
			deadEnds.add(new XYCoords(x,y, 0, 0));
			entities.add(new Torch(this));
			entities.lastElement().setTile(deadEnds.lastElement().xTile()*2 + 1, deadEnds.lastElement().yTile()*2 + 1, 8, 8);
		}
	}
	
	void paintBackground(Graphics sg){
		for (int i = 0; i < maze.length; ++i){
			for (int k = 0; k < maze[0].length; ++k){
				sg.setColor(Color.white);
				sg.drawImage(game.tileset, 32*k, 32*i, 32*k+32, 32*i+32, 0, 0, 16, 16, null);
			}
		}
		sg.setColor(Color.black);
		for (int i = 0; i < maze.length; ++i){
			for (int k = 0; k < maze[0].length; ++k){
				if (maze[i][k]){
					sg.drawImage(game.wallshadow, 32*k-3, 32*i-3, null);
				}
			}
		}
		for (int i = 0; i < maze.length; ++i){
			for (int k = 0; k < maze[0].length; ++k){
				if (maze[i][k]){
					sg.drawImage(game.tileset, 32*k, 32*i, 32*k+32, 32*i+32, 16, 0, 32, 16, null);
				}
			}
		}
	}
	
}
